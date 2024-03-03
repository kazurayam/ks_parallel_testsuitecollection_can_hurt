import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

String url = "https://www.esbnyc.com/"

WebUI.callTestCase(findTestCase("sub/visitUrl"), ["tcindex": GlobalVariable.TESTCASE_INDEX, "url": url])
