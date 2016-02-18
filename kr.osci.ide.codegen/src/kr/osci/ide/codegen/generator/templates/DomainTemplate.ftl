package ${packageName}.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.hhi.hiway.woa.model.ResourceModel;

/**
 * <pre>
 * ${comment}
 * </pre>
 */


@SuppressWarnings("serial")
@XmlRootElement(name = "${domainClass}")
public class ${domainClass} implements ResourceModel
{

	<#if properties?has_content>
	<#list properties as p>
	private String ${p.name};
	</#list>

	<#list properties as p>
	public void set${p.methodName}(String ${p.name}){
		this.${p.name} = ${p.name};
	}

	public String get${p.methodName}(){
		return ${p.name};
	}
	</#list>
	</#if>
	 
}