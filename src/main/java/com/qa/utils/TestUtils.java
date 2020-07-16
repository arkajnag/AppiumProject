package com.qa.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import com.qa.base.BaseClass;

public interface TestUtils {
	
	public static UnaryOperator<String> formattedTimestamp=new UnaryOperator<String>() {
		public String apply(String formatter) {
			return LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter));
		}
	};
	
	
	public static Consumer<String> log=new Consumer<String>(){
		public void accept(String logMsg) {
			String loggingMsg=Thread.currentThread().getName()+":"+BaseClass.getPlatformName()
			+":"+BaseClass.getDeviceName()+":"+Thread.currentThread().getStackTrace()[2].getClassName()+":"+logMsg;
			String loggingPath="Logs"+File.separator+BaseClass.getPlatformName()+"_"+BaseClass.getDeviceName()+File.separator+formattedTimestamp.apply("yyyy-MM-dd-HH-mm-ss");
			File logFile=new File(loggingPath);
			synchronized (logFile) {
				if(!logFile.exists())
					logFile.mkdirs();
			}
			try {
				FileWriter fw=new FileWriter(logFile+File.separator+"log.txt",true);
				PrintWriter pw=new PrintWriter(fw);
				pw.println(loggingMsg);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
