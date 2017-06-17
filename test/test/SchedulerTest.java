package test;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.aurorax.dataunion.mapper.AddressMapperIC;
import cn.aurorax.dataunion.task.NormalizeScheduler;

public class SchedulerTest {
	private static SqlSessionFactory sqlSessionFactory;

	static {
		try {
			Reader reader = Resources.getResourceAsReader("conf.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SqlSessionFactory getSession() {
		return sqlSessionFactory;
	}

	public static void main(String[] args) {
		NormalizeScheduler scheduler=new NormalizeScheduler("TEST_S",AddressMapperIC.class);
		scheduler.setThreadNum(2);
		scheduler.setTaskCount(1000);
		scheduler.setDebug(true);
		scheduler.start();
	}

}
