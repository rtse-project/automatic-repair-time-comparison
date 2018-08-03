package daikon.parser.structure;

import java.util.List;

public class Mod implements Invariant {

    private Variable var;
    private String mod;


    public Mod(String var, String mod) {
        this.var = new Variable(var);
        this.mod = mod;
    }

    @Override
    public boolean talks(List<String> vars) {
        return this.var.talks(vars);
    }

    @Override
    public void replace(List<String> vars) {
        this.var.replace(vars);
    }
}
