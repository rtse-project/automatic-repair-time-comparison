package daikon.parser.structure;

import java.util.List;

public class Implication implements Invariant {

    private Invariant left;
    private Invariant right;


    public Implication(Invariant left, Invariant right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean talks(List<String> vars) {
        return left.talks(vars) || right.talks(vars);
    }

    @Override
    public void replace(List<String> vars) {
        if(left != null)
            left.replace(vars);
        if(right != null)
            right.replace(vars);
    }
}
