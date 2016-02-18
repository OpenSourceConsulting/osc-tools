package kr.osci.ide.codegen.sql;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class SQLParserTest {

	public static void main(String[] args)throws Exception {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		
		//String sql = "SELECT aa, bb, cc FROM user_tbl where aa = 'a'";
		
		String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "+
		" WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)" ;
		
		sql = "SELECT ANTIQUEOWNERS.OWNERLASTNAME aa, ANTIQUEOWNERS.OWNERFIRSTNAME, (select user from user) bb "+
				"FROM ANTIQUEOWNERS, ANTIQUES "+
				"WHERE ANTIQUES.BUYERID = ANTIQUEOWNERS.OWNERID AND ANTIQUES.ITEM = 'Chair';" ;
		
		net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
		
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			
			
			ColumnsFinder finder = new ColumnsFinder();
			finder.find(selectStatement);
			
			List<SelectExpressionItem> columns = finder.getColumnList();
			
			for (SelectExpressionItem selectExpressionItem : columns) {
				System.out.println((selectExpressionItem.getAlias() == null)? ((Column)selectExpressionItem.getExpression()).getColumnName() : selectExpressionItem.getAlias());
			}
		}

	}

}
