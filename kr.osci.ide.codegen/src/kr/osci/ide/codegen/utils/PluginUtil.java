/**
 * 
 */
package kr.osci.ide.codegen.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import kr.osci.ide.codegen.Activator;

/**
 * <pre>
 * Plugin 관련 유틸 함수.
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public abstract class PluginUtil {

	
	public static IFile getIFile(IPackageFragmentRoot packageFragmentRoot, String packageName, String fileName)throws JavaModelException{
		IPackageFragment pack= packageFragmentRoot.getPackageFragment(packageName);
		
		if(!pack.exists()){
			pack = packageFragmentRoot.createPackageFragment(packageName, true, null);
		}
		
        IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(pack.getPath());
        return folder.getFile(fileName);
	}
	
	public static IFile getUserDomainMappingFile(IPackageFragmentRoot packageFragmentRoot, String userCode)throws JavaModelException{
		return getIFile(packageFragmentRoot, "config.mapping", "domain-" + userCode + "-mapping.xml");
	}
	
	public static ICompilationUnit getICompilationUnit(IPackageFragmentRoot packageFragmentRoot, String packageName, String fileName){
		IPackageFragment pack= packageFragmentRoot.getPackageFragment(packageName);
        IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(pack.getPath());
        return JavaCore.createCompilationUnitFrom(folder.getFile(fileName));
	}
	
	/*public static IFile getSpringServletConfigFile(IPackageFragmentRoot packageFragmentRoot){
		packageFragmentRoot.getJavaProject().
	}*/
	
	public static void logError(Throwable e){
		JavaPlugin.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.toString(), e));
	}
	
	public static void logError(String message, Throwable e){
		JavaPlugin.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}
	
	public static void logWarn(Throwable e){
		JavaPlugin.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.toString(), e));
	}
	
	public static void logWarn(String message, Throwable e){
		JavaPlugin.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, message, e));
	}
	
	/**
	 * <pre>
	 * Job 내부에서 MessageDialog.openInformation 보여주기.
	 * </pre>
	 * @param parent
	 * @param title
	 * @param message
	 */
	public static void openInformation(final Shell parent, final String title, final String message){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageDialog.openInformation(parent, title, message);
			}
		});
	}
	
	/**
	 * <pre>
	 * Job 내부에서 MessageDialog.openWarning 보여주기.
	 * </pre>
	 * @param parent
	 * @param title
	 * @param message
	 */
	public static void openWarning(final Shell parent, final String title, final String message){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageDialog.openWarning(parent, title, message);
			}
		});
	}
	
	/**
	 * <pre>
	 * Job 내부에서 MessageDialog.openError 보여주기.
	 * </pre>
	 * @param parent
	 * @param title
	 * @param message
	 */
	public static void openError(final Shell parent, final String title, final String message){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageDialog.openError(parent, title, message);
			}
		});
	}
	
	public static void openEditor(final IFile ifile, final IProgressMonitor monitor){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				try{
					IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), ifile).doSave(monitor);
				}catch(PartInitException e){
					logError(e);
				}
			}
		});
	}
	
	public static String getPreferenceProperty(String name){
		//IPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, Activator.PLUGIN_ID);
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		
		String value = preferenceStore.getString(name);
		
		if(value == null){
			return "";
		}
		return value;
	}

}
