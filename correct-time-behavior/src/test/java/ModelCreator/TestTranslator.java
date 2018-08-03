package ModelCreator;

import com.microsoft.z3.Optimize;
import intermediateModel.interfaces.IASTMethod;
import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.creation.JDTVisitor;
import org.junit.Before;
import org.junit.Test;
import slicing.Slice;
import slicing.model.Method;
import smt.ModelCreator;
import smt.TranslateReducedModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestTranslator {

    TranslateReducedModel translateReducedModel;
    Optimize opt;
    String model;

    @Before
    public void setUp() throws Exception {
        ModelCreator._debug_ = !true;
        String code = load("translator/code.java");
        model = read("translator/model.smt");
        ASTClass c = JDTVisitor.parse(code, code.substring(0, code.lastIndexOf("/"))).get(0);
        IASTMethod mm = c.getFirstMethodByName("poll");
        Method m = Slice.slice(c).get(mm);
        translateReducedModel = new TranslateReducedModel();
        opt = translateReducedModel.convert(m);
    }

    @Test
    public void TestModel() throws Exception {
        System.out.println(opt);
        assertEquals(model, opt.toString());
    }

    @Test
    public void TestPushModel() throws Exception {
        List<String> models = translateReducedModel.getPushModel();
        assertEquals(3, models.size());
        Collections.reverse(models);
        /*int i = 0;
        for(String m : models){
            i++;
            System.out.println("Model " + i);
            System.out.println(m + "\n");
        }*/
        String expected;
        expected = read("translator/model_push1.smt");
        assertEquals(expected, models.get(0));
        expected = read("translator/model_push2.smt");
        assertEquals(expected, models.get(1));
        assertEquals(expected, models.get(2));
    }

    private String load(String file) {
        return TestTranslator.class.getClassLoader().getResource(file).getFile();
    }

    private String read(String file) throws IOException {
        Path p = Paths.get(load(file));
        List<String> lines = Files.readAllLines(p);
        StringBuilder sb = new StringBuilder();
        for(String l : lines) {
            l = l.trim();
            if(l.startsWith(";")) continue;
            if(l.startsWith("\n")) continue;
            if(l.equals("")) continue;
            sb.append(l + "\n");
        }
        return sb.toString();
    }

}
