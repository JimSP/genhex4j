package ${packageName}.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
public class ${entityName}Domain {

<#-- Verifica se domainDescriptor e seus atributos estão definidos -->
<#if domainDescriptor?? && domainDescriptor.attributes?? && (domainDescriptor.attributes?size > 0)>
	<#list domainDescriptor.attributes as attribute>
    ${attribute.type} ${attribute.name};
	</#list>
</#if>
}
