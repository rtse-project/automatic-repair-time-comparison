import intermediateModel.visitors.creation.JDTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;
import parser.Java2AST;

import java.util.List;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class TestJar {

	@Test
	public void TestRoot() throws Exception {
		String cache_file = TestJar.class.getClassLoader().getResource("Cache.java").getFile();
		String base_dir = cache_file.substring(0, cache_file.lastIndexOf("/"));
		List<String> jars = Java2AST.getJars(base_dir);
		List<String> class_path = Java2AST.getClassPath(base_dir);
		jars.addAll(class_path);
		Java2AST ast = new Java2AST(cache_file, true, base_dir, jars);
		CompilationUnit result = ast.getContextJDT();
		JDTVisitor v = new JDTVisitor(result, cache_file);
		result.accept(v);
		System.out.println("Ok");

	}

}
