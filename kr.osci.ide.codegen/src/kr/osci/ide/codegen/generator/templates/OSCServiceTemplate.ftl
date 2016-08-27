package ${model.servicePackageName};

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(${model.domainName}Service.class);

	@Autowired
	private ${model.dtoSimpleName}${model.suffixDao} ${model.daoFieldName};
	
	public ${model.domainName}Service() {
		// TODO Auto-generated constructor stub
	}
	
	public void save(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.save(${model.domainArgName});
	}
	
	public List<${model.dtoSimpleName}> get${model.domainName}AllList(){
		return ${model.daoFieldName}.findAll();
	}
	
	public Page<${model.domainName}> get${model.domainName}List(Pageable pageable, String search){
	
		/*
		Specifications<${model.domainName}> spec = Specifications.where(${model.domainName}Specs.notBattle()).and(${model.domainName}Specs.notDeteled());
		
		if (search != null) {
			spec = spec.and(${model.domainName}Specs.search(search));
		}
		
		return repository.findAll(spec, pageable);
		*/
		
		return repository.findAll(pageable);
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