/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon			            First Draft.
 */
package ${model.servicePackageName};

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class ${model.domainName}Service {

	@Autowired
	private ${model.dtoSimpleName} ${model.daoFieldName};
	
	public ${model.domainName}Service() {
		// TODO Auto-generated constructor stub
	}
	
	public void insert${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.insert${model.domainName}(${model.domainArgName});
	}
	
	public List<${model.dtoSimpleName}> get${model.domainName}List(ExtjsGridParam gridParam){
		return ${model.daoFieldName}.get${model.domainName}List(gridParam);
	}
	
	public int get${model.domainName}ListTotalCount(ExtjsGridParam gridParam){
		
		return ${model.daoFieldName}.get${model.domainName}ListTotalCount(gridParam);
	}
	
	public ${model.dtoSimpleName} get${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		return ${model.daoFieldName}.get${model.domainName}(${model.domainArgName});
	}
	
	public void update${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.update${model.domainName}(${model.domainArgName});
	}
	
	public void delete${model.domainName}(${model.dtoSimpleName} ${model.domainArgName}){
		${model.daoFieldName}.delete${model.domainName}(${model.domainArgName});
	}

}
//end of ${model.domainName}Service.java