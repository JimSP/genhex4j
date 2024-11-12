<#macro generateValidations attribute>
    <#if attribute.required>@NotNull</#if>
    <#if attribute.maxLength>@Size(max = ${attribute.maxLength})</#if>
    <#if attribute.minLength>@Size(min = ${attribute.minLength})</#if>
</#macro>
