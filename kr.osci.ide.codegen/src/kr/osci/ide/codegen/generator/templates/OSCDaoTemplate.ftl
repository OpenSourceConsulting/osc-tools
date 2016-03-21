package ${model.daoPackageName};

import java.util.List;

import org.springframework.stereotype.Repository;

import ${model.dtoClassName};

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * ${model.domainName}${model.suffixDao}
 *
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Repository
public class ${model.domainName}${model.suffixDao} extends AbstractBaseDao {

	/**
	 * ${model.domainName}${model.suffixDao}
	 *
	 * @param
	 * @exception
	 */
	public ${model.domainName}${model.suffixDao}() {
	}

	public List<${model.dtoSimpleName}> get${model.domainName}List(ExtjsGridParam gridParam){
		return sqlSession.selectList("${model.mapperNamespace}.get${model.domainName}List", gridParam);
	}
	
	public int get${model.domainName}ListTotalCount(ExtjsGridParam gridParam){
		
		return sqlSession.selectOne("${model.mapperNamespace}.get${model.domainName}ListTotalCount", gridParam);
	}
	
	public ${model.dtoSimpleName} get${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		return sqlSession.selectOne("${model.mapperNamespace}.get${model.domainName}", ${model.domainArgName});
	}
	
	public void insert${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		sqlSession.insert("${model.mapperNamespace}.insert${model.domainName}", ${model.domainArgName});
	}
	
	public void update${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		sqlSession.update("${model.mapperNamespace}.update${model.domainName}", ${model.domainArgName});
	}
	
	public void delete${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		sqlSession.delete("${model.mapperNamespace}.delete${model.domainName}", ${model.domainArgName});
	}
}