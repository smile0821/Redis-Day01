
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.james.cache.utils.JedisUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.runners.MethodSorters;

/**
* Redis业务测试用例
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRedis {



	@Resource
	private JedisUtils jedis;

	@Test
	public void test() {
		jedis.set("aaaaaa1","xxxxxx");
		System.out.println(jedis.get("aaaaaa1"));
	}
	@Test
	public void test1() {
		Map<String, String> order1 = new HashMap<String, String>();
		order1.put("orderId","1");
		order1.put("money","36.6");
		order1.put("time","2018-01-01");
		jedis.hmset("order:1",order1);
		Map<String, String> order2 = new HashMap<String, String>();
		order2.put("orderId","2");
		order2.put("money","38.6");
		order2.put("time","2018-01-01");
		jedis.hmset("order:2",order2);
		Map<String, String> order3 = new HashMap<String, String>();
		order3.put("orderId","3");
		order3.put("money","39.6");
		order3.put("time","2018-01-01");
		jedis.hmset("order:3",order3);
		System.out.println("order1:"+jedis.hmget("order:1","orderId","money","time")
				+",order2:"+jedis.hmget("order:2","orderId","money","time"));
	}
	@Test
	public void test2() {
		jedis.lpush("user:1:order","order:1","order:2","order:3");
	}
	@Test
	public void test3() {
		test1();
		Map<String, String> order4 = new HashMap<String, String>();
		order4.put("orderId","4");
		order4.put("money","40.6");
		order4.put("time","2018-01-01");
		jedis.hmset("order:4",order4);
		System.out.println("order1:"+jedis.hmget("order:1","orderId","money","time")
				+",order2:"+jedis.hmget("order:2","orderId","money","time")
				+",order3:"+jedis.hmget("order:3","orderId","money","time")
				+",order4:"+jedis.hmget("order:4","orderId","money","time"));
	}
	@Test
	public void test4() {
		test2();
		jedis.lpush("user:1:order","order:4");
	}
	@Test
	public void test5() {
		test3();
		jedis.del("user:1:order");
		test4();
		List<String> orderKeys = jedis.lrange("user:1:order",0,-1);
		for (String orderKey:orderKeys) {
			System.out.println(jedis.hmget(orderKey,"orderId","money","time"));
		}
	}

}