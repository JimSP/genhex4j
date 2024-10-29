<#macro dtoToEntityConverter attributes>
    <#list attributes as attribute>
        entity.set${attribute.name?cap_first}(dto.get${attribute.name?cap_first}());
    </#list>
</#macro>