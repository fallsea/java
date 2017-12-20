package com.fallsea.antisamy;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

/**
 * @Description: 
 * @Copyright: 2017 www.fallsea.com Inc. All rights reserved.
 * @author: fallsea
 * @version 1.0
 * @date: 2017年12月20日 上午9:45:53
 */
public class AntisamyTest {

	public static void main(String[] args) {
		
		String html = "测试<img onerror=\"alert('呵呵 执行了')\" src=\"\" /> <script>alert(1);</script>";
		try {
			//配置安全策略文件
			Policy policy = Policy.getInstance(AntisamyTest.class.getClassLoader().getResource("antisamy-myspace.xml"));
			
			AntiSamy as = new AntiSamy();
			
			CleanResults cr = as.scan(html, policy);
			
			//获取安全的 HTML 输出
			System.out.println(cr.getCleanHTML());
			
			//执行时间（秒）
			System.err.println("执行时间："+cr.getScanTime());
			
		} catch (PolicyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
