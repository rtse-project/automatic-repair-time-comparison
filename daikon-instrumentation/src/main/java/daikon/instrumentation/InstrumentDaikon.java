package daikon.instrumentation;

import daikon.instrumentation.preprocess.Utility;
import daikon.instrumentation.preprocess.WatchPoint;
import daikon.instrumentation.preprocess.WatchPoints;
import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by giovanni on 27/04/2017.
 */
public class InstrumentDaikon implements ClassFileTransformer  {
    private static final Logger LOGGER = LogManager.getLogger();
    private static WatchPoints watchPoints = WatchPoints.getInstance();
    private static List<String> injected = new ArrayList<>();
    private static List<String> injectedMethod = new ArrayList<>();

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;
        //LOGGER.debug("Analysing class {}", className);
        ClassPool pool = ClassPool.getDefault();
        //LOGGER.debug(className);
        if(watchPoints.shouldWatch(className)){
            String clsName = Utility.convertJavaToPkgName(className);
            LOGGER.debug("Editing bytecode of class {}", clsName);
            if(injected.contains(clsName)){
                LOGGER.debug("Already injected");
                return byteCode;
            }
            injected.add(clsName);
            try {
                List<String> getMethodDefs = watchPoints.getMethodDefinitions(className);
                CtClass cc = pool.get(Utility.convertJavaToPkgName(className));
                LOGGER.debug(pool);
                LOGGER.debug("Writing: {}", cc.getClassFile().getName());
                //generating the invariant_%line%(_%vars%)+ methods
                //DataOutputStream out = new DataOutputStream(new FileOutputStream("Base.class"));
                int version = 0;
                //cc.getClassFile().write(out);
                for(String m : getMethodDefs) {
                    if(injectedMethod.contains(m)){
                        continue;
                    }
                    //LOGGER.debug("Creating the method {}", m);
                    CtMethod mNew = CtNewMethod.make(m, cc);
                    cc.addMethod(mNew);
                    injectedMethod.add(m);
                    //LOGGER.debug("Injected method: " + m);
                }
                //generating the method calls
                List<WatchPoint> points = watchPoints.getWatchPoints(className);
                for(WatchPoint p : points){
                    String methodName = p.getMethodName();
                    try {
                        CtMethod method = cc.getDeclaredMethod(methodName);
                        try {
                            method.insertAt(p.getLine() - 1, p.printAsMethodCall());
                            LOGGER.debug("Injected method {} @ {}", p.printAsMethodCall(), p.getLine());
                        } catch (Exception e){
                            LOGGER.debug("[V:{}]Injecting call {} @ {}", version++, p.printAsMethodCall(), p.getLine());
                            LOGGER.debug("Problem with insert At {}", p.getLine()-1);
                            LOGGER.debug("Motivation: {}", e.getMessage());
                        }
                    } catch (Exception e){
                        LOGGER.debug("[V:{}]Injecting call {} @ {}", version++, p.printAsMethodCall(), p.getLine());
                        LOGGER.debug("Cannot find method");
                    }
                    //out = new DataOutputStream(new FileOutputStream("Base_" + version++ + ".class"));
                    //cc.getClassFile().write(out);
                }
                cc.writeFile();
                byteCode = cc.toBytecode();
                cc.detach();
                LOGGER.debug("Injection for class {} completed!", className);

            } catch (Exception e){
                LOGGER.debug("[FAIL] Injection failed: {}", className);
                LOGGER.debug(e.getMessage());
                LOGGER.debug("STACK TRACE");
                for(StackTraceElement s : e.getStackTrace())
                    LOGGER.debug(s);
            }
        }
        return byteCode;
    }
}
