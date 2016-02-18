/**
 * 
 */
package kr.osci.ide.codegen.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kr.osci.ide.codegen.generator.Column;
import kr.osci.ide.codegen.generator.JavaField;

/**
 * @author Bong-Jin Kwon
 * 
 */
public abstract class JDBCUtils {
	
	public static String[] getTables(Connection conn) throws Exception {
		
		List<String> tables = new ArrayList<String>();
		
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) {
		  //System.out.println(rs.getString(3));
			tables.add(rs.getString(3));
		}
		
		return tables.toArray(new String[0]);
	}
	
	public static List<JavaField> getColumns(Connection conn, String tableName)throws SQLException{
		
		List<JavaField> fields = new ArrayList<JavaField>();
		
		DatabaseMetaData md = conn.getMetaData();
		
		// 컬럼 메타 정보 조회
		ResultSet columnsRs = md.getColumns(null, null, tableName, null);
		
		while (columnsRs.next()) {
			// ResultSet에 컬럼이 중복되어 들어 오는 경우가 있으므로, 먼저 이미 처리됐는지 여부를 확인하기 위해 프로퍼티
			// 명만 먼저 바인딩.
			String columnName = columnsRs.getString("COLUMN_NAME");
			String propertyName = StringUtil.convertUnderscoreNameToPropertyName(columnName);

			Column column = resolveColumnInfo(columnsRs, columnName);
			String propertyType = JdbcTypeFactory.resolveColumnType(column);

			fields.add(new JavaField(propertyName, propertyType, StringUtil.convertMethodName(propertyName), null, column));
		}
		
		return fields;
	}
	
	public static List<JavaField> getPrimaryKeys(Connection conn, String tableName)throws SQLException{
		
		List<JavaField> fields = new ArrayList<JavaField>();
		
		DatabaseMetaData md = conn.getMetaData();
		
		// 컬럼 메타 정보 조회
		ResultSet priKeys = md.getPrimaryKeys(null, null, tableName);
		
		while (priKeys.next()) {
			// ResultSet에 컬럼이 중복되어 들어 오는 경우가 있으므로, 먼저 이미 처리됐는지 여부를 확인하기 위해 프로퍼티
			// 명만 먼저 바인딩.
			String columnName = priKeys.getString("COLUMN_NAME");
			String propertyName = StringUtil.convertUnderscoreNameToPropertyName(columnName);

			Column column = new Column();
			column.setName(columnName);
			//String propertyType = JdbcTypeFactory.resolveColumnType(column);

			fields.add(new JavaField(propertyName, null, null, null, column));
		}
		
		return fields;
	}
	
	public static Column resolveColumnInfo(ResultSet columnsRs, String columnName) throws SQLException {
		Column column = new Column();

		column.setName(columnName);
		column.setTypeName(columnsRs.getString("TYPE_NAME"));
		column.setJdbcType(columnsRs.getInt("DATA_TYPE"));
		column.setPrecision(columnsRs.getInt("COLUMN_SIZE"));
		column.setScale(columnsRs.getInt("DECIMAL_DIGITS"));
		column.setPosition(columnsRs.getInt("ORDINAL_POSITION"));
		String comments = StringUtil.isNotEmpty(columnsRs.getString("REMARKS")) ? columnsRs.getString("REMARKS") : "";
		column.setComments(comments);

		return column;
	}

	/** Uses JNDI and Datasource (preferred style). */
	public static Connection getJNDIConnection(String jndiName)	throws Exception {

		Context initialContext = new InitialContext();

		DataSource datasource = (DataSource) initialContext.lookup(jndiName);
		Connection result = datasource.getConnection();

		return result;
	}

	/** Uses DriverManager. */
	public static Connection getSimpleConnection(String driverClassName, String dbConnUrl, String user, String password) throws Exception {

		Class.forName(driverClassName).newInstance();

		Connection result = DriverManager.getConnection(dbConnUrl, user, password);

		return result;
	}
	
	public static void close(Connection connection) {
		try {
			if (connection == null || connection.isClosed())
				return;

			connection.close();
		} catch (SQLException e) {
		}
	}
}
