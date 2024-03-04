package com.kazurayam.ks.testsuitecollection

import java.time.format.DateTimeFormatter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

public class Utils {
	
	static DateTimeFormatter getDateTimeFormatter() {
		return DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	}
	
	static Path getTimestampFile() {
		Path out = Paths.get("./output")
		Files.createDirectories(out)
		return out.resolve("timestamp")
	}
	
	static String getWaitJs() {
		Path waitjs = Paths.get("./src/js/wait_fast.js")
		return Files.readAllLines(waitjs).join("\n")
	}
	
}
