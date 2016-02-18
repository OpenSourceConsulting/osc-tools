package ${packageName}.dao;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Repository;

import ${packageName}.domain.${dataSetClass};
import com.hhi.hiway.woa.dao.mybatis.SqlSessionSupports;

/**
 * <pre>
 * ${comment}
 * </pre>
 */

@Repository
public class ${dataSetClass}Dao extends SqlSessionSupports{
	
	public void update${dataSetClass}(${dataSetClass} dataSet){ 
		
		<#list statements as s>
		sqlSession.${s.sqlType}("${domainClass}.${s.sqlID}", dataSet.get${s.paramSimpleName}() );
		</#list>
		
	}
	
}