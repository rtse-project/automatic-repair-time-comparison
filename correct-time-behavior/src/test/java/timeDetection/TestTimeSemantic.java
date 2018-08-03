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
import slicing.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestTimeSemantic {

    ASTClass c;

    @Before
    public void setUp() throws Exception {
        String dir = load("config/methods.csv");
        dir = dir.substring(0, dir.lastIndexOf("/"));
        TemporalInfo.getInstance().loadUserDefined(dir);
        String f = load("semantic/SemanticTest.java");
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
        int[] lines = {50,51,53,56,61,64,68,69,70,76,80,82};
        assertEquals(l.size(), lines.length);
        for(int i = 0; i < lines.length; i++){
            assertEquals(lines[i], l.get(i).getStm().getLine());
        }
    }

    @Test
    public void TestReducedModel() throws Exception {
        HashMap<IASTMethod, Method> slice = Slice.slice(c);
        assertEquals(slice.size(), 2);
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
        assertEquals(8, wBody.getWhileBody().size());

        assertEquals(If.class, wBody.getWhileBody().get(0).getClass());
        assertEquals(If.class, wBody.getWhileBody().get(1).getClass());
        assertEquals(MethodCall.class, wBody.getWhileBody().get(2).getClass());
        assertEquals(Assignment.class, wBody.getWhileBody().get(3).getClass());
        assertEquals(MethodCall.class, wBody.getWhileBody().get(4).getClass());
        assertEquals(Assignment.class, wBody.getWhileBody().get(5).getClass());
        assertEquals(Expression.class, wBody.getWhileBody().get(6).getClass());
        assertEquals(Stop.class, wBody.getWhileBody().get(7).getClass());

        If first;
        If second;
        first = (If) wBody.getWhileBody().get(0);
        second= (If) wBody.getWhileBody().get(1);
        assertNull(first.getExpr());
        assertNull(second.getExpr());
        assertEquals(1, first.getIfBody().size());
        assertEquals(1, second.getIfBody().size());
        assertEquals(0, first.getElseBody().size());
        assertEquals(0, second.getElseBody().size());
    }

    @Test
    public void TestTimeVariableInMethodCall() throws Exception {
        HashMap<IASTMethod, Method> slice = Slice.slice(c);
        assertEquals(slice.size(), 2);
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
        assertEquals(8, wBody.getWhileBody().size());


        assertEquals(MethodCall.class, wBody.getWhileBody().get(2).getClass());
        assertEquals(MethodCall.class, wBody.getWhileBody().get(4).getClass());

        MethodCall c1 = (MethodCall) wBody.getWhileBody().get(2);
        MethodCall c2 = (MethodCall) wBody.getWhileBody().get(4);

        assertEquals(1, c1.getVariables().size());
        assertEquals(2, c2.getVariables().size());
        assertEquals(new ArrayList<>(Arrays.asList("now")), c1.getVariables());
        assertEquals(new ArrayList<>(Arrays.asList("remaining","now")), c2.getVariables());

    }

    private String load(String s) {
        return TestTimeSemantic.class.getClassLoader().getResource(s).getFile();
    }
}
