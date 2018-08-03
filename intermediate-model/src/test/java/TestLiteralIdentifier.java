import intermediateModel.structure.ASTClass;
import intermediateModel.structure.ASTMethod;
import intermediateModel.structure.expression.ASTIdentifier;
import intermediateModel.structure.expression.ASTLiteral;
import intermediateModel.visitors.DefaultASTVisitor;
import intermediateModel.visitors.creation.JDTVisitor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class TestLiteralIdentifier {


	@Test
	public void bugHiddenClasses() throws Exception {
		//first method
		String f =  TestLiteralIdentifier.class.getClassLoader().getResource("examples/IdentifierLiteral.java").getFile();

		//we have only one class
		ASTClass c = JDTVisitor.parse(f, f.substring(0, f.lastIndexOf("/"))).get(0);
		ASTMethod m = (ASTMethod) c.getMethods().get(0);
		List<String> lit = new ArrayList<>();
		List<String> id = new ArrayList<>();
		m.visit(new DefaultASTVisitor(){
			@Override
			public void enterASTLiteral(ASTLiteral elm) {
				lit.add(elm.getValue());
			}

			@Override
			public void enterASTIdentifier(ASTIdentifier elm) {
				id.add(elm.getValue());
			}
		});
		List<String> expectedLit = Arrays.asList("true","false","10","1","\"dummy test\"", "\" es \"");
		List<String> expectedId = Arrays.asList("var1", "var2", "x", "y", "foo", "System", "foo");
		assertEquals(expectedId, id);
		assertEquals(expectedLit, lit);
	}


}
