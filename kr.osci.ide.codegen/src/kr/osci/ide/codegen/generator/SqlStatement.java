/**
 * 
 */
package kr.osci.ide.codegen.generator;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class SqlStatement {
	
	public static final String ATTR_ID = "id";
	public static final String ATTR_PARAM_TYPE = "parameterType";
	public static final String ATTR_RESULT_TYPE = "resultType";

	private String sqlType;
	private String sqlID;
	private String parameterType;
	private String resultType;
	private String statement;
	private boolean newSql;
	private String xmlTagName;
	
	public SqlStatement(boolean newSql) {
		this.newSql = newSql;
	}

	public boolean isNewSql() {
		return newSql;
	}

	public void setNewSql(boolean newSql) {
		this.newSql = newSql;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
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

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getXmlTagName() {
		return xmlTagName;
	}

	public void setXmlTagName(String xmlTagName) {
		this.xmlTagName = xmlTagName;
	}
	
	public String getParamSimpleName(){
		if(parameterType == null){
			return null;
		}
		
		return parameterType.substring(parameterType.lastIndexOf(".")+1);
	}

}
