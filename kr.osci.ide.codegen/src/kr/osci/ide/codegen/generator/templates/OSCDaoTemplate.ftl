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
 * BongJin Kwon 			            First Draft.
 */
package ${model.daoPackageName};

import java.util.List;

import org.springframework.stereotype.Repository;

import ${model.dtoClassName};

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * ${model.domainName}${model.clsSuffix}
 *
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Repository
public class ${model.domainName}${model.clsSuffix} extends AbstractBaseDao {

	/**
	 * ${model.domainName}${model.clsSuffix}
	 *
	 * @param
	 * @exception
	 */
	public ${model.domainName}${model.clsSuffix}() {
	}

	public List<${model.dtoSimpleName}> get${model.domainName}List(ExtjsGridParam gridParam){
		return sqlSession.selectList("${model.mapperNamespace}.get${model.domainName}List", gridParam);
	}
	
	public int get${model.domainName}ListTotalCount(ExtjsGridParam gridParam){
		
		return sqlSession.selectOne("${model.mapperNamespace}.get${model.domainName}ListTotalCount", gridParam);
	}
	
	public ${model.dtoSimpleName} get${model.domainName}(${model.dtoSimpleName} param){
		return sqlSession.selectOne("${model.mapperNamespace}.get${model.domainName}", param);
	}
	
	public void insert${model.domainName}(${model.dtoSimpleName} param){
		sqlSession.insert("${model.mapperNamespace}.insert${model.domainName}", param);
	}
	
	public void update${model.domainName}(${model.dtoSimpleName} param){
		sqlSession.update("${model.mapperNamespace}.update${model.domainName}", param);
	}
	
	public void delete${model.domainName}(${model.dtoSimpleName} param){
		sqlSession.delete("${model.mapperNamespace}.delete${model.domainName}", param);
	}
}