package timeDetection;

import intermediateModel.interfaces.IASTMethod;
import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import org.junit.Before;
import org.junit.Test;
import slicing.Slice;
import slicing.TimeElement;
import slicing.TimeStatements;
import slicing.model.Assignment;
import slicing.model.DoWhile;
import slicing.model.If;
import slicing.model.Method;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestTimeSemanticDoWhile {

    ASTClass c;

    @Before
    public void setUp() throws Exception {
        String dir = load("config/methods.csv");
        dir = dir.substring(0, dir.lastIndexOf("/"));
        TemporalInfo.getInstance().loadUserDefined(dir);
        String f = load("semantic/DoWhile.java");
        List<ASTClass> cs = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/")));
        c = cs.get(0);
    }

    @Test
    public void RunIt() throws Exception {
        Slice.slice(c);
        List<TimeElement> l = TimeStatements.getInstance().getStms();
        for(TimeElement t : l){
            //System.out.println( t.getStm().getCode() + " :: " + t.getType());
        }
    }

    @Test
    public void TestListStms() throws Exception {
        Slice.slice(c);
        List<TimeElement> l = TimeStatements.getInstance().getStms();
        assertTrue("There should be some statements", l.size()>0);
        int[] lines = {50,51,56,61,62};
        assertEquals(l.size(), lines.length);
        for(int i = 0; i < lines.length; i++){
            assertEquals(lines[i], l.get(i).getStm().getLine());
        }
    }

    @Test
    public void TestReducedModel() throws Exception {
        HashMap<IASTMethod, Method> slice = Slice.slice(c, false);
        assertEquals(slice.size(), 1);
        Method m;
        m = slice.get(c.getFirstMethodByName("poll"));
        assertNotNull(m);
        assertEquals(3, m.getBody().size());
        assertEquals(Assignment.class, m.getBody().get(0).getClass());
        assertEquals(Assignment.class, m.getBody().get(1).getClass());
        assertEquals(DoWhile.class, m.getBody().get(2).getClass());

        DoWhile w = (DoWhile) m.getBody().get(2);
        assertEquals(3, w.getWhileBody().size());
        assertNotNull(w.getExpr());

        assertEquals(If.class, w.getWhileBody().get(0).getClass());
        assertEquals(If.class, w.getWhileBody().get(1).getClass());
        assertEquals(Assignment.class, w.getWhileBody().get(2).getClass());

        If first = (If) w.getWhileBody().get(0);
        assertEquals(1, first.getIfBody().size());
        assertEquals(0, first.getElseBody().size());
        assertNull(first.getExpr());

        assertEquals(Assignment.class, first.getIfBody().get(0).getClass());

        If second = (If) w.getWhileBody().get(1);
        assertEquals(0, second.getIfBody().size());
        assertEquals(0, second.getElseBody().size());
        assertNull(second.getExpr());

    }



    private String load(String s) {
        return TestTimeSemanticDoWhile.class.getClassLoader().getResource(s).getFile();
    }
}
