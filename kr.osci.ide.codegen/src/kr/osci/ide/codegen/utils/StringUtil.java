/**
 * 
 */
package kr.osci.ide.codegen.utils;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public abstract class StringUtil {

	public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	public static String convertMethodName(String str) {
		return convertFirstCharToUpperCase(str);
	}
	
	public static String convertFirstCharToUpperCase(String str){
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static String convertFieldName(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	public static String getClassSimpleName(String classNameWithPackage){
		return classNameWithPackage.substring(classNameWithPackage.lastIndexOf(".")+1);
	}
	
	public static String convertUnderscoreNameToClassName(String name){
		String propertyName = convertUnderscoreNameToPropertyName(name.replaceAll("_tbl", ""));
		
		return convertFirstCharToUpperCase(propertyName);
	}
	
	/**
	 * Convert a column name with underscores to the corresponding property name using "camel case".  A name
	 * like "customer_number" would match a "customerNumber" property name.
	 * @param name the column name to be converted
	 * @return the name using "camel case"
	 * @see SpringFramework JdbcUtils
	 */
	public static String convertUnderscoreNameToPropertyName(String name) {
		StringBuffer result = new StringBuffer();
		boolean nextIsUpper = false;
		if (name != null && name.length() > 0) {
			if (name.length() > 1 && name.substring(1,2).equals("_")) {
				result.append(name.substring(0, 1).toUpperCase());
			}
			else {
				result.append(name.substring(0, 1).toLowerCase());
			}
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals("_")) {
					nextIsUpper = true;
				}
				else {
					if (nextIsUpper) {
						result.append(s.toUpperCase());
						nextIsUpper = false;
					}
					else {
						result.append(s.toLowerCase());
					}
				}
			}
		}
		return result.toString();
	}
}
