package daikon.parser.structure;

import java.util.ArrayList;
import java.util.List;

public class MethodInvariants {
    boolean isClass;
    boolean isObject;
    boolean isEnter;
    boolean isExit;
    boolean isInvariant;
    int lineExit;
    int lineInvariant;
    String method;
    List<String> pars;
    List<String> timeVars = new ArrayList<>();

    Boolean isPure = null;
    List<Invariant> invs = new ArrayList<>();
    private int setFileLine;

    public MethodInvariants(boolean isClass, boolean isObject, boolean isEnter, boolean isExit, int lineExit, String method, List<String> pars) {
        this.isClass = isClass;
        this.isObject = isObject;
        this.isEnter = isEnter;
        this.isExit = isExit;
        this.lineExit = lineExit;
        this.method = method;
        this.pars = pars;
        this.isInvariant = method.contains("invariant_");
        if(this.isInvariant) {
            String[] parts = method.substring(method.indexOf("invariant_")).split("_");
            lineInvariant = Integer.parseInt(parts[1]);
            for(int i = 2; i < parts.length; i++){
                timeVars.add(parts[i]);
            }
        }
    }

    public void setFileLine(int line){
        this.setFileLine = line;
    }

    public void add(Invariant inv) {
        if(inv == null) // we can reduce some invs that we do not care, e.g. elements of
            return;
        //replace args with concrete name
        inv.replace(timeVars);
        invs.add(inv);
        //we need to compute again if it is pure or not
        isPure = null;
    }

    public List<Invariant> getInvs(List<String> vars){
        List<Invariant> out = new ArrayList<>();
        for(Invariant i : invs){
            if(i.talks(vars)){
                out.add(i);
            }
        }
        return out;
    }

    public boolean isMethod(String method){
        return (this.isEnter || this.isExit) && this.method.equals(method);
    }
    public boolean isMethod(String method, String[] pars){
        boolean f1 = (this.isEnter || this.isExit) && this.method.equals(method);
        boolean f2 = pars.length == this.pars.size();
        if(!f2) return false;
        boolean f3 = true;
        for(int i = 0; i < pars.length; i++){
            String p = this.pars.get(i);
            if(!p.endsWith(pars[i]))
                f3 = false;
        }
        return f1 && f3;
    }

    public boolean isClass() {
        return isClass;
    }

    public boolean isObject() {
        return isObject;
    }

    public boolean isEnter() {
        return isEnter;
    }

    public boolean isExit() {
        return isExit;
    }

    public boolean isPure(List<String> vars) {
        if (isPure == null) {
            isPure = true;
            if(invs.size() > 0) {
                for (String p : vars) {
                    boolean flag = false;
                    for (Invariant i : invs) {
                        if (i instanceof Orig) {
                            Orig o = (Orig) i;
                            if (o.containsVar(p) && o.isSameVar()) {
                                flag = true;
                            }
                        }
                    }
                    isPure &= flag;
                }
            }
        }
        return isPure;
    }
}
