package daikon.parser.structure;

import java.util.List;

public class Size implements Invariant {

    private Variable left;
    private Variable right;
    private OP operator;


    public Size(String left, String right, OP operator) {
        this.left = new Variable(left);
        this.right = new Variable(right);
        this.operator = operator;
    }

    @Override
    public boolean talks(List<String> vars) {
        return this.left.talks(vars) || this.right.talks(vars);
    }

    @Override
    public void replace(List<String> vars) {
        this.left.replace(vars);
        this.right.replace(vars);
    }
}
