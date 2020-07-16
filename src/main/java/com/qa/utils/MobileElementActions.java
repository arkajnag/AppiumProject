package com.qa.utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.qa.base.BaseClass;

import io.appium.java_client.MobileElement;

public interface MobileElementActions {
	
	public static Consumer<MobileElement> waitForElementToBeVisible=mElement ->BaseClass.getWait().until(ExpectedConditions.visibilityOf(mElement));
	public static BiConsumer<MobileElement,String> sendTextToElement=(mElement, textToEnter)-> {
			waitForElementToBeVisible.accept(mElement);
			mElement.sendKeys(textToEnter);
	};
	public static Consumer<MobileElement> waitForElementToBeClickable=mElement ->BaseClass.getWait().until(ExpectedConditions.elementToBeClickable(mElement));
	public static Consumer<MobileElement> clickElement=mElement->{
			waitForElementToBeClickable.accept(mElement);
			mElement.click();
	};
	
	public static Function<MobileElement,String> getTextFromElement=mElement ->{
			waitForElementToBeVisible.accept(mElement);
			return mElement.getText();
	};

}
