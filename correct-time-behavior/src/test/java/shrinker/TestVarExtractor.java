package shrinker;

import intermediateModel.interfaces.IASTMethod;
import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import org.junit.Before;
import org.junit.Test;
import slicing.Slice;
import slicing.TimeElement;
import slicing.TimeStatements;
import slicing.model.Method;
import smt.VariableExtractor;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestVarExtractor {

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
            //System.out.println( t.getStm().getCode() + " :: " + t.getType());
        }
    }

    @Test
    public void TestListStms() throws Exception {
        HashMap<IASTMethod, Method> slice = Slice.slice(c);
        assertEquals(slice.size(), 1);
        Method m;
        m = slice.get(c.getFirstMethodByName("poll"));
        assertNotNull(m);
        List<String> vars = VariableExtractor.extract(m);
        System.out.println(vars);
    }


    private String load(String s) {
        return TestVarExtractor.class.getClassLoader().getResource(s).getFile();
    }
}
