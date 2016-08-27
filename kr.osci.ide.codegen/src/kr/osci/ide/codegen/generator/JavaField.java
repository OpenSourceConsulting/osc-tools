package kr.osci.ide.codegen.generator;


/**
 * <pre>
 * 클래스의 프로퍼티에 대한 메타 정보를 유지하는 도메인 객체
 * </pre>
 * 
 * @author BongJin Kwon
 * 
 */
public class JavaField {
	
	public static boolean useJPA;

	private String name;
	private String type;
	private String methodName;
	private String xmlTagName;
	private Column column;

	public JavaField(String name, String type, String methodName, String xmlTagName, Column column) {
		this.name = name;
		this.type = type;
		this.methodName = methodName;
		this.xmlTagName = xmlTagName;
		this.column = column;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSignature() {
		return type + " " + name;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getXmlTagName() {
		return xmlTagName;
	}

	public void setXmlTagName(String xmlTagName) {
		this.xmlTagName = xmlTagName;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}
	
	public String getFieldContents(int fieldIndex){
		
		String fc = "private "+ type + " " + name + ";//" + this.column.getComments();
		if(JavaField.useJPA){
			
			if (fieldIndex == 0) {
				fc = "@Id\r\n@GeneratedValue(strategy = GenerationType.AUTO)\r\n" + fc;
			}
			fc = "@Column(name = \""+ this.column.getName() +"\")\r\n" + fc;
		}
		
		return fc;
	}
	
	public String getNameEx(){
		return "#{" + name + "}";
	}
	
}
