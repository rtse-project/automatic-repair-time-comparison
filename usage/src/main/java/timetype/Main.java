package timetype;

import debugger.Debugger;
import intermediateModel.structure.ASTClass;
import intermediateModel.types.TimeTypeSystem;
import intermediateModel.types.rules.exception.TimeTypeRecommendation;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModel.visitors.creation.filter.ElseIf;
import intermediateModelHelper.envirorment.temporal.structure.TimeTypes;
import intermediateModelHelper.envirorment.temporalTypes.TemporalTypes;
import intermediateModelHelper.envirorment.temporalTypes.structure.TimeParameterMethod;
import intermediateModelHelper.indexing.IndexingProject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by giovanni on 17/07/2017.
 */
public class Main {

    static Debugger debugger = Debugger.getInstance();

    static {
        debugger.setActive(false);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage with: name root_path");
            System.exit(0);
        }
        try {
            new Main().do_job(args);
            debugger.stop();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            debugger.stop();
        }
    }

    public void do_job(String[] args) throws Exception {

        //get root path
        String name = args[0];
        String root_path = args[1];

        long start = System.currentTimeMillis();

        TemporalTypes.getInstance().loadUserDefinedPrefix(name);

        int lastCount;
        int count = 0;
        do {
            lastCount = count;
            // Indexing RT
            {
                List<TimeTypes> rt = IndexingProject.getMethodReturnTime(name, root_path, true);
                TemporalTypes.getInstance().addRT(rt);
                TemporalTypes.getInstance().loadUserDefinedPrefix(name);
            }
            // Indexing ET
            {
                List<TimeParameterMethod> et = IndexingProject.getMethodTimeParameter(name, root_path, true);
                TemporalTypes.getInstance().addET(et);
                TemporalTypes.getInstance().loadUserDefinedPrefix(name);
            }
            count = TemporalTypes.getInstance().size();
        } while(lastCount != count);
        System.out.println("Indexing - Done");

        //get all files
        Iterator<File> i = IndexingProject.getJavaFiles(root_path);

        List<TimeTypeRecommendation> e = new ArrayList<>();
        while (i.hasNext()) {
            String filename = i.next().getAbsolutePath();
            if(filename.contains("/src/test/")) continue; //skip tests
//            if(!filename.endsWith("LeaseDatabaseLocker.java")) continue; //skip tests
            debugger.log("Parsing: " + filename);
            //System.out.println(filename);
            List<ASTClass> classes = JDTVisitor.parse(filename, root_path, ElseIf.filter);
            for (ASTClass c : classes) {
                TimeTypeSystem tts = new TimeTypeSystem();
                tts.start(c);
                List<TimeTypeRecommendation> errors = tts.getRecommendation();
                //e.addAll(errors);
                for(TimeTypeRecommendation tte : errors){
                    if(!e.contains(tte)){
                        e.add(tte);
                        //System.out.println(tte.getFullMessage());
                    }
                }
                if(errors.size() > 0) {
                    repair(c, errors);
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Total Time [ms]: " + (end - start));
        System.out.println("Total # Errors : " + e.size());
        System.out.println("======= ERRORS =======");
        for(TimeTypeRecommendation err : e){
            System.out.println(err.getFullMessage());
        }
        BufferedWriter writerErr = new BufferedWriter(new FileWriter("errors.log", true));
        for(TimeTypeRecommendation err : e){
            writerErr.append(err.getFullMessage());
            writerErr.append("\n");
        }
        writerErr.flush();
        writerErr.close();
    }

    private void repair(ASTClass c, List<TimeTypeRecommendation> errors) {
        String name = c.fullName();
        try (PrintWriter out = new PrintWriter(name)) {
            String code = c.repair(errors);
            out.println(code);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
