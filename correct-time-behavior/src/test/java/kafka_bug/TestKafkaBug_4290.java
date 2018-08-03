package kafka_bug;

import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import org.junit.Before;
import org.junit.Test;
import slicing.Slice;
import slicing.TimeElement;
import slicing.TimeStatements;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestKafkaBug_4290 {

    ASTClass c;

    @Before
    public void setUp() throws Exception {
        String dir = load("config/methods.csv");
        dir = dir.substring(0, dir.lastIndexOf("/"));
        TemporalInfo.getInstance().loadUserDefined(dir);
        String f = load("kafka_bug/WorkerCoordinator_issue.java");
        List<ASTClass> cs = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/")));
        c = cs.get(0);
    }

    @Test
    public void RunIt() throws Exception {
        Slice.slice(c);
    }

    @Test
    public void TestListStms() throws Exception {
        Slice.slice(c);
        List<TimeElement> l = TimeStatements.getInstance().getStms();
        assertTrue("There should be some statements", l.size()>0);
        int[] lines = {50,51,53,56,61,64,68,69,70};
        assertEquals(lines.length, l.size());

        for(int i = 0; i < lines.length; i++){
            assertEquals(lines[i], l.get(i).getStm().getLine());
        }

    }



    private String load(String s) {
        return TestKafkaBug_4290.class.getClassLoader().getResource(s).getFile();
    }
}
