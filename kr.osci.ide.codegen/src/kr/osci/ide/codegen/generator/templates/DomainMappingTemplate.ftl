<?xml version="1.0" encoding="EUC-KR"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.0.xsd
						http://www.springframework.org/schema/beans	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	 
	<util:list id="${userCode}.mapping"> 
		<value>${packageName}.domain.${domainClass}List</value>
		<value>${packageName}.domain.${domainClass}</value>
	</util:list>

</beans>