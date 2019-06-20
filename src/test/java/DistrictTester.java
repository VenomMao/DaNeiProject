import java.util.List;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.bean.Province;
import cn.tedu.store.mapper.AddressMapper;
import cn.tedu.store.mapper.DistrictMapper;

public class DistrictTester {

	public DistrictTester() {
	}
	@Test
	public void testMapper() {
		AbstractApplicationContext ctx = 
				new ClassPathXmlApplicationContext("spring-dao.xml");
		DistrictMapper mapper = ctx.getBean("districtMapper",DistrictMapper.class);
		//获取省列表
		System.out.println("获取省列表");
		List<Province> provinces = mapper.getProvinces();
		for (Province province : provinces) {
			
			System.out.println(province);
		}
	}

}
