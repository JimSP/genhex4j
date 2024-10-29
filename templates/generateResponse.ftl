<#macro generateResponse entityName statusCode>
    return ResponseEntity.status(HttpStatus.${statusCode}).body(${entityName});
</#macro>