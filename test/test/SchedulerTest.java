package test;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import cn.aurorax.dataunion.task.Scheduler;

public class SchedulerTest {
	private static SqlSessionFactory sqlSessionFactory;
	private static Logger logger = Logger.getLogger(TaskTest.class);

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
		Scheduler scheduler=new Scheduler("TEST3");
		scheduler.setThreadNum(1);
		scheduler.setTaskCount(1000);
		scheduler.setDebug(true);
		scheduler.start();
	}

}
