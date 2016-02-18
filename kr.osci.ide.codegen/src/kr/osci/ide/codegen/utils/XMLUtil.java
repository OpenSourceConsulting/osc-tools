/**
 * 
 */
package kr.osci.ide.codegen.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.eclipse.core.resources.IFile;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import kr.osci.ide.codegen.generator.SqlStatement;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class XMLUtil {
	
	public static Document parse(String xmlStr) throws Exception{
		InputSource is = new InputSource(new StringReader(xmlStr));
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        document.setXmlStandalone(true);
        
        return document;
	}
	
	public static Document parse(InputStream in) throws Exception{
		return parse(in, "UTF-8");
	}
	
	public static Document parse(InputStream in, String charsetName) throws Exception{
		InputSource is = new InputSource(new InputStreamReader(in, charsetName));
		try{
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			document.setXmlStandalone(true);
	        
	        return document;
	        
		}finally{
			in.close();
		}
	}
	
	public static void addElement(Document document, SqlStatement statement){
		
		Element el = document.createElement(statement.getSqlType());
		
		el.setAttribute(SqlStatement.ATTR_ID, statement.getSqlID());
		el.setAttribute(SqlStatement.ATTR_PARAM_TYPE, statement.getParameterType());
		
		if(statement.getResultType() != null){
			el.setAttribute(SqlStatement.ATTR_RESULT_TYPE, statement.getResultType());
		}
		el.setTextContent("\n"+statement.getStatement());
		
		document.getDocumentElement().appendChild(el);
	}
	
	public static void updateElement(Document document, SqlStatement statement){
		Element el = getElement(document, statement.getSqlID());
		
		el.setAttribute(SqlStatement.ATTR_PARAM_TYPE, statement.getParameterType());
		
		if(statement.getResultType() != null){
			el.setAttribute(SqlStatement.ATTR_RESULT_TYPE, statement.getResultType());
		}
		el.setTextContent(statement.getStatement());
		
	}
	
	public static String toString(Document document, String charsetName) throws Exception {
		
        Writer outxml = new StringWriter();
        serialize(document, outxml, charsetName);
        
        return outxml.toString().replaceAll("&#xd;", "\r");
	}
	
	public static void serialize(Document document, Writer writer, String charsetName) throws IOException{
		
		OutputFormat format = new OutputFormat(document);
        format.setLineWidth(200);
        format.setIndenting(true);
        format.setIndent(2);
        format.setEncoding(charsetName);
        XMLSerializer serializer = new XMLSerializer(writer, format);
        
        try{
        	serializer.serialize(document);
        }finally{
        	if(writer != null){
        		try{
        			writer.close();
        		}catch(IOException e){}
        	}
        }
	}
	
	public static void serialize(Document doc, IFile iFile, String charsetName)throws Exception{
		
		String xmlSource = XMLUtil.toString(doc, charsetName);
		
		InputStream source = new ByteArrayInputStream(xmlSource.getBytes(charsetName));
		
		iFile.setContents(source, true, true, null);
	}
	
	/**
	 * 
	 * <pre>
	 * id 속성이 id 인 Element 가져오기.
	 * xpath example : http://www.zvon.org/xxl/XPathTutorial/General/examples.html
	 * </pre>
	 * @param document
	 * @param id
	 * @return
	 */
	public static Element getElement(Document document, String id){
		Element el = null;
		
		// xpath 생성
        XPath xpath = XPathFactory.newInstance().newXPath();
		
        try{
			el = (Element)xpath.evaluate("//*[@id='"+id+"']", document, XPathConstants.NODE);
			
        }catch(XPathExpressionException e){
        	// ignore
        }
	
		
		return el;
	}
	
	public static boolean hasValueByText(Document doc, String text){

		return existPath(doc, "//value[text()='"+text+"']");
	}
	
	public static boolean existPath(IFile xmlFile, String xpathExpression){
		
		try{
		
			return existPath(parse(xmlFile.getContents()), xpathExpression);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static boolean existPath(Document doc, String xpathExpression){
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		Element el = null;
		try{
			el = (Element)xpath.evaluate(xpathExpression, doc, XPathConstants.NODE);
			
		}catch(XPathExpressionException e){
			throw new RuntimeException(e);
		}
		return el != null;
	}
	
	public static Set<String> getDomainParamTypeSet(Document doc, String exceptionType){
		Map<String, String> paramMap = new HashMap<String, String>();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		NodeList nodeList = null;
		try{
			nodeList = (NodeList)xpath.evaluate("//@parameterType", doc, XPathConstants.NODESET);
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				Attr attr = (Attr)nodeList.item(i);
				
				String paramType = attr.getValue();
				
				if(!paramType.equals(exceptionType)){
					paramMap.put(paramType, "");
				}
				
			}
			
		}catch(XPathExpressionException e){
			throw new RuntimeException(e);
		}
		
		return paramMap.keySet();
	}
	
	/*
	public static void main(String[] args)throws Exception {
		String xmlstr = "<root><value>1234</value><value>abcd</value></root>";
		
		Document doc = XMLUtil.parse(xmlstr);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		Element el = (Element)xpath.evaluate("//value[text()='abcd']", doc, XPathConstants.NODE);
		System.out.println(el.getTagName());
	}*/
}
