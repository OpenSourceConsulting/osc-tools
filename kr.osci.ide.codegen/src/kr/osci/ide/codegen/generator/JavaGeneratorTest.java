package kr.osci.ide.codegen.generator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;

public class JavaGeneratorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		IPath location = new Path("/javaproject/src/javaproject/User.java");
		IFile javaFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
		
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(javaFile);
		
		

	}

}
