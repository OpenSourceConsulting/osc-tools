/**
 * 
 */
package kr.osci.ide.codegen.sql;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public abstract class SQLParser {

	
	public static List<String> getColumns(String query) throws JSQLParserException{
		List<String> columns = new ArrayList<String>();
		CCJSqlParserManager pm = new CCJSqlParserManager();
		
		List<String> params = getParameters(query);
		
		net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(query.replaceAll("#\\{", "").replaceAll("\\}", "")));
		
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			
			ColumnsFinder finder = new ColumnsFinder();
			finder.find(selectStatement);
			
			List<SelectExpressionItem> columnList = finder.getColumnList();
			
			for (SelectExpressionItem selectExpressionItem : columnList) {
				columns.add((selectExpressionItem.getAlias() == null)? ((Column)selectExpressionItem.getExpression()).getColumnName() : selectExpressionItem.getAlias());
			}
		}
		
		
		if(params.size() > 0){
			columns.addAll(params);
		}
		
		
		return columns;
	}
	
	public static List<String> getParameters(String query){
		List<String> columns = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("\\#\\{\\w+\\}");
	    // in case you would like to ignore case sensitivity,
	    // you could use this statement:
	    // Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(query);
	    
	    while(matcher.find()){
	    	columns.add(retriveWord(matcher.group()));
	    }
		
		return columns;
	}
	
	private static String retriveWord(String mathingWord){
		if(mathingWord.length() > 3){
			return mathingWord.substring(2, mathingWord.length() -1);
		}
		
		return mathingWord;
	}
	/*
	public static void main(String[] args) {
		
		System.out.println("update user set username = #{username}, password = #{password}, email = #{email}".replaceAll("#\\{", "").replaceAll("\\}", ""));
	}*/
}
