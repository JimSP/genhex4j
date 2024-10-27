package ${packageName};

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ${entityName}EntityTest {

    @Test
    public void test${entityName}EntityGettersAndSetters() {
        final ${entityName}Entity entity = new ${entityName}Entity();
        
        <#list jpaDescriptor.attributes as attribute>
        final ${attribute.type} ${attribute.name}Value = <#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Exemplo"</#if><#if attribute.type == "Double">0.0</#if><#if attribute.type == "Integer">1</#if></#if>;
        
        entity.set${attribute.name?cap_first}(${attribute.name}Value);
        assertEquals(${attribute.name}Value, entity.get${attribute.name?cap_first}());
        </#list>
    }
}
