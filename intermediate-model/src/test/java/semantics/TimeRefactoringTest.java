package semantics;

import debugger.Debugger;
import intermediateModel.structure.ASTClass;
import intermediateModel.types.TimeTypeSystem;
import intermediateModel.types.rules.exception.TimeTypeRecommendation;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import intermediateModelHelper.envirorment.temporal.structure.TimeTypes;
import intermediateModelHelper.envirorment.temporalTypes.TemporalTypes;
import intermediateModelHelper.envirorment.temporalTypes.structure.TimeParameterMethod;
import intermediateModelHelper.indexing.IndexingProject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TimeRefactoringTest {

    Debugger d = Debugger.getInstance(false);
    ASTClass c;
    ASTClass cProblem;
    {
        d.setActive(false);
    }

    @Before
    public void setUp() throws Exception {
        String dir = load("config/methods.csv");
        dir = dir.substring(0, dir.lastIndexOf("/"));
        String f = load("timeTypes/TimeTypesRefactor.java");
        List<TimeTypes> rt = IndexingProject.getMethodReturnTime("test", f.substring(0, f.lastIndexOf("/")), false);
        List<TimeParameterMethod> et = IndexingProject.getMethodTimeParameter("test", f.substring(0, f.lastIndexOf("/")), false);
        TemporalInfo.getInstance().loadUserDefined(dir);
        TemporalTypes.getInstance().addRT(rt);
        TemporalTypes.getInstance().addET(et);
        List<ASTClass> cs = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/")));
        c = cs.get(0);

        f = load("timeTypes/SingleCase.java");
        rt = IndexingProject.getMethodReturnTime("test", f.substring(0, f.lastIndexOf("/")), false);
        et = IndexingProject.getMethodTimeParameter("test", f.substring(0, f.lastIndexOf("/")), false);
        TemporalInfo.getInstance().loadUserDefined(dir);
        TemporalTypes.getInstance().addRT(rt);
        TemporalTypes.getInstance().addET(et);
        cs = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/")));
        cProblem = cs.get(0);
    }

    @Test
    public void RunIt() throws Exception {
        TimeTypeSystem u = new TimeTypeSystem();
        u.start(c);
    }

    @Test
    public void TestRecommendation() throws Exception {
        TimeTypeSystem u = new TimeTypeSystem();
        u.start(c);
        List<TimeTypeRecommendation> errors = u.getRecommendation();
        for (TimeTypeRecommendation e : errors) {
            System.out.println(e.getFullMessage());
        }
        assertEquals(0, errors.size());
    }

    @Test
    public void TestRefactoring() throws Exception {
        Debugger.getInstance(false).setActive(false);
        TimeTypeSystem u = new TimeTypeSystem();
        u.start(cProblem);
        List<TimeTypeRecommendation> errors = u.getRecommendation();
        assertEquals(1, errors.size());
        String output = cProblem.refactor(errors);
        assertEquals( readFile(load("timeTypes/SingleCaseCorrect.java")) , output);
    }

    @Test
    public void TestRefactoringFullClass() throws Exception {
        TimeTypeSystem u = new TimeTypeSystem();
        String f = load("timeTypes/TimeTypes.java");
        ASTClass fullClass = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/"))).get(0);
        u.start(fullClass);
        List<TimeTypeRecommendation> errors = u.getRecommendation();
        assertEquals(3, errors.size());
        String output = fullClass.refactor(errors);
        assertEquals( readFile(load("timeTypes/TimeTypesRefactor.java")) , output);
    }

    static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }


    private String load(String s) {
        return TimeRefactoringTest.class.getClassLoader().getResource(s).getFile();
    }
}
