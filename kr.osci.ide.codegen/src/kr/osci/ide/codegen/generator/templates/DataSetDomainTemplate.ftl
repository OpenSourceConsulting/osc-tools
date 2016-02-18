package ${packageName}.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre>
 * ${comment}
 * </pre>
 */


@XmlRootElement(name = "${domainClass}")
@XmlAccessorType(XmlAccessType.FIELD)
public class ${domainClass} {
	
	<#if fields?has_content>
	<#list fields as field>
	@XmlElement(name="${field.xmlTagName}")
	private ${field.type} ${field.name};
	</#list>

	<#list fields as field>
	public void set${field.methodName}(${field.type} ${field.name}){
		this.${field.name} = ${field.name};
	}

	public ${field.type} get${field.methodName}(){
		return ${field.name};
	}
	</#list>
	</#if>
	
}