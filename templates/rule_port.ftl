package ${packageName}.rules;

@FunctionalInterface
public interface ${ruleName}RulePort extends ${javaFunctionalInterface}

<#if javaFunctionalInterface == "java.util.function.Consumer">
    <${ruleInput}>
<#elseif javaFunctionalInterface == "java.util.function.Supplier">
    <${ruleOutput}>
<#elseif javaFunctionalInterface == "java.util.function.Function">
    <${ruleInput}, ${ruleOutput}>
<#elseif javaFunctionalInterface == "java.util.function.BiConsumer">
    <${ruleInput}, ${additionalInput}>
<#elseif javaFunctionalInterface == "java.util.function.BiFunction">
    <${ruleInput}, ${additionalInput}, ${ruleOutput}>
<#else>
    <${ruleInput}, ${ruleOutput}>
</#if> {

}

