package pathGenerator;

import intermediateModel.interfaces.IASTMethod;
import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import org.junit.Test;
import slicing.Slice;
import slicing.model.Assignment;
import slicing.model.If;
import slicing.model.Method;
import slicing.model.While;
import smt.PathGenerator;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestPathGenerator {

    PathGenerator pg = new PathGenerator();

    @Test
    public void RunIt() throws Exception {
        Method m = load("pathGenerator/code.java");
        List<Method> mm = pg.generate(m);
        for(Method _m : mm){
            System.out.println(_m);
        }
    }

    @Test
    public void TestNoPath() throws Exception {
        Method m = load("pathGenerator/noPath.java");
        List<Method> mm = pg.generate(m);
        assertEquals(1, mm.size());
        Method m1 = mm.get(0);
        assertEquals(2, m1.getBody().size());
    }

    @Test
    public void TestIfPath() throws Exception {
        Method m = load("pathGenerator/ifPath.java");
        List<Method> mm = pg.generate(m);
        assertEquals(2, mm.size());
        Method m1 = mm.get(0);
        assertEquals(3, m1.getBody().size());
        assertEquals(Assignment.class, m1.getBody().get(0).getClass());
        assertEquals(Assignment.class, m1.getBody().get(1).getClass());
        assertEquals(If.class, m1.getBody().get(2).getClass());
        If _if = (If) m1.getBody().get(2);
        assertEquals(1, _if.getIfBody().size());
        assertEquals(Assignment.class, _if.getIfBody().get(0).getClass());

        Method m2 = mm.get(1);
        assertEquals(2, m2.getBody().size());
        assertEquals(Assignment.class, m2.getBody().get(0).getClass());
        assertEquals(Assignment.class, m2.getBody().get(1).getClass());

    }

    @Test
    public void TestWhilePath() throws Exception {
        Method m = load("pathGenerator/whilePath.java");
        List<Method> mm = pg.generate(m);
        assertEquals(2, mm.size());
        Method m1 = mm.get(0);
        assertEquals(3, m1.getBody().size());
        assertEquals(Assignment.class, m1.getBody().get(0).getClass());
        assertEquals(Assignment.class, m1.getBody().get(1).getClass());
        assertEquals(While.class, m1.getBody().get(2).getClass());
        While _w = (While) m1.getBody().get(2);
        assertEquals(2, _w.getWhileBody().size());
        assertEquals(Assignment.class, _w.getWhileBody().get(0).getClass());
        assertEquals(Assignment.class, _w.getWhileBody().get(1).getClass());

        Method m2 = mm.get(1);
        assertEquals(2, m2.getBody().size());
        assertEquals(Assignment.class, m2.getBody().get(0).getClass());
        assertEquals(Assignment.class, m2.getBody().get(1).getClass());

    }

    @Test
    public void TestCodePath() throws Exception {
        Method m = load("pathGenerator/code.java");
        List<Method> mm = pg.generate(m);
        assertEquals(4, mm.size());
        {
            Method m0 = mm.get(0);
            assertEquals(3, m0.getBody().size());
            assertEquals(Assignment.class, m0.getBody().get(0).getClass());
            assertEquals(Assignment.class, m0.getBody().get(1).getClass());
            assertEquals(While.class, m0.getBody().get(2).getClass());
            While w = (While) m0.getBody().get(2);
            assertEquals(4, w.getWhileBody().size());
            assertNotNull(w.getExpr());
            assertEquals(If.class, w.getWhileBody().get(0).getClass());
            assertEquals(If.class, w.getWhileBody().get(1).getClass());
            assertEquals(Assignment.class, w.getWhileBody().get(2).getClass());
            assertEquals(Assignment.class, w.getWhileBody().get(3).getClass());
            If _if1 = (If) w.getWhileBody().get(0);
            assertNull(_if1.getExpr());
            assertEquals(1, _if1.getIfBody().size());
            assertEquals(0, _if1.getElseBody().size());
            assertEquals(Assignment.class, _if1.getIfBody().get(0).getClass());
            If _if2 = (If) w.getWhileBody().get(1);
            assertNull(_if2.getExpr());
            assertEquals(1, _if2.getIfBody().size());
            assertEquals(0, _if2.getElseBody().size());
            assertEquals(Assignment.class, _if2.getIfBody().get(0).getClass());
        }
        {
            Method m1 = mm.get(1);
            assertEquals(2, m1.getBody().size());
            assertEquals(Assignment.class, m1.getBody().get(0).getClass());
            assertEquals(Assignment.class, m1.getBody().get(1).getClass());
        }
        {
            Method m2 = mm.get(2);
            assertEquals(3, m2.getBody().size());
            assertEquals(Assignment.class, m2.getBody().get(0).getClass());
            assertEquals(Assignment.class, m2.getBody().get(1).getClass());
            assertEquals(While.class, m2.getBody().get(2).getClass());
            While w = (While) m2.getBody().get(2);
            assertEquals(3, w.getWhileBody().size());
            assertNotNull(w.getExpr());
            assertEquals(If.class, w.getWhileBody().get(0).getClass());
            assertEquals(Assignment.class, w.getWhileBody().get(1).getClass());
            assertEquals(Assignment.class, w.getWhileBody().get(2).getClass());
            If _if1 = (If) w.getWhileBody().get(0);
            assertNull(_if1.getExpr());
            assertEquals(1, _if1.getIfBody().size());
            assertEquals(0, _if1.getElseBody().size());
            assertEquals(Assignment.class, _if1.getIfBody().get(0).getClass());
        }
        {
            Method m3 = mm.get(2);
            assertEquals(3, m3.getBody().size());
            assertEquals(Assignment.class, m3.getBody().get(0).getClass());
            assertEquals(Assignment.class, m3.getBody().get(1).getClass());
            assertEquals(While.class, m3.getBody().get(2).getClass());
            While w = (While) m3.getBody().get(2);
            assertEquals(3, w.getWhileBody().size());
            assertNotNull(w.getExpr());
            assertEquals(If.class, w.getWhileBody().get(0).getClass());
            assertEquals(Assignment.class, w.getWhileBody().get(1).getClass());
            assertEquals(Assignment.class, w.getWhileBody().get(2).getClass());
            If _if1 = (If) w.getWhileBody().get(0);
            assertNull(_if1.getExpr());
            assertEquals(1, _if1.getIfBody().size());
            assertEquals(0, _if1.getElseBody().size());
            assertEquals(Assignment.class, _if1.getIfBody().get(0).getClass());
        }


    }


    private Method load(String file){
        String dir = loadFile("config/methods.csv");
        dir = dir.substring(0, dir.lastIndexOf("/"));
        TemporalInfo.getInstance().loadUserDefined(dir);
        String f = loadFile(file);
        List<ASTClass> cs = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/")));
        ASTClass c = cs.get(0);
        IASTMethod mm = c.getFirstMethodByName("poll");
        Method m = Slice.slice(c).get(mm);
        return m;
    }

    private String loadFile(String s) {
        return TestPathGenerator.class.getClassLoader().getResource(s).getFile();
    }
}
