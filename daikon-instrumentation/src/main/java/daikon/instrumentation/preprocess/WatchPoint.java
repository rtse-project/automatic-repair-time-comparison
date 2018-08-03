package daikon.instrumentation.preprocess;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class WatchPoint {
    String className;
    String methodName;
    int line;
    Set<Variable> vars;

    public WatchPoint(String className, String methodName, int line, Set<String> vars)  {
        this.className = className;
        this.methodName = methodName;
        this.line = line;
        this.vars = new LinkedHashSet<>();
        for(String s : vars){
            this.vars.add(new Variable(s));
        }
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getLine() {
        return line;
    }

    public boolean isTheOne(String className){
        return this.className.equals(className);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("%s;%s;%d;%s\n", className, methodName, line, Arrays.toString(vars.toArray())));
        return sb.toString();
    }

    public String printAsMethodDef() {
        StringBuilder name;
        String pars = "";
        name = new StringBuilder("invariant_" + this.line);
        for(Variable v : vars){
            name.append("_").append(v.getName());
        }
        for(Variable v : vars){
            pars += v.getType() + " " + v.getName() + ",";
        }
        //checks
        for(int i = 0; i < vars.size()-1; i++){
            pars += "boolean b" + i + ",";
        }
        if(pars.length() > 0) {
            pars = pars.substring(0, pars.length()-1);
        }
        return String.format("public void %s(%s){}", name.toString(), pars);
    }

    public String printAsMethodCall() {
        StringBuilder name;
        String pars = "";
        name = new StringBuilder("invariant_" + this.line);
        for(Variable v : vars){
            name.append("_").append(v.name);
        }
        for(Variable v : vars){
            pars += v.getName() + ",";
        }
        //checks
        Variable[] varSet = vars.toArray(new Variable[0]);
        for(int i = 2; i <= varSet.length; i++){
            Variable v1 = varSet[i-2];
            Variable v2 = varSet[i-1];
            pars += v1.getName() + "<=" + v2.getName() + ",";
        }
        if(pars.length() > 0) {
            pars = pars.substring(0, pars.length()-1);
        }
        return String.format("{%s(%s);}", name.toString(), pars);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof WatchPoint)) return false;
        WatchPoint p = (WatchPoint)o;
        return this.line == p.line;
    }
}
