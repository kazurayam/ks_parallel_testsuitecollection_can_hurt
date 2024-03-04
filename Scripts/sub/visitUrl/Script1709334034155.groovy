import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.kazurayam.browserwindowlayout.BrowserWindowLayoutManager
import com.kazurayam.browserwindowlayout.TilingCellLayoutMetrics
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

assert tcindex != null
assert url != null
println "tcindex: ${tcindex}"
println "url: ${url}"

Path logs = Paths.get("./logs")
Files.createDirectories(logs)
String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())
Path log = logs.resolve("${timestamp}.${tcindex}.log")
System.setProperty("webdriver.chrome.logfile", log.toAbsolutePath().toString());
System.setProperty("webdriver.chrome.verboseLogging", "true");

// open a browser window, manage the layout
WebUI.openBrowser('')
WebDriver driver = DriverFactory.getWebDriver()
TilingCellLayoutMetrics layoutMetrics = GlobalVariable.LAYOUT_METRICS;
BrowserWindowLayoutManager.layout(driver,
	layoutMetrics.getCellPosition(tcindex),
	layoutMetrics.getCellDimension(tcindex))

// tune the timeout value of Smart Wait shorter
if (GlobalVariable.FAST_SMARTWAIT == true) {
	Path waitjs = Paths.get("./src/js/wait_fast.js")
	String script = Files.readAllLines(waitjs).join("\n")
	WebUI.executeJavaScript(script, null)
}

// navitate to the target URL, play on it a bit, the close the borwser
WebUI.navigateToUrl(url)

Path output = Paths.get("./output")
Files.createDirectories(output)
Path screenshot = output.resolve("${timestamp}.${tcindex}.png")
WebUI.takeScreenshot(screenshot.toFile().toString())

WebUI.closeBrowser()