package ${packageName}.service;

import java.util.List;
import com.hhi.hiway.woa.model.GenericCriteria;


import ${packageName}.domain.${domainClass};
import ${packageName}.domain.${domainClass}List;

 
/**
 * <pre>
 * ${comment}
 * </pre>
 */

 public interface ${domainClass}Service
{	
	 /**
	  *
	  *
	  * @param gc
	  * @return List
	  */ 
	 List<${domainClass}> searchList(String sqlId, Object param);
	 
	 
	 /**
	  *
	  *
	  * @param gc
	  * @return
	  */ 
	 void delete(String sqlId, Object param);
	 
	 /**
	  *
	  *
	  * @param gc
	  * @return
	  */ 
	 void update(String sqlId, Object param);
	 
	 /**
	  *
	  *
	  * @param gc
	  * @return
	  */ 
	 void insert(String sqlId, Object param);
	 
	 
	 
	 /**
	  *
	  *
	  * @param sqlId,${domainClass}List
	  * @return
	  */ 
	 void deleteList(String sqlId, ${domainClass}List param);
	 
	 /**
	  *
	  *
	  * @param sqlId,${domainClass}List
	  * @return
	  */ 
	 void updateList(String sqlId, ${domainClass}List param);
	 
	 /**
	  *
	  *
	  * @param sqlId,${domainClass}List
	  * @return 
	  */ 
	 void insertList(String sqlId, ${domainClass}List param);
	 
	 
	 
	 
	 /**
	  *
	  *
	  * @param ${domainClass}
	  * @return Object
	  */
	  Object procedure(String sqlId, ${domainClass} param);
	 
	 /**
	  *
	  *
	  * @param ${domainClass}List
	  * @return Object
	  */
	 Object procedureList(String sqlId, ${domainClass}List param);
	 
}
