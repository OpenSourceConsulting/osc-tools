package ${packageName}.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.ibatis.session.SqlSession;

import com.hhi.hiway.woa.model.GenericCriteria;

import ${packageName}.domain.${domainClass};
import ${packageName}.domain.${domainClass}List;

/**
 * <pre>
 * ${comment}
 * </pre>
 */

@Service
public class ${domainClass}ServiceImpl  implements ${domainClass}Service {

	/**
	 * 모듈별로 Dao 클래스를 만들지 않고 DB 데이터 처리는 MyBatis SqlSession을 바로 사용하도록 합니다.
	 */
	@Autowired
	private SqlSession sqlSession;


	public List<${domainClass}> searchList(String sqlId, Object param) {
		return sqlSession.selectList(sqlId, param);
	}
	
	
	public void update(String sqlId, Object param) {
		sqlSession.update(sqlId, param);
	}
	
	public void insert(String sqlId, Object param) {
		sqlSession.insert(sqlId, param);
	}
	
	public void delete(String sqlId, Object param) {
		sqlSession.delete(sqlId, param);
	}
	
	
	public void updateList(String sqlId, ${domainClass}List param) {
		for (int i = 0; i < param.getResources().size(); i++)
		{ 
			sqlSession.update(sqlId, ((List)param.getResources()).get(i));
		}
	}
	
	public void insertList(String sqlId, ${domainClass}List param) {
		for (int i = 0; i < param.getResources().size(); i++)
		{ 
			sqlSession.insert(sqlId, ((List)param.getResources()).get(i));
		}
	}
	
	public void deleteList(String sqlId, ${domainClass}List param) {
		for (int i = 0; i < param.getResources().size(); i++)
		{ 
			sqlSession.delete(sqlId, ((List)param.getResources()).get(i));
		} 
	}
	
	public Object procedure(String sqlId, ${domainClass} param) {
			Object obj = null;
			
			//try
			//{
				 
				    obj = 	sqlSession.selectOne(sqlId, param);
					
					//if(((${domainClass})obj).getOUT_ERRMSG()  != null )
					//{ 
					//	throw new Exception();
					//} 
			//}
			//catch (Exception e)
			//{
			//	return obj;
			//}
			
			return obj;
		}
	
	public Object procedureList(String sqlId, ${domainClass}List param) {
		Object obj = null;
		
		//try
		//{
			for (int i = 0; i < param.getResources().size(); i++)
			{ 
				  obj = sqlSession.selectOne(sqlId, ((List)param.getResources()).get(i));
				
				//if(((${domainClass})obj).getRTN_MSG() != null )
				//{ 
				//	throw new Exception();
				//}
			}
		//}
		//catch (Exception e)
		//{
		//	return obj;
		//}
		
		return obj;
	}
	
}
