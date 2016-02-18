/**
 * 
 */
package kr.osci.ide.codegen.generator;

import java.util.List;

import kr.osci.ide.codegen.utils.PluginUtil;
import kr.osci.ide.codegen.utils.StringUtil;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class CodeModel {

	private String domainName; // for method name
	private String dtoSimpleName; // domainName + suffixDto
	private String domainArgName;
	private String suffixDao;
	private String suffixDto;
	private String daoFieldName;
	private String packageName;
	private String tableName;
	
	private JavaField[] fields;
	private List<JavaField> pks;
	
	private String pagingQuery = "LIMIT #{start}, #{limit}";
	private String searchQuery = "user_name LIKE concat('%',#{search},'%')";
	
	public CodeModel(String packageName, String domainName, String tableName, String suffixDao, String suffixDto) {
		this.packageName = packageName;
		this.domainName = domainName;
		this.dtoSimpleName = domainName + suffixDto;
		this.tableName = tableName;
		this.suffixDao = suffixDao;
		this.suffixDto = suffixDto;
		
		this.domainArgName = StringUtil.convertFieldName(domainName);
		this.daoFieldName = StringUtil.convertFieldName(suffixDao);
	}

	public String getSuffixDao() {
		return suffixDao;
	}

	public void setSuffixDao(String suffixDao) {
		this.suffixDao = suffixDao;
	}

	public String getSuffixDto() {
		return suffixDto;
	}

	public void setSuffixDto(String suffixDto) {
		this.suffixDto = suffixDto;
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
	
	/**
	 * for method name
	 */
	public String getDomainName(){
		
		return domainName;
	}
	
	public String getDomainArgName(){
		
		return domainArgName;
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
	
	public String getDaoFieldName() {
		return daoFieldName;
	}

}
