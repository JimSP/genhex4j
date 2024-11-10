<#import "testValueModule.ftl" as testValues>

package ${packageName}.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ${entityName}DTOTest {

    @Test
    void test${entityName}DTOCreation() {
        final ${entityName}DTO dto = ${entityName}DTO
                .builder()
                <#list dtoDescriptor.attributes as attribute>
                    .${attribute.name}(<@testValues.generateTestValue attribute.type />)
                </#list>
                .build();

        <#list dtoDescriptor.attributes as attribute>
        assertEquals(<@testValues.generateTestValue attribute.type />, dto.get${attribute.name?cap_first}());
        </#list>
    }
}
