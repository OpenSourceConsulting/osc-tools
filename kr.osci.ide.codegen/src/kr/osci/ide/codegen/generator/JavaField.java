package kr.osci.ide.codegen.generator;


/**
 * <pre>
 * Ŭ������ ������Ƽ�� ���� ��Ÿ ������ �����ϴ� ������ ��ü
 * </pre>
 * 
 * @author BongJin Kwon
 * 
 */
public class JavaField {

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
	
	public String getFieldContents(){
		return "private "+ type + " " + name + ";";
	}
	
	public String getNameEx(){
		return "#{" + name + "}";
	}
	
}
