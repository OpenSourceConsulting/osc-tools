package ${model.servicePackageName};

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
	
	public List<${model.dtoSimpleName}> get${model.domainName}AllList(){
		return ${model.daoFieldName}.findAll();
	}
	
	/*
	public int get${model.domainName}ListTotalCount(GridParam gridParam){
		
		return ${model.daoFieldName}.get${model.domainName}ListTotalCount(gridParam);
	}
	*/
	
	public ${model.dtoSimpleName} get${model.domainName}(Long ${model.domainArgName}Id){
		return ${model.daoFieldName}.findOne(${model.domainArgName}Id);
	}
	
	/*
	public void update${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.update${model.domainName}(${model.domainArgName});
	}
	*/
	
	public void delete${model.domainName}(Long ${model.domainArgName}Id){
		${model.daoFieldName}.delete(${model.domainArgName}Id);
	}

}
//end of ${model.domainName}Service.java