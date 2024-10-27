package ${packageName}.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
<#if jpaDescriptor?? && jpaDescriptor.tableName??>
@Table(name = "${jpaDescriptor.tableName}")
</#if>
public class ${entityName}Entity {

    <#if jpaDescriptor?? && jpaDescriptor.attributes??>
        <#list jpaDescriptor.attributes as attribute>
        <#if attribute.primaryKey?? && attribute.primaryKey>
        @Id
        <#if attribute.generatedValue?? && attribute.generatedValue>
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        </#if>
        </#if>
        <#if attribute.columnDefinition?? && (attribute.columnDefinition?length > 0)>
        @Column(name = "${attribute.name}", columnDefinition = "${attribute.columnDefinition}")
        <#else>
        @Column(name = "${attribute.name}")
        </#if>
        private ${attribute.type} ${attribute.name};
        </#list>
    </#if>
}
