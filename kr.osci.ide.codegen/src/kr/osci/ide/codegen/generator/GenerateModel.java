/**
 * 
 */
package kr.osci.ide.codegen.generator;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class GenerateModel {

	private IPackageFragmentRoot pkgFragmentRoot;
	private String packageName;
	private String domainClassName;
	private String comment;
	private String sqlID;
	private String parameterType;
	private String userCode;//developer code
	
	public GenerateModel() {
		// TODO Auto-generated constructor stub
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public IPackageFragmentRoot getPkgFragmentRoot() {
		return pkgFragmentRoot;
	}

	public void setPkgFragmentRoot(IPackageFragmentRoot pkgFragmentRoot) {
		this.pkgFragmentRoot = pkgFragmentRoot;
	}
	
	public IPackageFragmentRoot getResPkgFragmentRoot(){
		
		IJavaProject prj = this.pkgFragmentRoot.getJavaProject();
		try{
			return prj.findPackageFragmentRoot(new Path(prj.getPath().makeAbsolute().toString()+ "/src/main/resources"));
		}catch(JavaModelException e){
			return null;
		}
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDomainClassName() {
		return domainClassName;
	}

	public void setDomainClassName(String domainClassName) {
		this.domainClassName = domainClassName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSqlID() {
		return sqlID;
	}

	public void setSqlID(String sqlID) {
		this.sqlID = sqlID;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

}
