package com.qa.TestCases;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.qa.base.BaseClass;
import com.qa.utils.TestUtils;

import io.appium.java_client.screenrecording.CanRecordScreen;

public class BaseTest extends BaseClass {

//	@BeforeSuite
//	public void startAppium(){
//		startAppiumServer();
//	}
//	
//	@AfterSuite
//	public void tearDownAppium(){
//		stopAppiumServer();
//	}
	
	@Parameters({ "platformName", "platformVersion", "deviceName" ,"systemPort","wdaLocalPort"})
	@BeforeMethod
	public void beforeMethod(String platformName, String platformVersion, String deviceName, @Optional("androidOnly")String systemPort, @Optional("iOSOnly")String wdaLocalPort) {
		initiateDriver(platformName, platformVersion, deviceName,systemPort,wdaLocalPort);
		System.out.println(getDriver());
		((CanRecordScreen) getDriver()).startRecordingScreen();
		
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		HashMap<String, String> allParameters = new HashMap<String, String>();
		String media = ((CanRecordScreen)getDriver()).stopRecordingScreen();
		allParameters=(HashMap<String, String>) result.getTestContext().getCurrentXmlTest().getAllParameters();
		String mediaDir = "Media"+File.separator+allParameters.get("platformName")+"_"+allParameters.get("platformVersion")+"_"+allParameters.get("deviceName")
		+File.separator+TestUtils.formattedTimestamp.apply("yyyy-MM-dd-HH-mm-ss")+File.separator
		+ result.getTestClass().getName() + File.separator + result.getName();
		File mediaFile=new File(mediaDir);
		synchronized (mediaFile) {
			if(!mediaFile.exists())
				mediaFile.mkdirs();
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(mediaFile+File.separator+result.getName()+".mp4");
			fos.write(Base64.decodeBase64(media));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tearDownDriver();
	}
	

}
