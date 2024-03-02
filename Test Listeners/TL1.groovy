import com.kazurayam.browserwindowlayout.TilingCellLayoutMetrics
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import internal.GlobalVariable

class TL1 {
	
	private boolean inSuite = false
	private int numOfCells
	private TilingCellLayoutMetrics layoutMetrics
	
	@BeforeTestSuite
	def neforeTestSuite(TestSuiteContext testSuiteContext) {
		//println testSuiteContext.getTestSuiteId()
		inSuite = true
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		//println testCaseContext.getTestCaseId()
		//println testCaseContext.getTestCaseVariables()
		if (inSuite && GlobalVariable.NUMBER_OF_CELLS != null) {
			numOfCells = GlobalVariable.NUMBER_OF_CELLS
		} else {
			numOfCells = 1
		}
		GlobalVariable.CELL_LAYOUT_MANAGER = createLayoutMetrics()
		//
		String tcid = testCaseContext.getTestCaseId()
		GlobalVariable.TESTCASE_INDEX = tcid.substring(tcid.length() - 1) + 0
	}

	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		//println testCaseContext.getTestCaseId()
		//println testCaseContext.getTestCaseStatus()
	} 

	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		//println testSuiteContext.getTestSuiteId()
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
	
}