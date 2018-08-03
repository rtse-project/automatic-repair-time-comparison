package daikon.invariant;

import daikon.parser.Parser;
import daikon.parser.structure.MethodInvariants;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Reader {
    public static List<MethodInvariants> readInvariant(String file) throws IOException {
        Parser p = new Parser();
        List<MethodInvariants> l = p.calculate(Paths.get(file));
        return l;
    }
}
