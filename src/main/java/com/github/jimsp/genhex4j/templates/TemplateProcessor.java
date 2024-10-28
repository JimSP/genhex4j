package com.github.jimsp.genhex4j.templates;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jimsp.genhex4j.descriptors.AttributeDescriptor;
import com.github.jimsp.genhex4j.descriptors.AttributeType;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TemplateProcessor {

	private final Configuration freemarkerConfig;

	public TemplateProcessor(final Configuration freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}

	@SneakyThrows
	public void processTemplates(final EntityDescriptor entityDescriptor) {
		final List<TemplateDescriptor> templates = loadTemplates();

		for (final TemplateDescriptor templateDesc : templates) {
			final String outputFileName = resolveOutputFileName(templateDesc, entityDescriptor);
			final Map<String, Object> dataModel = prepareDataModel(templateDesc, entityDescriptor);
			generateFileFromTemplate(templateDesc.getTemplateName(), dataModel, outputFileName);
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

		final Map<String, Object> dataModel = new HashMap<>();
	    if (templateDesc.getAdditionalData() != null) {
	        dataModel.putAll(templateDesc.getAdditionalData());
	    }
	    
	    dataModel.putAll(entityDescriptorToMap(entityDescriptor));
	    
	    replacePlaceholders(dataModel);
	    
		final AttributeType attributeType = AttributeType.valueOf(templateDesc.getDataModelKey().toUpperCase());
		final List<AttributeDescriptor> attributes = entityDescriptor.getAttributesByKey(attributeType);
		dataModel.put("attributes", attributes);

		if (attributeType == AttributeType.JPA) {
			dataModel.put("tableName", entityDescriptor.getJpaDescriptor().getTableName());
		}

		return dataModel;
	}

	
	
	@SneakyThrows
	public void generateFileFromTemplate(final String templateName, final Map<String, Object> dataModel,
			final String outputFileName) {

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
		}
	}

	private Map<String, Object> entityDescriptorToMap(EntityDescriptor entityDescriptor) {
		final Map<String, Object> map = new HashMap<>();
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

}
