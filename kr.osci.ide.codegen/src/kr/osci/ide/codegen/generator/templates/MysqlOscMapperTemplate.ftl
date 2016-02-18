<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${model.mapperNamespace}">

	<resultMap id="${model.domainName}Map" type="${model.dtoClassName}">
		<!-- <id property="id" column="user_id" /> -->
		<#list model.fields as field>
		<result property="${field.name}" column="${field.column.name}"/>
		</#list>
	</resultMap>
	
	<sql id="get${model.domainName}ListBase" >
        SELECT
        	<#list model.fields as field>
        	${comma2!}${field.column.name}<#assign comma2=",">
        	</#list>
        FROM
        	${model.tableName}
        <where>
			<if test="search != null">
			${model.searchQuery}
			</if>
		</where>
		${model.pagingQuery}
    </sql>
    
    <select id="get${model.domainName}List" parameterType="ExtjsGridParam" resultMap="${model.domainName}Map">
        <include refid="get${model.domainName}ListBase"/>
    </select>
    
    <select id="get${model.domainName}ListTotalCount" parameterType="ExtjsGridParam" resultType="int">
		SELECT COUNT(*)
		FROM (
			<include refid="get${model.domainName}ListBase"/>
		) AS T
	</select>
	
	<select id="get${model.domainName}" parameterType="${model.dtoClassName}" resultMap="${model.domainName}Map">
        SELECT
        	<#list model.fields as field>
        	${comma1!}${field.column.name}<#assign comma1=",">
        	</#list>
        FROM
        	${model.tableName}
        WHERE
        	<#list model.pks as pk>
        	${andd1!}${pk.column.name} = ${pk.nameEx}<#assign andd1 = "AND ">
        	</#list>
    </select>
    
    <insert id="insert${model.domainName}" parameterType="${model.dtoClassName}" >
        INSERT INTO ${model.tableName}
        (
        	<#list model.fields as field>
        	${comma3!}${field.column.name}<#assign comma3=",">
        	</#list>
        ) VALUES (
        	<#list model.fields as field>
        	${comma4!}${field.nameEx}<#assign comma4=",">
        	</#list>
        )
    </insert>
    <update id="update${model.domainName}" parameterType="${model.dtoClassName}" >
        UPDATE ${model.tableName}
        SET
        	<#list model.fields as field>
        	${comma5!}${field.column.name} = ${field.nameEx}<#assign comma5=",">
        	</#list>
        WHERE
        	<#list model.pks as pk>
        	${andd2!}${pk.column.name} = ${pk.nameEx}<#assign andd2 = "AND ">
        	</#list>
    </update>
	<delete id="delete${model.domainName}" parameterType="${model.dtoClassName}" >
        DELETE FROM ${model.tableName}
        WHERE
        	<#list model.pks as pk>
        	${andd3!}${pk.column.name} = ${pk.nameEx}<#assign andd3 = "AND ">
        	</#list>
    </delete>
</mapper>