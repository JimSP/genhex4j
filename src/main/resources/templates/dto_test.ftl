package ${packageName};

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ${entityName}DTOTest {

    @Test
    public void test${entityName}DTOCreation() {
        final ${entityName}DTO dto = ${entityName}DTO
        			.builder()<#list dtoDescriptor.attributes as attribute>
        			.${attribute.name}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if><#if attribute.type == "Integer">1</#if></#if>)</#list>
        			.build();

        <#list dtoDescriptor.attributes as attribute>
        assertEquals(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if><#if attribute.type == "Integer">1</#if></#if>, dto.get${attribute.name?cap_first}());
        </#list>
    }
}
