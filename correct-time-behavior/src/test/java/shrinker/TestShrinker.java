package shrinker;

import com.rits.cloning.Cloner;
import intermediateModel.interfaces.IASTMethod;
import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import org.junit.Before;
import org.junit.Test;
import slicing.Shrinker;
import slicing.Slice;
import slicing.TimeElement;
import slicing.TimeStatements;
import slicing.model.Assignment;
import slicing.model.If;
import slicing.model.Method;
import slicing.model.While;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestShrinker {

    ASTClass c;

    @Before
    public void setUp() throws Exception {
        String dir = load("config/methods.csv");
        dir = dir.substring(0, dir.lastIndexOf("/"));
        TemporalInfo.getInstance().loadUserDefined(dir);
        String f = load("shrinker/ShrinkTest.java");
        List<ASTClass> cs = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/")));
        c = cs.get(0);
    }

    @Test
    public void RunIt() throws Exception {
        Slice.slice(c);
        List<TimeElement> l = TimeStatements.getInstance().getStms();
        for(TimeElement t : l){
            System.out.println( t.getStm().getCode() + " :: " + t.getType());
        }
    }

    @Test
    public void TestListStms() throws Exception {
        Slice.slice(c);
        List<TimeElement> l = TimeStatements.getInstance().getStms();
        assertTrue("There should be some statements", l.size()>0);
        int[] lines = {50,51,53,60,63,74,78};
        assertEquals(l.size(), lines.length);
        for(int i = 0; i < lines.length; i++){
            assertEquals(lines[i], l.get(i).getStm().getLine());
        }
    }

    @Test
    public void TestShrinkModel() throws Exception {
        HashMap<IASTMethod, Method> slice = Slice.slice(c);
        assertEquals(slice.size(), 1);
        Method m = null;
        m = slice.get(c.getFirstMethodByName("poll"));
        assertNotNull(m);
        assertEquals(4, m.getBody().size());
        assertEquals(Assignment.class, m.getBody().get(0).getClass());
        assertEquals(Assignment.class, m.getBody().get(1).getClass());
        assertEquals(Assignment.class, m.getBody().get(3).getClass());

        assertEquals(While.class, m.getBody().get(2).getClass());
        While wBody = (While) m.getBody().get(2);
        assertNotNull(wBody.getExpr());
        assertEquals(3, wBody.getWhileBody().size());

        assertEquals(If.class, wBody.getWhileBody().get(0).getClass());
        assertEquals(Assignment.class, wBody.getWhileBody().get(1).getClass());
        assertEquals(If.class, wBody.getWhileBody().get(2).getClass());

        If first;
        If second;
        first = (If) wBody.getWhileBody().get(0);
        second= (If) wBody.getWhileBody().get(2);
        assertNull(first.getExpr());
        assertNull(second.getExpr());
        assertEquals(1, first.getIfBody().size());
        assertEquals(0, second.getIfBody().size());
        assertEquals(0, first.getElseBody().size());
        assertEquals(1, second.getElseBody().size());
    }

    @Test
    public void EqualSlice() throws Exception {
        HashMap<IASTMethod, Method> slice = Slice.slice(c);
        assertEquals(slice.size(), 1);
        HashMap<IASTMethod, Method> noSlice = Slice.slice(c,false);
        assertEquals(noSlice.size(), 1);

        Method m1;
        m1 = slice.get(c.getFirstMethodByName("poll"));
        assertNotNull(m1);

        Method m2;
        m2 = noSlice.get(c.getFirstMethodByName("poll"));
        assertNotNull(m2);

        assertFalse(m1.equals(m2));
    }

    @Test
    public void TestIdempotent() throws Exception {
        HashMap<IASTMethod, Method> slice = Slice.slice(c);
        assertEquals(slice.size(), 1);

        Method m;
        m = slice.get(c.getFirstMethodByName("poll"));
        assertNotNull(m);

        Cloner cloner = new Cloner();
        Method m2 = cloner.deepClone(m);
        assertNotNull(m2);
        assertTrue(m.equals(m2));

        Shrinker.shrink(m);
        assertTrue(m.equals(m2));
        Shrinker.shrink(m);
        assertTrue(m.equals(m2));
        Shrinker.shrink(m2);
        assertTrue(m.equals(m2));
    }

    private String load(String s) {
        return TestShrinker.class.getClassLoader().getResource(s).getFile();
    }
}
