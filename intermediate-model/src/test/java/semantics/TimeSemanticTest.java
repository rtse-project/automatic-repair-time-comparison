package semantics;

import intermediateModel.structure.ASTClass;
import intermediateModel.structure.expression.ASTMethodCall;
import intermediateModel.types.rules.exception.TimeTypeError;
import intermediateModel.types.rules.exception.TimeTypeRecommendation;
import intermediateModel.visitors.ApplyHeuristics;
import intermediateModel.visitors.DefaultASTVisitor;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import intermediateModelHelper.envirorment.temporal.structure.TimeTypes;
import intermediateModelHelper.envirorment.temporalTypes.TemporalTypes;
import intermediateModelHelper.envirorment.temporalTypes.structure.TimeParameterMethod;
import intermediateModelHelper.heuristic.v2.*;
import intermediateModelHelper.indexing.IndexingProject;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import intermediateModel.types.TimeTypeSystem;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TimeSemanticTest {


    class ExpectedResult {
        ASTMethodCall.TimeType t;
        int line;

        public ExpectedResult(ASTMethodCall.TimeType t, int line) {
            this.t = t;
            this.line = line;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExpectedResult that = (ExpectedResult) o;
            return line == that.line &&
                    t == that.t;
        }
    }

    List<ExpectedResult> expectedResults = new ArrayList<>();
    {
        expectedResults.add(new ExpectedResult(ASTMethodCall.TimeType.RT_T, 50));
        expectedResults.add(new ExpectedResult(ASTMethodCall.TimeType.RT_T, 56));
        expectedResults.add(new ExpectedResult(ASTMethodCall.TimeType.ET, 58));
        expectedResults.add(new ExpectedResult(ASTMethodCall.TimeType.RT_T, 61));
        expectedResults.add(new ExpectedResult(ASTMethodCall.TimeType.RT_T, 70));
        expectedResults.add(new ExpectedResult(ASTMethodCall.TimeType.RT_D, 72));
    }

    static ApplyHeuristics ah = new ApplyHeuristics();
    static {
        //ah.set__DEBUG__(true);
        ah.subscribe(MarkTime.class);
        ah.subscribe(TimeInSignature.class);
        ah.subscribe(AssignmentTimeVar.class);
        ah.subscribe(BooleanExpression.class);
        ah.subscribe(MinMaxSearch.class);
        ah.subscribe(ReturnExpression.class);
        ah.subscribe(AddTimeVarToTimeExpression.class);
    }

    ASTClass c;

    @Before
    public void setUp() throws Exception {
        String dir = load("config/methods.csv");
        dir = dir.substring(0, dir.lastIndexOf("/"));
        String f = load("timeTypes/TimeTypes.java");
        List<TimeTypes> rt = IndexingProject.getMethodReturnTime("test", f.substring(0, f.lastIndexOf("/")), false);
        List<TimeParameterMethod> et = IndexingProject.getMethodTimeParameter("test", f.substring(0, f.lastIndexOf("/")), false);
        TemporalInfo.getInstance().loadUserDefined(dir);
        TemporalTypes.getInstance().addRT(rt);
        TemporalTypes.getInstance().addET(et);
        List<ASTClass> cs = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/")));
        c = cs.get(0);
    }

    @Test
    public void RunIt() throws Exception {
        TimeTypeSystem u = new TimeTypeSystem();
        u.start(c);
    }

    @Test
    public void TestListStms() throws Exception {

        ah.analyze(c);
        final int[] count = {0};
        c.visit(new DefaultASTVisitor(){
            @Override
            public void enterASTMethodCall(ASTMethodCall elm) {
                if(expectedResults.contains(new ExpectedResult(elm.getTimeType(), elm.getLine()))){
                    count[0]++;
                }
            }
        });
        assertEquals(expectedResults.size(), count[0]);
    }

    @Test
    public void TestErrors() throws Exception {

        TimeTypeSystem u = new TimeTypeSystem();
        u.start(c);
        List<TimeTypeError> errors = u.getErrors();
        for(TimeTypeError e : errors){
            System.out.println(e.getFullMessage());
        }
        assertEquals(7, errors.size());
    }

    @Test
    public void TestRecommendation() throws Exception {

        TimeTypeSystem u = new TimeTypeSystem();
        u.start(c);
        List<TimeTypeRecommendation> errors = u.getRecommendation();
        assertEquals(3, errors.size());
    }


    private String load(String s) {
        return TimeSemanticTest.class.getClassLoader().getResource(s).getFile();
    }
}
