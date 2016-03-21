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
	
	@RequestMapping("/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse list(ExtjsGridParam gridParam){
	
		GridJsonResponse jsonRes = new GridJsonResponse();
		jsonRes.setTotal(service.get${model.domainName}ListTotalCount(gridParam));
		jsonRes.setList(service.get${model.domainName}List(gridParam));
		
		return jsonRes;
	}
	
	@RequestMapping("/create")
	@ResponseBody
	public SimpleJsonResponse create(${model.dtoSimpleName} ${model.domainArgName}){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		try{
			service.insert${model.domainName}(${model.domainArgName});
			jsonRes.setMsg("사용자가 정상적으로 생성되었습니다.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("사용자 생성 중 에러가 발생하였습니다.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public SimpleJsonResponse update(${model.dtoSimpleName} ${model.domainArgName}){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		try{
			service.update${model.domainName}(${model.domainArgName});
			jsonRes.setMsg("사용자 정보가 정상적으로 수정되었습니다.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("사용자 정보 수정 중 에러가 발생하였습니다.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public SimpleJsonResponse delete(${model.dtoSimpleName} ${model.domainArgName}){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		try{
			service.delete${model.domainName}(${model.domainArgName});
			jsonRes.setMsg("사용자 정보가 정상적으로 삭제되었습니다.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("사용자 정보 삭제 중 에러가 발생하였습니다.");
			
			e.printStackTrace();
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/get${model.domainName}", method = RequestMethod.GET)
	@ResponseBody
	public DtoJsonResponse get${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
	
		DtoJsonResponse jsonRes = new DtoJsonResponse();
		
		jsonRes.setData(service.get${model.domainName}(${model.domainArgName}));
		
		return jsonRes;
	}

}
//end of ${model.domainName}Controller.java