package test;
import java.io.Reader;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import cn.aurorax.dataunion.mapper.AddressMapperIC;
import cn.aurorax.dataunion.task.NormalizeTask;
import cn.aurorax.dataunion.utils.Normalizer;

public class TaskTest {
	private static SqlSessionFactory sqlSessionFactory;
	private static Logger logger=Logger.getLogger(TaskTest.class);
	
	static{
		try{
			Reader reader=Resources.getResourceAsReader("conf.xml");
			sqlSessionFactory=new SqlSessionFactoryBuilder().build(reader);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static SqlSessionFactory getSession(){
		return sqlSessionFactory;
	}
	
	public static void main(String[] args) {
		try{
			NormalizeTask task=new NormalizeTask(0, sqlSessionFactory, "TEST_TIME", 0, 1000,100);
			task.setMapperClass(AddressMapperIC.class);
			task.run();
			Set<Character> set=Normalizer.getCharSet();
			for(Character ch:set){
				logger.error(ch);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
