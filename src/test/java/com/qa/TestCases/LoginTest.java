package com.qa.TestCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qa.pages.LoginPage;

public class LoginTest extends BaseTest {
	
	private Logger logger=LogManager.getLogger(LoginTest.class.getName());
	LoginPage lgnPage=null;
	
	@BeforeMethod
	public void setUpLoginPageTest(){
		lgnPage=new LoginPage();
	}

	@Test(priority=1)
	public void tc_ValidUserLogin() {
		try {
				lgnPage.fnc_enterCustomerDetails("standard_user", "secret_sauce");
		} catch (Exception w) {
			w.printStackTrace();
			Assert.fail(w.getMessage());
		}
	}

	@Test(priority=2)
	public void tc_InValidUserLogin() {
			lgnPage=new LoginPage();
				lgnPage.fnc_enterCustomerDetails("standard_use", "secret_sauce");
				String actualErrorMessage=lgnPage.fnc_getErrorMessageForInvalidLogin();
				logger.info("Actual Error Message:"+actualErrorMessage);
				Assert.assertEquals(actualErrorMessage, "Username and password do not match any user in this service.");
	}
}
