package FrameWork;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @author Stefan.Hou
 *
 */
public class RetryFailedTest implements IRetryAnalyzer{
	private Logger log = Logger.getLogger(RetryFailedTest.class);
	private final int m_maxRetries = 1;
//retry的间隔时间单位为ms
    private final int m_sleepBetweenRetries = 1000;
    private int currentTry;
    private String previousTest = null;
    private String currentTest = null;
   
    public RetryFailedTest()
    {
        currentTry = 0;
    }
    
	@Override
	public boolean retry(ITestResult result) {
        boolean retStatus = false;               
// 从测试集配置文件中获取失败用例最大重试次数
       String maxRetriesStr = result.getTestContext().getSuite().getParameter("maxRetries");
        int maxRetries = m_maxRetries;
        if(maxRetriesStr != null)
        {
            try{
                maxRetries = Integer.parseInt(maxRetriesStr);
            }
            catch (final NumberFormatException e)
            {
                System.out.println("请确认您的测试配置文件中的最大重试次数的格式是否为一个整型数据！参考错误信息为：" + e.getLocalizedMessage());
            }
        }
       
// 从配置文件中获取测试用例重试时间间隔
        String sleepBetweenRetriesStr = result.getTestContext().getSuite().getParameter("sleepBetweenRetries");
// 如果未配置，默认值为1s
        int sleepBetweenRetries = m_sleepBetweenRetries;
        if(sleepBetweenRetriesStr != null)
        {
            try        
            {
                sleepBetweenRetries = Integer.parseInt(sleepBetweenRetriesStr);
            }
            catch (final NumberFormatException e)
            {
                System.out.println("请确认您的测试配置文件中的重试时间间隔的格式是否为一个整型数据！参考错误信息为：" + e.getLocalizedMessage());
            }
        }
        
        currentTest = result.getTestContext().getCurrentXmlTest().getName();
        
        if (previousTest == null)
        {
            previousTest = currentTest;
        }
        if(!(previousTest.equals(currentTest)))
        {
            currentTry = 0;
        }
       
        if (currentTry < maxRetries && !result.isSuccess())
        {
            try
            {
                Thread.sleep(sleepBetweenRetries);
            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
            currentTry++;  
            result.setStatus(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
            retStatus = true;
            log.debug("测试用例名为【"+currentTest+"】的测试中有执行失败的测试，重新自动执行一次！");
            log.warn("测试用例名为【"+currentTest+"】的测试中有执行失败的测试，重新自动执行一次！");
        }
        else
        {
            currentTry = 0;
        }
        previousTest = currentTest;
        // if this method returns true, it will rerun the test once again.    
        return retStatus;
    }

}
