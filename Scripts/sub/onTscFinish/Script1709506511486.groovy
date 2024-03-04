import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.kazurayam.ks.testsuitecollection.Utils
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Path file = Utils.getTimestampFile();
List<String> text = Files.readAllLines(file)

DateTimeFormatter format = Utils.getDateTimeFormatter()  
LocalDateTime tscStartedAt = LocalDateTime.parse(text[0], format)
Duration duration = Duration.between(tscStartedAt, LocalDateTime.now())
long seconds = duration.getSeconds()

WebUI.comment("Tsc took ${seconds} seconds")
