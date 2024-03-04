import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime

import com.kazurayam.ks.testsuitecollection.Utils

Path file = Utils.getTimestampFile()
file.toFile().text = Utils.getDateTimeFormatter().format(LocalDateTime.now())
