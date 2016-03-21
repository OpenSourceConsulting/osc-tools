package ${model.servicePackageName};

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class ${model.domainName}Service {

	@Autowired
	private ${model.dtoSimpleName}${model.suffixDao} ${model.daoFieldName};
	
	public ${model.domainName}Service() {
		// TODO Auto-generated constructor stub
	}
	
	public void insert${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.insert${model.domainName}(${model.domainArgName});
	}
	
	public List<${model.dtoSimpleName}> get${model.domainName}List(ExtjsGridParam gridParam){
		return ${model.daoFieldName}.get${model.domainName}List(gridParam);
	}
	
	public int get${model.domainName}ListTotalCount(ExtjsGridParam gridParam){
		
		return ${model.daoFieldName}.get${model.domainName}ListTotalCount(gridParam);
	}
	
	public ${model.dtoSimpleName} get${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		return ${model.daoFieldName}.get${model.domainName}(${model.domainArgName});
	}
	
	public void update${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.update${model.domainName}(${model.domainArgName});
	}
	
	public void delete${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.delete${model.domainName}(${model.domainArgName});
	}

}
//end of ${model.domainName}Service.java