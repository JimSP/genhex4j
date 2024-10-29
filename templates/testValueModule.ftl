<#-- Módulo de geração de valores de teste baseado no tipo do atributo -->
<#macro generateTestValue type>
    <#if type == "Long">1L
    <#elseif type == "String">"Example"
    <#elseif type == "Double">0.0
    <#elseif type == "Integer">1
    <#elseif type == "Boolean">true
    <#else>null</#if>
</#macro>
