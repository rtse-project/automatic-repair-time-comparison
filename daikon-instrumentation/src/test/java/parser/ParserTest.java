package parser;

import daikon.parser.Parser;
import daikon.parser.structure.MethodInvariants;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

public class ParserTest {

    @Test
    public void name() throws Exception {
        String file = ParserTest.class.getClassLoader().getResource("only_injected_invariants.txt").getFile();
        //Parser p = new Parser(new FileReader(file));
        //p.run();
        Parser p = new Parser();
        List<MethodInvariants> l = p.calculate(Paths.get(file));
        for(MethodInvariants m : l)
            System.out.print('\r');
    }
}
