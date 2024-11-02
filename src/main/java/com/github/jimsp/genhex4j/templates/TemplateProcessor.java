package com.github.jimsp.genhex4j.templates;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jimsp.genhex4j.descriptors.AttributeDescriptor;
import com.github.jimsp.genhex4j.descriptors.AttributeType;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.descriptors.RuleDescriptor;
import com.github.jimsp.genhex4j.random.RandomValueGenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Component
@Slf4j
public class TemplateProcessor {

	Configuration freemarkerConfig;
	OpenAiChatModel openAiChatModel;
	
	public List<TemplateDescriptor> loadTemplates(final Predicate<TemplateDescriptor> filter) {
	    
		try (final FileReader reader = new FileReader("config/template-config.json")) {
	       
			final ObjectMapper objectMapper = new ObjectMapper();
	        final TemplateConfig config = objectMapper.readValue(reader, TemplateConfig.class);
	        
	        return config.getTemplates()
	        		.stream()
	                .filter(filter)
	                .toList();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	@SneakyThrows
	public void processStandardTemplates(final List<TemplateDescriptor> templates, final EntityDescriptor entityDescriptor) {

		for (final TemplateDescriptor templateDesc : templates) {
			
			final String outputFileName = resolveOutputFileName(templateDesc, entityDescriptor);
			final Map<String, Object> dataModel = prepareDataModel(templateDesc, entityDescriptor);
			generateFileFromTemplate(templateDesc.getTemplateName(), outputFileName, dataModel);
		}
	}
	
	@SneakyThrows
	public void processRuleTemplates(final List<TemplateDescriptor> templates, final EntityDescriptor entityDescriptor) {
		
	    for (final TemplateDescriptor templateDesc : templates) {
	        
	    	final List<Map<String, Object>> rulesList = prepareRulesList(templateDesc, entityDescriptor);

	        for (Map<String, Object> ruleData : rulesList) {
	            
	        	final String outputFileName = resolveOutputFileName(templateDesc, entityDescriptor, (String) ruleData.get("ruleName"));
	        	final Map<String, Object> dataModel = prepareDataModel(templateDesc, entityDescriptor, ruleData);
	            generateFileFromTemplate(templateDesc.getTemplateName(), outputFileName, dataModel);
	        }
	    }
	}

	private String resolveOutputFileName(final TemplateDescriptor templateDesc,
			final EntityDescriptor entityDescriptor) {
		
		String outputPath = templateDesc.getOutputPathPattern();
		final Map<String, String> placeholders = new HashMap<>();
		placeholders.put("${packageName}", entityDescriptor.getPackageName().replace('.', '/'));
		placeholders.put("${entityName}", entityDescriptor.getEntityName());

		for (final Map.Entry<String, String> entry : placeholders.entrySet()) {
			outputPath = outputPath.replace(entry.getKey(), entry.getValue());
		}

		return outputPath;
	}
	
	private String resolveOutputFileName(final TemplateDescriptor templateDesc, final EntityDescriptor entityDescriptor, final String ruleName) {
	    
		String outputPath = templateDesc.getOutputPathPattern();

	    final Map<String, String> placeholders = new HashMap<>();
	    placeholders.put("${packageName}", entityDescriptor.getPackageName().replace('.', '/'));
	    placeholders.put("${entityName}", entityDescriptor.getEntityName());
	    placeholders.put("${ruleName}", ruleName);

	    for (final Map.Entry<String, String> entry : placeholders.entrySet()) {
	        outputPath = outputPath.replace(entry.getKey(), entry.getValue());
	    }

	    return outputPath;
	}

	private Map<String, Object> prepareDataModel(final TemplateDescriptor templateDesc,
			final EntityDescriptor entityDescriptor) {
		
		final Map<String, Object> dataModel = entityDescriptorToMap(entityDescriptor);
		
	    if (templateDesc.getAdditionalData() != null) {
	        dataModel.putAll(templateDesc.getAdditionalData());
	    }
	    	    
	    replacePlaceholders(dataModel);
	    
		final AttributeType attributeType = AttributeType.valueOf(templateDesc.getDataModelKey().toUpperCase());
		final List<AttributeDescriptor> attributes = entityDescriptor.getAttributesByKey(attributeType);
		dataModel.put("attributes", attributes);

		if (attributeType == AttributeType.JPA) {
			dataModel.put("tableName", entityDescriptor.getJpaDescriptor().getTableName());
		}

		dataModel.put(null, attributes);

		return dataModel;
	}
	
	private Map<String, Object> prepareDataModel(final TemplateDescriptor templateDesc, final EntityDescriptor entityDescriptor, final Map<String, Object> ruleData) {
	    
		final Map<String, Object> dataModel = entityDescriptorToMap(entityDescriptor);

	    if (templateDesc.getAdditionalData() != null) {
	        dataModel.putAll(templateDesc.getAdditionalData());
	    }

	    dataModel.putAll(ruleData);

	    replacePlaceholders(dataModel);

	    return dataModel;
	}
	
	private List<Map<String, Object>> prepareRulesList(final TemplateDescriptor templateDescriptor, final EntityDescriptor entityDescriptor) {
	    
		final List<Map<String, Object>> rulesList = new ArrayList<>();

	    for (final RuleDescriptor ruleDescriptor : entityDescriptor.getRulesDescriptor()) {
	        Map<String, Object> ruleData = new HashMap<>();
	        ruleData.put("ruleName", ruleDescriptor.getRuleName());
	        ruleData.put("ruleInput", ruleDescriptor.getRuleInput());
	        ruleData.put("ruleOutput", ruleDescriptor.getRuleOutput());
	        ruleData.put("additionalInput", ruleDescriptor.getRuleAdditionalInput());
	        ruleData.put("javaFunctionalInterface", ruleDescriptor.getJavaFunctionalInterface());
	        ruleData.put("javaFuncionalIntefaceMethodName", ruleDescriptor.getJavaFuncionalIntefaceMethodName());
	        ruleData.put("llmGeneratedLogic", generateLLMLogic(templateDescriptor, ruleDescriptor, entityDescriptor.getSystemPrompt()));
	        
	        rulesList.add(ruleData);
	    }

	    return rulesList;
	}

	@SneakyThrows
	private String generateLLMLogic(final TemplateDescriptor templateDescriptor, final RuleDescriptor ruleDescriptor, final String systemPrompt) {
		
		final String contentTemplate = Files.readString(Paths.get("templates/" + templateDescriptor.getTemplateName()));
		
		final SystemMessage systemMessage = new SystemMessage(systemPrompt);
		final UserMessage userMessage = new UserMessage("template:" + contentTemplate + "\n" + "llmGeneratedLogic:" + ruleDescriptor.getLlmGeneratedLogic());
		
		final String block = openAiChatModel.call(systemMessage, userMessage);
		
		final int beginCode = block.lastIndexOf("```java") + 7;
		final int endCode = block.indexOf("```", beginCode);
		
		final String code = beginCode > -1 && endCode > -1 ? block.substring(beginCode, endCode) : defaultCode(ruleDescriptor);
		
		log.info("m=generateLLMLogic, \"code generated: {}\"", code);
		
		return code;
	}

	private String defaultCode(final RuleDescriptor ruleDescriptor) {
		
		return ruleDescriptor.getRuleOutput().trim().toLowerCase().contains("void") ? "return;" : "return null;";
	}

	public void generateFileFromTemplate(final String templateName,
			final String outputFileName, final Map<String, Object> dataModel) {

		final String outputPath = outputFileName.substring(0, outputFileName.lastIndexOf('/'));
		final File directory = new File(outputPath);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		final File outputFile = new File(outputFileName);

		try (final FileWriter writer = new FileWriter(outputFile)) {
			final Template template = freemarkerConfig.getTemplate(templateName);
			template.process(dataModel, writer);
			log.info("create {} with template {}", outputFileName, templateName);
		}catch (Exception e) {
			
			log.error("error create {} with template {}", outputFileName, templateName, e);
			
			throw new RuntimeException(String.format("error create %s with template %s", outputFileName, templateName), e);
		}
	}

	private Map<String, Object> entityDescriptorToMap(EntityDescriptor entityDescriptor) {
		
		final Map<String, Object> map = dataModel();
		
	    map.put("packageName", entityDescriptor.getPackageName());
	    map.put("entityName", entityDescriptor.getEntityName());
	    map.put("domainDescriptor", entityDescriptor.getDomainDescriptor());
	    map.put("dtoDescriptor", entityDescriptor.getDtoDescriptor());
	    map.put("jpaDescriptor", entityDescriptor.getJpaDescriptor());
	    map.put("attributesMap", entityDescriptor.getAttributesMap());
	    map.put("rulesDescriptor", entityDescriptor.getRulesDescriptor());
	    
	    return map;
	}

	private void replacePlaceholders(final Map<String, Object> dataModel) {
		
	    dataModel.replaceAll((key, value) -> {
	        if (value instanceof String strValue) {
	            for (final Map.Entry<String, Object> entry : dataModel.entrySet()) {
	                strValue = strValue.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
	            }
	            return strValue;
	        }
	        return value;
	    });
	}

	private Map<String, Object> dataModel() {
		
		final Map<String, Object> dataModel = Collections.synchronizedMap(new HashMap<>());
		
		dataModel.put("randomLong", RandomValueGenerator.generateRandomLong());
		dataModel.put("randomString", RandomValueGenerator.generateRandomString());
		dataModel.put("randomDouble", RandomValueGenerator.generateRandomDouble());
		dataModel.put("randomInteger", RandomValueGenerator.generateRandomInteger());
		dataModel.put("randomBoolean", RandomValueGenerator.generateRandomBoolean());
		
		return dataModel;
	}
}
