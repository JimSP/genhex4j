package ${packageName};

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ${entityName}DomainTest {

    @Test
    public void test${entityName}DomainCreation() {
        final ${entityName}Domain domain = ${entityName}Domain
		        	.builder()
		            <#list domainDescriptor.attributes as attribute>
		            .${attribute.name}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Exemplo"</#if><#if attribute.type == "Double">0.0</#if></#if>)
		            </#list>
		            .build();

        <#list domainDescriptor.attributes as attribute>
        assertEquals(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Exemplo"</#if><#if attribute.type == "Double">0.0</#if></#if>, domain.get${attribute.name?cap_first}());
        </#list>
    }
}
