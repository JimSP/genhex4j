package com.github.jimsp.genhex4j.session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.github.jimsp.genhex4j.dto.TemplateDTO;

import lombok.SneakyThrows;
import lombok.Value;

@Value
@Service
public class Loader {

	public List<TemplateDTO> execute(){
		
		return list()
				.map(path->TemplateDTO
						.builder()
						.name(path.toString())
						.content(read(path))
						.build())
				.toList();
	}
	
	@SneakyThrows
	private Stream<Path> list() {
		return Files.list(Paths.get("templates"));
	}
	
	@SneakyThrows
	private String read(Path path) {
		return Files.readString(path);
	}
}
