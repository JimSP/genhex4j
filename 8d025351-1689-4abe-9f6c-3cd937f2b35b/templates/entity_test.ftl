<#import "testValueModule.ftl" as testValues>

package ${packageName}.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ${entityName}EntityTest {

    @Test
    void test${entityName}EntityGettersAndSetters() {
        final ${entityName}Entity entity = new ${entityName}Entity();
        
        <#list jpaDescriptor.attributes as attribute>
        final ${attribute.type} ${attribute.name}Value = <@testValues.generateTestValue attribute.type />;
        
        entity.set${attribute.name?cap_first}(${attribute.name}Value);
        assertEquals(${attribute.name}Value, entity.get${attribute.name?cap_first}());
        </#list>
    }
}
