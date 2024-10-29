package com.github.jimsp.genhex4j.templates;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jimsp.genhex4j.descriptors.AttributeDescriptor;
import com.github.jimsp.genhex4j.descriptors.AttributeType;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
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


	@SneakyThrows
	public void processTemplates(final EntityDescriptor entityDescriptor) {
		final List<TemplateDescriptor> templates = loadTemplates();

		for (final TemplateDescriptor templateDesc : templates) {
			final String outputFileName = resolveOutputFileName(templateDesc, entityDescriptor);
			final Map<String, Object> dataModel = prepareDataModel(templateDesc, entityDescriptor);
			generateFileFromTemplate(templateDesc.getTemplateName(), outputFileName, dataModel);
		}
	}

	private List<TemplateDescriptor> loadTemplates() {

		try (final FileReader reader = new FileReader("config/template-config.json")) {
			final ObjectMapper objectMapper = new ObjectMapper();
			final TemplateConfig config = objectMapper.readValue(reader, TemplateConfig.class);
			return config.getTemplates();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
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

		return dataModel;
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
