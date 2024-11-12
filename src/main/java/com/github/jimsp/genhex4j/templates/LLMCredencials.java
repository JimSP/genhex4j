package com.github.jimsp.genhex4j.templates;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LLMCredencials {

	String openAiApiKey;
	String openAiBaseUrl;
	String chatOptionsModel;
	Float chatOptionsTemperature;
}
