/**
 * 
 */
package kr.osci.ide.codegen.generator;

import java.util.Map;

import org.eclipse.core.resources.IFile;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 * @see SourceCodeGenerator
 */
public interface ExistFileHandler {
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dataModel
	 * @param iFile
	 * @return 작업결과 (SourceCodeGenerator.RESULT_*)
	 */
	public int process(Map dataModel, IFile iFile);
}
