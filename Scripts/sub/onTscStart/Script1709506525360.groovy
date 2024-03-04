import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

Path out = Paths.get("./output")
Files.createDirectories(out)
Path file = out.resolve("timestamp")

file.toFile().text = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())
