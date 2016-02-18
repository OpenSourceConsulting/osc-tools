/**
 * 
 */
package kr.osci.ide.codegen.generator;

import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.util.JavaConventionsUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.corext.util.Resources;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public abstract class JavaValidator {

	
	public static IStatus validateJavaTypeName(String typeName, IJavaProject project, IPackageFragment pack) {
		
		StatusInfo status= new StatusInfo();
		IStatus val= null;
		
		if (project == null || !project.exists()) {
			val = JavaConventions.validateJavaTypeName(typeName, JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);
		}else{
			val = JavaConventionsUtil.validateJavaTypeName(typeName, project);
		}
		
		if (val.getSeverity() == IStatus.ERROR) {
			status.setError(Messages.format(NewWizardMessages.NewTypeWizardPage_error_InvalidTypeName, val.getMessage()));
			return status;
		} else if (val.getSeverity() == IStatus.WARNING) {
			status.setWarning(Messages.format(NewWizardMessages.NewTypeWizardPage_warning_TypeNameDiscouraged, val.getMessage()));
			// continue checking
		}
		
		if (pack != null) {
			ICompilationUnit cu = pack.getCompilationUnit(typeName + ".java");
			//fCurrType= cu.getType(typeName);
			IResource resource= cu.getResource();

			if (resource.exists()) {
				status.setError(NewWizardMessages.NewTypeWizardPage_error_TypeNameExists);
				return status;
			}
			if (!ResourcesPlugin.getWorkspace().validateFiltered(resource).isOK()) {
				status.setError(NewWizardMessages.NewTypeWizardPage_error_TypeNameFiltered);
				return status;
			}
			URI location= resource.getLocationURI();
			if (location != null) {
				try {
					IFileStore store= EFS.getStore(location);
					if (store.fetchInfo().exists()) {
						status.setError(NewWizardMessages.NewTypeWizardPage_error_TypeNameExistsDifferentCase);
						return status;
					}
				} catch (CoreException e) {
					status.setError(Messages.format(
						NewWizardMessages.NewTypeWizardPage_error_uri_location_unkown,
						BasicElementLabels.getURLPart(Resources.getLocationString(resource))));
				}
			}
		}
		
		return status;
	}

}
