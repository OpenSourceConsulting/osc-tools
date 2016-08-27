package ${model.controllerPackageName};


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/${model.domainName}")
public class ${model.domainName}Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(${model.domainName}Controller.class);
	
	@Autowired
	private ${model.domainName}Service service;
	
	@Autowired
	private MessageSource messageSource;
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public ${model.domainName}Controller() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse allList(GridJsonResponse jsonRes){
	
		List<${model.domainName}> list = service.get${model.domainName}AllList();

		jsonRes.setTotal(list.size());
		jsonRes.setList(list);
		
		return jsonRes;
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getList(SimpleJsonResponse jsonRes, @PageableDefault(sort = { "createDt" }, direction = Direction.DESC) Pageable pageable, String search){
	
		Page<${model.domainName}> list = service.get${model.domainName}List(pageable, search);

		jsonRes.setData(list);
		
		return jsonRes;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse save(SimpleJsonResponse jsonRes, ${model.dtoSimpleName} ${model.domainArgName}){
		
		service.save(${model.domainArgName});
		//jsonRes.setMsg(messageSource.getMessage("account.email.not.reg", new String[]{userEmail}, locale));
		
		
		return jsonRes;
	}
	
	@RequestMapping(value="/{${model.domainArgName}Id}", method = RequestMethod.DELETE)
	@ResponseBody
	public SimpleJsonResponse delete(SimpleJsonResponse jsonRes, @PathVariable("${model.domainArgName}Id") Long ${model.domainArgName}Id){
		
		service.delete${model.domainName}(${model.domainArgName}Id);
		//jsonRes.setMsg("사용자 정보가 정상적으로 삭제되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping(value="/{${model.domainArgName}Id}", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse get${model.domainName}(SimpleJsonResponse jsonRes, , @PathVariable("${model.domainArgName}Id") Long ${model.domainArgName}Id){
	
		jsonRes.setData(service.get${model.domainName}(${model.domainArgName}Id));
		
		return jsonRes;
	}

}
//end of ${model.domainName}Controller.java