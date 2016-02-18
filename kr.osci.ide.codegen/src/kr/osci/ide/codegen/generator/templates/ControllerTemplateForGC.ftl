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
	 *  - 필요매개변수는 화면에서_param=[NOTICE_NO=1010,OPEN_YN=Y] 와 같은 형태로 URL과 함께 서버로 전송합니다.
	 *    ex) [GET] http://localhost:8080/SC002M01?_param=[NOTICE_NO=1010,OPEN_YN=Y]
	 *        위와 같이 전송하는 경우 GenericCriteria 객체에  NOTICE_NO=1010 , OPEN_YN=Y 이 key/value 쌍으로 매핑됨.
	 *
	 * @param gc
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}List", method = RequestMethod.GET)
	@ResponseBody
	public ${domainClass}List searchList(GenericCriteria gc) {
		List<${domainClass}> list = Service.searchList("${domainClass}.${sqlID!"SQL_ID"}",gc );
		return new ${domainClass}List(list);
	}

	/**
	 *
	 * 단건 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}insert", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void insert(GenericCriteria gc) {
		Service.insert("${domainClass}.${sqlID!"SQL_ID"}",gc);
	}

	/**
	 *
	 * 단건 수정
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}update", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(GenericCriteria gc){
		Service.update("${domainClass}.${sqlID!"SQL_ID"}",gc);
	}

	/**
	 *
	 * 단건 삭제
	 * @param primaryKey
	 * @return
	 */
	@RequestMapping(value = "/${domainClass}delete", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(GenericCriteria gc) {
		Service.update("${domainClass}.${sqlID!"SQL_ID"}",gc);
	}
 
}
