<#macro entityToDtoConverter attributes>
    <#list attributes as attribute>
        dto.set${attribute.name?cap_first}(entity.get${attribute.name?cap_first}());
    </#list>
</#macro>