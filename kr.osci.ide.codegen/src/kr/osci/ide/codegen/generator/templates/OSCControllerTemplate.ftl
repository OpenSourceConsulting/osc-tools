package ${model.controllerPackageName};


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private ${model.domainName}Service service;

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
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse save(SimpleJsonResponse jsonRes, ${model.dtoSimpleName} ${model.domainArgName}){
		
		service.save(${model.domainArgName});
		//jsonRes.setMsg(" 정상적으로 생성되었습니다.");
		
		
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