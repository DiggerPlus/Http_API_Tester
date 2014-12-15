package FrameWork;

import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

public class CustomReporter implements IReporter{

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		for(ISuite suite: suites){
			String suiteName = suite.getName();
			Map<?,?> suiteResults = suite.getResults();
			System.out.println("======================= 汇总  =======================");
			for (Object sr : suiteResults.values()) {
		        ITestContext tc = ((ISuiteResult) sr).getTestContext();
		        System.out.println("测试集名为 【" + suiteName +
		             "】，其中共有:" + tc.getPassedTests().getAllResults().size()+
		             "个测试用例通过！");
		        System.out.println("测试集名为 【" + suiteName +
			             "】，其中共有:" + tc.getFailedTests().getAllResults().size()+
			             "个测试用例失败！");
		        System.out.println("测试集名为 【" + suiteName +
			             "】，其中共有:" + tc.getSkippedTests().getAllResults().size()+
			             "个测试用例忽略！");
		    }
			System.out.println("====================== 汇总结束  ======================");
		}
	}

}
