package ${model.daoPackageName};

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ${model.dtoClassName};

/**
 * ${model.domainName}${model.suffixDao}
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface ${model.domainName}${model.suffixDao} extends JpaRepository<${model.domainName}, Integer>, JpaSpecificationExecutor<${model.domainName}> {

	
}