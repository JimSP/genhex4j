<#import "/testValueModule.ftl" as testValues>

package ${packageName}.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ${entityName}DomainTest {

    @Test
    void test${entityName}DomainCreation() {
        final ${entityName}Domain domain = ${entityName}Domain
                .builder()
                <#list domainDescriptor.attributes as attribute>
                    .${attribute.name}(testValues.generateTestValue("${attribute.type}"))
                </#list>
                .build();

        <#list domainDescriptor.attributes as attribute>
        assertEquals(
            testValues.generateTestValue("${attribute.type}"), 
            domain.get${attribute.name?cap_first}()
        );
        </#list>
    }
}
