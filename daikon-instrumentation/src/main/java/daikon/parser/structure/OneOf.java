package daikon.parser.structure;

import java.util.List;

public class OneOf implements Invariant {

    private Variable left;
    private List<String> elms;

    public OneOf(String left, List<String> elms) {
        this.left = new Variable(left);
        this.elms = elms;
    }

    @Override
    public boolean talks(List<String> vars) {
        return this.left.talks(vars);
    }

    @Override
    public void replace(List<String> vars) {
        this.left.replace(vars);
    }
}
