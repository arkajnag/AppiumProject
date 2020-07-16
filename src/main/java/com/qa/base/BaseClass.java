package com.qa.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class BaseClass {
	
	private static Logger logger=LogManager.getLogger(BaseClass.class.getName());
	protected static ThreadLocal<AppiumDriver<MobileElement>> driver=new ThreadLocal<AppiumDriver<MobileElement>>();
	protected static ThreadLocal<Properties> prop=new ThreadLocal<Properties>();
	protected static ThreadLocal<WebDriverWait> wait=new ThreadLocal<WebDriverWait>();
	protected static ThreadLocal<String> deviceName=new ThreadLocal<String>();
	protected static ThreadLocal<String> platformName=new ThreadLocal<String>();


	private static AppiumDriverLocalService server;
	
	public static void setDeviceName(String deviceName2){
		deviceName.set(deviceName2);
	}
	
	public static void setPlatformName(String platformName2){
		platformName.set(platformName2);
	}
	
	public static void setDriver(AppiumDriver<MobileElement> driver2) {
		driver.set(driver2);
	}

	public static void setProp(Properties prop2) {
		prop.set(prop2);
	}

	public static void setWait(WebDriverWait wait2) {
		wait.set(wait2);
	}

	public static WebDriverWait getWait() {
		return wait.get();
	}

	public static AppiumDriver<MobileElement> getDriver() {
		return driver.get();
	}

	public static Properties getProp() {
		return prop.get();
	}
	
	public static String getDeviceName(){
		return deviceName.get();
	}
	
	public static String getPlatformName(){
		return platformName.get();
	}
	
	public static AppiumDriverLocalService getServer() {
		HashMap<String,String> environment=new HashMap<String,String>();
		environment.put("PATH", "/Library/Java/JavaVirtualMachines/jdk1.8.0_251.jdk/Contents/Home/bin:/Users/sujatac/Library/Android/sdk/tools:/Users/sujatac/Library/Android/sdk/platform-tools:/usr/local/bin/carthage:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"+System.getenv("PATH"));
		environment.put("ANDROID_HOME", "/Users/sujatac/Library/Android/sdk");
		environment.put("JAVA_HOME", "/Library/Java/JavaVirtualMachines/jdk1.8.0_251.jdk/Contents/Home");
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingPort(4723)
				.usingDriverExecutable(new File("/usr/local/bin/node"))
				.withEnvironment(environment)
				.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js")));
	}
	
	public static void startAppiumServer(){
		try{
				server=getServer();
				if(!server.isRunning()){
					server.start();
					server.clearOutPutStreams();
					TestUtils.log.accept("Server has started");
				}else{
					server.clearOutPutStreams();
					TestUtils.log.accept("Server is already running..");
				}
		}catch(Exception e){
			TestUtils.log.accept("Some Error in starting the Appium Server. Cause:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void stopAppiumServer(){
		try{
				if(server!=null){
					server.stop();
					TestUtils.log.accept("Server has been stopped");
				}
		}catch(Exception e){
			TestUtils.log.accept("Some Error in stopping the Appium Server. Cause:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void initiateDriver(String platformName,String platformVersion,String deviceName,String systemPort,String wdaLocalPort){
		String configfileName="config.properties";
		InputStream is=null;
		String logFilePath="Logs"+File.separator+platformName+"_"+deviceName;
		File logfile=new File(logFilePath);
		synchronized (logfile) {
			if(!logfile.exists()){
				logfile.mkdirs();
			}
		}
		ThreadContext.put("ROUTINGKEY", logFilePath);
		try{
					logger.info("Initializing the Appium Driver to start Execution");
					setProp(new Properties());
					is=new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/"+configfileName);
					getProp().load(is);
					DesiredCapabilities caps=new DesiredCapabilities();
					caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
					caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
					caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
					caps.setCapability("projectName", getProp().getProperty("projectName"));
					switch (platformName.toLowerCase()){
					case "ios":
						caps.setCapability("automationName", getProp().getProperty("iOSAutomationName"));
						caps.setCapability("fullReset", false);
						caps.setCapability("noReset", true);
						caps.setCapability("wdaLocalPort", wdaLocalPort);
						caps.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/src/test/resources/app/SwagLabsMobileApp.app");
						setDriver(new IOSDriver<MobileElement>(new URL(getProp().getProperty("appium")),caps));
						break;
					case "android":
						caps.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/src/test/resources/app/SauceLabs.apk");
						caps.setCapability(MobileCapabilityType.UDID, getProp().getProperty("UDID"));
						caps.setCapability("automationName", getProp().getProperty("AndroidAutomationName"));
						caps.setCapability("appPackage", getProp().getProperty("androidAppPackage"));
						caps.setCapability("appActivity", getProp().getProperty("androidAppActivity"));
						caps.setCapability("systemPort", systemPort);
						setDriver(new AndroidDriver<MobileElement>(new URL(getProp().getProperty("appium")),caps));
						break;
					default:
						throw new Exception("No Such Device is present. Please choose correct Platform.");
				}
				setWait(new WebDriverWait(getDriver(), 10));
				setDeviceName(deviceName);
				setPlatformName(platformName);
				logger.info("Appium Driver initialised to start Execution");
		}catch(Exception e){
			logger.fatal("Failure to start the Appium Server");
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void tearDownDriver(){
		try{
				if(getDriver()!=null){
					getDriver().quit();
				}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void launchApp(){
		try{
				((InteractsWithApps)getDriver()).launchApp();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void closeApp(){
		try{
				((InteractsWithApps)getDriver()).closeApp();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
