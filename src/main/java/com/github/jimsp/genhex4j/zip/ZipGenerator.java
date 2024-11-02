package com.github.jimsp.genhex4j.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.Value;

@Value
@Component
public class ZipGenerator {

    FileSystem inMemoryFileSystem;

    @SneakyThrows
    public byte[] createZip(final Path sourceDir) {
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        		final ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            
            walk(sourceDir).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                try {
                	final ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                    zipOutputStream.putNextEntry(zipEntry);
                    Files.copy(path, zipOutputStream);
                    zipOutputStream.closeEntry();
                } catch (Exception e) {
                    throw new RuntimeException("Error adding file to ZIP: " + path, e);
                }
            });

            zipOutputStream.finish();
            return byteArrayOutputStream.toByteArray();
        }
    }

	private Stream<Path> walk(final Path sourceDir) throws IOException {
		
		return Files.walk(sourceDir);
	}
}

