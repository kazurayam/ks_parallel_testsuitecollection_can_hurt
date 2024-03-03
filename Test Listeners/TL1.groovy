import java.time.Duration
import java.time.LocalDateTime
import java.util.regex.Matcher
import java.util.regex.Pattern

import com.kazurayam.browserwindowlayout.TilingCellLayoutMetrics
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

class TL1 {
	
	private boolean inSuite = false
	private int numOfCells
	private TilingCellLayoutMetrics layoutMetrics
	private Pattern pattern = Pattern.compile("Test Cases/TC([0-9]+)")
	private LocalDateTime testSuiteStartedAt = null
	private LocalDateTime testCaseStartedAt = null
	
	@BeforeTestSuite
	def neforeTestSuite(TestSuiteContext testSuiteContext) {
		inSuite = true
		testSuiteStartedAt = LocalDateTime.now()
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		if (inSuite && GlobalVariable.NUMBER_OF_CELLS != null) {
			numOfCells = GlobalVariable.NUMBER_OF_CELLS
		} else {
			numOfCells = 1
		}
		GlobalVariable.LAYOUT_METRICS = createLayoutMetrics()
		//
		Matcher m = pattern.matcher(testCaseContext.getTestCaseId())
		if (m.matches()) {
			GlobalVariable.TESTCASE_INDEX = Integer.parseInt(m.group(1))
		} else {
			GlobalVariable.TESTCASE_INDEX = 0
		}
		testCaseStartedAt = LocalDateTime.now()
	}

	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		LocalDateTime testCaseFinishedAt = LocalDateTime.now()
		showDurationSeconds(testCaseContext.getTestCaseId(), testCaseStartedAt, testCaseFinishedAt)
	}
	
	
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		LocalDateTime testSuiteFinishedAt = LocalDateTime.now()
		showDurationSeconds(testSuiteContext.getTestSuiteId(), testSuiteStartedAt, testSuiteFinishedAt)
	}
	
	private TilingCellLayoutMetrics createLayoutMetrics() {
		int numOfCells = 1
		if (GlobalVariable.NUMBER_OF_CELLS != null) {
			if (GlobalVariable.NUMBER_OF_CELLS > 0 &&
					GlobalVariable.NUMBER_OF_CELLS <= 8) {
				numOfCells = GlobalVariable.NUMBER_OF_CELLS
			}
		}
		return new TilingCellLayoutMetrics.Builder(numOfCells).build()
	}
	
	private void showDurationSeconds(String id, LocalDateTime start, LocalDateTime finish) {
		Duration duration = Duration.between(start, finish)
		long seconds = duration.getSeconds()
		WebUI.comment("${id} took ${seconds} seconds")
	}

}