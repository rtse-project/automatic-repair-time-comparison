package daikon.annotation;

import daikon.ComputeAnnotation;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class TestAnnotation {




	@Test
	public void verifyAnnotation() {

		int first = 56;
		int second = 61;
		int third = 70;
		int fourth = 73;
		//first method
		String f =  TestAnnotation.class.getClassLoader().getResource("kafka-bug/WorkerCoordinator_issue.java").getFile();
		WatchingPoints variables = ComputeAnnotation.get(f);
		System.out.println(variables);
		assertEquals(4, variables.size());
		Set<Integer> keys = new HashSet<>();
		keys.add(first);
		keys.add(second);
		keys.add(third);
		keys.add(fourth);
		assertEquals(keys, variables.getLines());

		assertEquals(3, variables.getVarsByLine(first).size());
		assertEquals(3, variables.getVarsByLine(second).size());
		assertEquals(4, variables.getVarsByLine(third).size());
		assertEquals(3, variables.getVarsByLine(fourth).size());


	}


}
