package ${model.controllerPackageName};

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;

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
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse list(ExtjsGridParam gridParam){
	
		GridJsonResponse jsonRes = new GridJsonResponse();
		jsonRes.setTotal(service.get${model.domainName}ListTotalCount(gridParam));
		jsonRes.setList(service.get${model.domainName}List(gridParam));
		
		return jsonRes;
	}
	
	@RequestMapping(value="/create")
	@ResponseBody
	public SimpleJsonResponse create(SimpleJsonResponse jsonRes, ${model.dtoSimpleName} ${model.domainArgName}){
		
		service.insert${model.domainName}(${model.domainArgName});
		//jsonRes.setMsg("사용자가 정상적으로 생성되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping(value="/update")
	@ResponseBody
	public SimpleJsonResponse update(SimpleJsonResponse jsonRes, ${model.dtoSimpleName} ${model.domainArgName}){
		
		service.update${model.domainName}(${model.domainArgName});
		//jsonRes.setMsg("사용자 정보가 정상적으로 수정되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public SimpleJsonResponse delete(SimpleJsonResponse jsonRes, ${model.dtoSimpleName} ${model.domainArgName}){
		
		service.delete${model.domainName}(${model.domainArgName});
		//jsonRes.setMsg("사용자 정보가 정상적으로 삭제되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping(value="/get${model.domainName}", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse get${model.domainName}(SimpleJsonResponse jsonRes, ${model.dtoSimpleName} ${model.domainArgName}){
	
		jsonRes.setData(service.get${model.domainName}(${model.domainArgName}));
		
		return jsonRes;
	}

}
//end of ${model.domainName}Controller.java