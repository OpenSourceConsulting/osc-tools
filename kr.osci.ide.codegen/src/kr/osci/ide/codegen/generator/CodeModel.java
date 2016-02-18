/**
 * 
 */
package kr.osci.ide.codegen.generator;

import java.util.List;

import kr.osci.ide.codegen.utils.PluginUtil;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class CodeModel {

	private String dtoSimpleName;
	private String clsSuffix;
	private String packageName;
	private String tableName;
	
	private JavaField[] fields;
	private List<JavaField> pks;
	
	private String pagingQuery = "LIMIT #{start}, #{limit}";
	private String searchQuery = "user_name LIKE concat('%',#{search},'%')";
	
	public CodeModel(String packageName, String dtoSimpleName, String tableName) {
		this.packageName = packageName;
		this.dtoSimpleName = dtoSimpleName;
		this.tableName = tableName;
	}

	public String getClsSuffix() {
		return clsSuffix;
	}

	public void setClsSuffix(String clsSuffix) {
		this.clsSuffix = clsSuffix;
	}

	public String getDtoSimpleName() {
		return dtoSimpleName;
	}

	public void setDtoSimpleName(String dtoSimpleName) {
		this.dtoSimpleName = dtoSimpleName;
	}

	public String getDtoPackageName() {
		return packageName + PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_DTO);
	}

	public String getDaoPackageName() {
		return packageName + PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_DAO);
	}
	
	public String getServicePackageName() {
		return packageName + PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_SERVICE);
	}
	
	public String getControllerPackageName() {
		return packageName + PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_CONTROLLER);
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public JavaField[] getFields() {
		return fields;
	}

	public void setFields(JavaField[] fields) {
		this.fields = fields;
	}

	public List<JavaField> getPks() {
		return pks;
	}

	public void setPks(List<JavaField> pks) {
		this.pks = pks;
	}
	
	public String getDtoClassName(){
		return getDtoPackageName() + "." + this.dtoSimpleName;
	}
	
	public String getDomainName(){
		String suffixDto = PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUFFIX_DTO);
		
		if("".equals(suffixDto)){
			return this.dtoSimpleName;
		}
		
		return this.dtoSimpleName.replace(suffixDto, "");
	}

	public String getPagingQuery() {
		return pagingQuery;
	}

	public void setPagingQuery(String pagingQuery) {
		this.pagingQuery = pagingQuery;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}
	
	public String getMapperNamespace(){
		return getDomainName();
	}

}
