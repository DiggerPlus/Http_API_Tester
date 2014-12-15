package FrameWork;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
/**
 * 创建自定义测试日志
 * @author Stefan.Hou
 *
 */

public class LogListener extends TestListenerAdapter{
	private Logger log = Logger.getLogger(LogListener.class);
	private int m_count = 0;
	
    public void onTestFailure(ITestResult tr) {
    		log.debug("测试名为【"+ tr.getName()+ "】的测试失败！");
    		log.error("测试名为【"+ tr.getName()+ "】的测试失败！");
    		if (++m_count % 10 == 0) {
            	System.out.println("");
            }
    }
	 
 
    public void onTestSkipped(ITestResult tr) {
    		log.debug("测试名为【"+ tr.getName()+ "】的测试忽略！");
    		log.warn("测试名为【"+ tr.getName()+ "】的测试忽略！");
    		if (++m_count % 10 == 0) {
            	System.out.println("");
            }
    }
	 
    public void onTestSuccess(ITestResult tr) {
    		log.info("测试名为【"+ tr.getName()+ "】的测试成功！");
    		log.debug("测试名为【"+ tr.getName()+ "】的测试成功！");
    		if (++m_count % 10 == 0) {
            	System.out.println("");
            }
    }
}
