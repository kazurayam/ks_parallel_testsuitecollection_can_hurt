import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.kazurayam.browserwindowlayout.BrowserWindowLayoutManager
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

assert tcindex != null
assert url != null
println "tcindex: ${tcindex}"
println "url: ${url}"

Path logs = Paths.get("./logs")
Files.createDirectories(logs)
String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_hhmmss").format(LocalDateTime.now())
Path log = logs.resolve("${timestamp}.log")
System.setProperty("webdriver.chrome.logfile", log.toAbsolutePath().toString());
System.setProperty("webdriver.chrome.verboseLogging", "true");

// open a browser window, manage the layout
WebUI.openBrowser('')
WebDriver driver = DriverFactory.getWebDriver()
TilingCellLayoutMetrics layoutMetrics = GlobalVariable.LAYOUT_METRICS;
BrowserWindowLayoutManager.layout(driver, )

// navitate to the target URL, play on it a bit, the close the borwser
WebUI.navigateToUrl(url)
WebUI.verifyElementPresent(findTestObject("Object Repository/katalon.com/footer/img_logo"), 10)
WebUI.delay(3)
WebUI.closeBrowser()