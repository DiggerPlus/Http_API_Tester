package FrameWork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestNG;

public class TestRunner {
	private static LogListener logListenner = new LogListener();
	private static RetryTestListener retryListenner = new RetryTestListener();
	private static CustomReporter customReporter = new CustomReporter();
	private static String resuiltOutputPath = System.getProperty("user.dir");
	private static String testConfigFileBasePath = System.getProperty("user.dir")+"/TestConfig/";
	private static Logger log  = Logger.getLogger(TestRunner.class);
	
	
	
	
	private static void setOutputDirectoryName(TestNG runner,String outPutFolderName) throws Exception{
		resuiltOutputPath += "/Test_Report/" + outPutFolderName;
		runner.setOutputDirectory(resuiltOutputPath);
	}
	
	private static void setTestSuites(TestNG ng,String testConfigFileName) {
		List<String> suites = new ArrayList<String>();
		if (testngXMLExists(testConfigFileName))
			suites.add(testConfigFileBasePath+testConfigFileName);
		else {
			log.debug("未创建制定的测试集配置文件，请检查【测试集名为："+testConfigFileName+"】");
			log.error("未创建制定的测试集配置文件，请检查【测试集名为："+testConfigFileName+"】");
			System.exit(0);
		}
		ng.setTestSuites(suites);
	}
	
	private static boolean testngXMLExists(String testConfigFileName) {
		return new File(testConfigFileBasePath+testConfigFileName).exists();
	}
	
	private static void setListener(TestNG runner) {
		runner.addListener(logListenner);
		runner.addListener(retryListenner);
		runner.addListener(customReporter);
	}
	
	public static void main(String[] args) {
		String testConfigFileName = "testng.xml";
		String outPutFolderName = "TestResult";
		TestNG runner = new TestNG();
		try {
			setOutputDirectoryName(runner,outPutFolderName);
		} catch (Exception e) {
			log.debug("设置测试结果报告路径时遇到了意外，错误信息为："+e.getLocalizedMessage());
			log.error("设置测试结果报告路径时遇到了意外，错误信息为："+e.getLocalizedMessage());
		}
		setTestSuites(runner,testConfigFileName);
		setListener(runner);
		runner.run();
	}
}
