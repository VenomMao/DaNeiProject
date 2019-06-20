import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.springframework.util.DigestUtils;

/**
 * 摘要测试
 * @author Administrator
 *
 */
public class DigestTest {
	@Test
	/**
	 * 计算一个文件的摘要（md5）
	 */
	public void testFileDidest() throws Exception{
		String file = "passwd";
		
		//打开文件
		InputStream in = new FileInputStream(file);
		
		String md5 = DigestUtils.md5DigestAsHex(in);
		
		
		
		in.close();
		
		System.out.println(md5);
	}
}
