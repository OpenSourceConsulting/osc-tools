package ${packageName}.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ${packageName}.domain.${domainClass}; 
import ${packageName}.domain.${domainClass}List; 
import ${packageName}.service.${domainClass}Service; 

import com.hhi.hiway.woa.model.GenericCriteria;

import com.hhi.hiway.exception.BusinessException;
 

/**
 * <pre>
 * ${comment}
 * </pre>
 */

@Controller
@RequestMapping("/${domainClass}")
public class ${domainClass}Controller 
{

	private ${domainClass}Service Service;

	@Autowired
	public void set${domainClass}Service (${domainClass}Service service)
	{
		this.Service = service;
	}
	
	/**
	 *
	 * 지원자 기본정보리스트 조회
	 *
	 *  - xml로 보내서 조회 조건으로 사용 할때 ... 
	 *
	 * @param Body
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}List", method = RequestMethod.POST)
	@ResponseBody
	public ${domainClass}List SearchList(@RequestBody  ${domainClass} paramList) throws Exception
	{ 
		List<${domainClass}> list = Service.searchList("${domainClass}.${sqlID!"SQL_ID"}", paramList);
		return new  ${domainClass}List(list);
	}

	
	/**
	 *
	 * 지원자 기본정보리스트 조회
	 *
	 *  - xml로 보내서 조회 조건으로 사용 할때 ... 
	 *
	 * @param Body
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}List/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public ${domainClass}List SearchList2(@RequestBody  ${domainClass} paramList, @PathVariable("sqlid") String SQLID) throws Exception
	{ 
		List<${domainClass}> list = Service.searchList("${domainClass}." +SQLID, paramList);
		return new  ${domainClass}List(list);
	}
	
	/**
	 *
	 * 지원자 기본정보 수정
	 * 단건 수정
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}update", method = RequestMethod.POST)
	@ResponseBody
	public void update(@RequestBody ${domainClass} paramList ) throws Exception
	{	
 		Service.update("${domainClass}.${sqlID!"SQL_ID"}" , paramList);
		 
	}

	/**
	 *
	 * 지원자 기본정보 수정
	 * 단건 수정
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}update/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public void update2(@RequestBody ${domainClass} paramList , @PathVariable("sqlid") String SQLID) throws Exception
	{	
 		Service.update("${domainClass}." +SQLID, paramList);
		 
	}
	

	/**
	 *
	 *단건 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}insert", method = RequestMethod.POST)
	@ResponseBody
	public void insert(@RequestBody ${domainClass} paramList ) throws Exception
	{	
 		Service.insert("${domainClass}.${sqlID!"SQL_ID"}" , paramList);
		 
	}

	/**
	 *
	 * 단건 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}insert/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public void insert2(@RequestBody ${domainClass} paramList , @PathVariable("sqlid") String SQLID) throws Exception
	{	
 		Service.insert("${domainClass}." +SQLID, paramList);
		 
	}
	
	
	/** 
	 * 단건 삭제
	 */
	 
	@RequestMapping(value = "/${domainClass}delete ", method = RequestMethod.POST)
	@ResponseBody
	public void delete(@RequestBody ${domainClass} paramList) throws Exception
	{	
		  
		Service.delete("${domainClass}.${sqlID!"SQL_ID"}"  , paramList); 
	} 

	/** 
	 * 단건 삭제
	 */
	 
	@RequestMapping(value = "/${domainClass}delete/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public void delete2(@RequestBody ${domainClass} paramList, @PathVariable("sqlid") String SQLID) throws Exception
	{	
		  
		Service.delete("${domainClass}." +SQLID, paramList);
	} 
	
	
	/**
	 * LIST 수정
	 */
	@RequestMapping(value = "/${domainClass}updatelist", method = RequestMethod.POST)
	@ResponseBody
	public void update(@RequestBody ${domainClass}List  gc) throws Exception
	{	
 		Service.updateList("${domainClass}.${sqlID!"SQL_ID"}" , gc);
		 
	}

	/**
	 * LIST 수정
	 */
	@RequestMapping(value = "/${domainClass}updatelist/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public void update2(@RequestBody ${domainClass}List  gc, @PathVariable("sqlid") String SQLID) throws Exception
	{	
 		Service.updateList("${domainClass}." + SQLID , gc);
		 
	}
	
	
	
	/**
	 * LIST 저장
	 */
	@RequestMapping(value = "/${domainClass}insertlist", method = RequestMethod.POST)
	@ResponseBody
	public void insert(@RequestBody ${domainClass}List  gc) throws Exception
	{	
 		Service.insertList("${domainClass}.${sqlID!"SQL_ID"}" , gc);
		 
	}

	/**
	 * LIST 저장
	 */
	@RequestMapping(value = "/${domainClass}insertlist/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public void insert2(@RequestBody ${domainClass}List  gc , @PathVariable("sqlid") String SQLID) throws Exception
	{	
 		Service.insertList("${domainClass}." + SQLID  , gc);
		 
	}
	
	/**
	 * LIST 삭제
	 */
	@RequestMapping(value = "/${domainClass}deletelist ", method = RequestMethod.POST)
	@ResponseBody
	public void delete(@RequestBody ${domainClass}List  gc) throws Exception
	{	
		 
 
		Service.deleteList("${domainClass}.${sqlID!"SQL_ID"}"  , gc); 
	}

	@RequestMapping(value = "/${domainClass}deletelist/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public void delete2(@RequestBody ${domainClass}List  gc , @PathVariable("sqlid") String SQLID) throws Exception
	{	
		 
 
		Service.deleteList("${domainClass}." + SQLID  , gc);
	}
	
	
	
	 /**
	 * procedure 실행
	 */
	@RequestMapping(value = "/${domainClass}procedure", method = RequestMethod.POST)
	@ResponseBody
	public ${domainClass} procedure(@RequestBody ${domainClass}  gc) throws Exception
	{	
		Object obj = Service.procedure("${domainClass}.${domainClass}_SQL" , gc);
		return (${domainClass})obj; 
		 
	}
	


	/**
	 * procedure 실행
	 */
	@RequestMapping(value = "/${domainClass}procedure/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public ${domainClass} procedure2(@RequestBody ${domainClass}  gc , @PathVariable("sqlid") String SQLID) throws Exception
	{	
		Object obj = Service.procedure("${domainClass}."+ SQLID  , gc);
		return (${domainClass})obj; 
		 
	}
	
	/**
	 * procedure 실행
	 */
	@RequestMapping(value = "/${domainClass}procedurelist", method = RequestMethod.POST)
	@ResponseBody
	public ${domainClass} procedureList(@RequestBody ${domainClass}List  gc) throws Exception
	{	
		Object obj = Service.procedureList("${domainClass}.${domainClass}_SQL" , gc);
		return (${domainClass})obj; 
		 
	}
	
	/**
	 * procedure 실행
	 */
	@RequestMapping(value = "/${domainClass}procedurelist/{sqlid}", method = RequestMethod.POST)
	@ResponseBody
	public ${domainClass} procedureList2(@RequestBody ${domainClass}List  gc , @PathVariable("sqlid") String SQLID) throws Exception
	{	
		Object obj = Service.procedureList("${domainClass}." + SQLID, gc);
		return (${domainClass})obj; 
		 
	}
	
}

