package com.github.jimsp.genhex4j.configurations;

import java.nio.file.FileSystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.jimfs.Jimfs;

@Configuration
public class JimfsConfiguration {

	@Bean
	public FileSystem inMemoryFileSystem() {
		return Jimfs.newFileSystem(com.google.common.jimfs.Configuration.unix());
	}
}
