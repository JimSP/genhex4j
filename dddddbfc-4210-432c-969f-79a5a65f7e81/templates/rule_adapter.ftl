package ${packageName}.rules;

import org.springframework.stereotype.Component;

@Component
public class ${ruleName}RuleAdapter implements ${ruleName}RulePort {

    @Override
    <#-- Condição para escolher a implementação correta com base na interface funcional -->
    <#if javaFunctionalInterface == "java.util.function.Consumer">
    public void ${javaFuncionalIntefaceMethodName}(final ${ruleInput} input) {
        ${llmGeneratedLogic}
    }
    <#elseif javaFunctionalInterface == "java.util.function.Supplier">
    public ${ruleOutput} ${javaFuncionalIntefaceMethodName}() {
        ${llmGeneratedLogic}
    }
    <#elseif javaFunctionalInterface == "java.util.function.Function">
    public ${ruleOutput} ${javaFuncionalIntefaceMethodName}(final ${ruleInput} input) {
        ${llmGeneratedLogic}
    }
    <#elseif javaFunctionalInterface == "java.util.function.BiConsumer">
    public void ${javaFuncionalIntefaceMethodName}(final ${ruleInput} input, final ${additionalInput} additionalInput) {
        ${llmGeneratedLogic}
    }
    <#elseif javaFunctionalInterface == "java.util.function.BiFunction">
    public ${ruleOutput} ${javaFuncionalIntefaceMethodName}(final ${ruleInput} input, final ${additionalInput} additionalInput) {
        ${llmGeneratedLogic}
    }
    <#else>
    public ${ruleOutput} ${javaFuncionalIntefaceMethodName}(final ${ruleInput} input) {
        ${llmGeneratedLogic}
    }
    </#if>
}
