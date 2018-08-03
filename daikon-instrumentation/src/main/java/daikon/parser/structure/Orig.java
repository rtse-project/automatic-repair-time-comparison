package daikon.parser.structure;

import java.util.List;

public class Orig implements Invariant {

    private Variable orig;
    private Variable right;
    private OP operator;


    public Orig(String orig, OP operator, String right) {
        this.orig = new Variable(orig.trim());
        this.right = new Variable(right.trim());
        this.operator = operator;
    }

    public boolean isSameVar(){
        return orig.equals(right);
    }

    public boolean containsVar(String p) {
        //remove this. from all
        String pp = p.replace("this.", "");
        String oo = orig.getName().replace("this.", "");
        String rr = right.getName().replace("this.", "");
        return pp.equals(oo) || pp.equals(rr);
    }

    @Override
    public boolean talks(List<String> vars) {
        return this.orig.talks(vars) || this.right.talks(vars);
    }

    @Override
    public void replace(List<String> vars) {
        this.orig.replace(vars);
        this.right.replace(vars);
    }
}
