import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.ApplyHeuristics;
import intermediateModel.visitors.creation.JDTVisitor;
import intermediateModelHelper.envirorment.temporal.structure.Constraint;
import org.junit.Test;

import java.util.List;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class TestIdentificationTime {

	@Test
	public void Test() {
		String file = TestIdentificationTime.class.getResource("semantic/ConcatenatingTime.java").getFile();
		ASTClass c = JDTVisitor.parse(file, file.substring(0, file.lastIndexOf("/"))).get(0);
		List<Constraint> cc = ApplyHeuristics.getConstraintV2(c);
		for(Constraint cnst : cc){
			System.out.println(cnst.getElm().getCode());
		}
	}
}
