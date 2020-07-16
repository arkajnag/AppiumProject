package com.qa.pages;

import org.openqa.selenium.support.PageFactory;
import com.qa.base.BaseClass;
import com.qa.utils.MobileElementActions;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class LoginPage {
	
	@AndroidFindBy(accessibility="test-Username")
	@iOSXCUITFindBy(xpath="//XCUIElementTypeTextField[@name='test-Username']")
	private MobileElement txt_username;
	
	@AndroidFindBy(accessibility="test-Password")
	@iOSXCUITFindBy(accessibility="test-Password")
	private MobileElement txt_password;
	
	@AndroidFindBy(accessibility="test-LOGIN")
	@iOSXCUITFindBy(accessibility="test-LOGIN")
	private MobileElement button_login;
	
	@AndroidFindBy(accessibility="test-Error message")
	@iOSXCUITFindBy(accessibility="test-Error message")
	private MobileElement txt_InvalidLogginMessage;
	
	
	public LoginPage() {
        PageFactory.initElements(new AppiumFieldDecorator(BaseClass.getDriver()), this);
    }

	
	public void fnc_enterCustomerDetails(String username, String password){
		try{
				MobileElementActions.sendTextToElement.accept(txt_username, username);
				MobileElementActions.sendTextToElement.accept(txt_password, password);
				MobileElementActions.clickElement.accept(button_login);
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	public String fnc_getErrorMessageForInvalidLogin(){
		try{
				if(txt_InvalidLogginMessage.isDisplayed())
					return MobileElementActions.getTextFromElement.apply(txt_InvalidLogginMessage);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
