package daikon.parser.structure;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Invariant {
    boolean talks(List<String> vars);

    enum OP {
        L, LE, GE, G,
        EQ, NEQ
    }
    default String replace(List<String> vars, String val) {
        Pattern pattern = Pattern.compile("arg([0-9]+)");
        Matcher matcher = pattern.matcher(val);
        String out = val;
        if(matcher.matches() && matcher.groupCount() > 0){
            int idx = Integer.parseInt(matcher.group(1));
            if(idx < vars.size()) //we are not a boolean parameter
                out = vars.get(idx);
        }
        return out;
    }
    void replace(List<String> vars);
}
