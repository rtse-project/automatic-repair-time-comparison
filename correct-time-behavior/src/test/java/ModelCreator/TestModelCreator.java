package ModelCreator;

import com.microsoft.z3.Context;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import org.junit.Test;
import smt.ModelCreator;
import smt.exception.ModelNotCorrect;
import smt.exception.VarNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by giovanni on 06/07/2017.
 */
public class TestModelCreator {

    String[] names = {"time","min_val","max_val","over_max_val"};
    List<String> std = new ArrayList<>();
    {
        for(String name : names) {
            std.add("(declare-fun " + name + " () Int)");
        }
    }

    @Test
    public void TestInsertStdDef() throws Exception {
        ModelCreator model = new ModelCreator();
        Model m = model.getModel();
        for(FuncDecl f : m.getConstDecls()){
            assertTrue("Definition:" + f.toString(), std.contains(f.toString()));
        }
    }

    @Test
    public void TestMultipleVar() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        assertNotNull(v);

        IntExpr v1 = model.createVariable("bla");
        assertNotNull(v1);

        Model m = model.getModel();
        int found = 0;
        for(FuncDecl f : m.getConstDecls()){
            if(f.toString().equals("(declare-fun bla () Int)"))
                found++;
        }
        assertEquals(1, found);
    }

    @Test
    public void TestInsertVar() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        assertNotNull(v);

        Model m = model.getModel();
        boolean found = false;
        for(FuncDecl f : m.getConstDecls()){
            if(f.toString().equals("(declare-fun bla () Int)"))
                found = true;
        }
        assertTrue("Searching \"bla\"", found);
    }

    @Test
    public void TestGetExistingVar() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        assertNotNull(v);
        assertNotNull(model.getVar("bla"));
        assertEquals(v, model.getVar("bla"));
    }

    @Test(expected = VarNotFoundException.class)
    public void TestGetNotExistingVar() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        assertNotNull(v);
        assertNotNull(model.getVar("blabla"));
    }

    @Test
    public void TestVerify() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        Context c = model.getCtx();
        model.addConstraint( c.mkLe(v, c.mkInt(10)) );
        model.addConstraint( c.mkLe(c.mkInt(1), v) );
        model.verifyVariable(v);
    }
    @Test
    public void TestVerifyName() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        Context c = model.getCtx();
        model.addConstraint( c.mkLe(v, c.mkInt(10)) );
        model.addConstraint( c.mkLe(c.mkInt(1), v) );
        model.verifyVariable("bla");
    }

    @Test(expected = ModelNotCorrect.class)
    public void TestVerifyMin() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        Context c = model.getCtx();
        model.addConstraint( c.mkLe(v, c.mkInt(10)) );
        //model.addConstraint( c.mkLe(c.mkInt(1), v) );
        model.verifyVariable(v);
    }

    @Test(expected = ModelNotCorrect.class)
    public void TestVerifyMax() throws Exception {
        ModelCreator model = new ModelCreator();
        IntExpr v = model.createVariable("bla");
        Context c = model.getCtx();
        //model.addConstraint( c.mkLe(v, c.mkInt(10)) );
        model.addConstraint( c.mkLe(v, c.mkInt(1)) );
        model.verifyVariable(v);
        System.out.println(model.getLastMinModel());
    }


}
