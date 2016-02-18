package ${packageName}.domain;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import com.hhi.hiway.woa.model.CollectionWrapper;

/**
 * <pre>
 * ${comment}
 * </pre>
 */


@SuppressWarnings("serial")
@XmlRootElement(name = "${domainClass}List")
public class ${domainClass}List implements CollectionWrapper<${domainClass}>{
    private Collection<${domainClass}> ${domainClassLower}List;   
	public ${domainClass}List() {
		// TODO Auto-generated method stub
	}
    
    public ${domainClass}List(Collection<${domainClass}> ${domainClassLower}List) {
		// TODO Auto-generated method stub
		this.${domainClassLower}List = ${domainClassLower}List;
	}
     
    @XmlElement(name = "${domainClass}")
	public Collection<${domainClass}> getResources() {
		// TODO Auto-generated method stub
		return ${domainClassLower}List;
	}

	public void setResources(Collection<${domainClass}> ${domainClassLower}List) {
		// TODO Auto-generated method stub
		 this.${domainClassLower}List = ${domainClassLower}List;
	} 
}


