package com.qa.Listeners;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.qa.base.BaseClass;
import com.qa.utils.TestUtils;

public class TestListeners implements ITestListener {
	
	@Override
	public void onTestFailure(ITestResult result) {
		HashMap<String, String> testParameters = null;
		testParameters = (HashMap<String, String>) result.getTestContext().getCurrentXmlTest().getAllParameters();
		File srcFile = BaseClass.getDriver().getScreenshotAs(OutputType.FILE);
		if (result.getThrowable() != null) {
			String imagePath = "Screenshots" + File.separator + "Failure" + File.separator
					+ testParameters.get("platformName") + "_" + testParameters.get("platformVersion") + "_"
					+ testParameters.get("deviceName") + File.separator+TestUtils.formattedTimestamp.apply("yyyy-MM-dd-HH-mm-ss")+File.separator
					+ result.getTestClass().getName() + File.separator + result.getName() + ".png";
			try {
				FileUtils.copyFile(srcFile, new File(imagePath));
				String absolutePath=System.getProperty("user.dir")+imagePath;
				Reporter.log("Failure in Test Method:"+result.getName());
				Reporter.log("<a href='"+absolutePath+"'><img src='"+absolutePath+"' height='100' width='100' ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onTestSkipped(ITestResult result) {

	}

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("Test Case is started:"+result.getTestClass().getName()+"_"+result.getName());

	}

	@Override
	public void onTestSuccess(ITestResult result) {

	}

	@Override
	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

}
