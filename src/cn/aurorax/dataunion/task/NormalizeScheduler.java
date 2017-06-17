package cn.aurorax.dataunion.task;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import cn.aurorax.dataunion.conf.DefaultConfiguration;
import cn.aurorax.dataunion.mapper.AddressMapper;
import cn.aurorax.dataunion.utils.Normalizer;

public class NormalizeScheduler {
	private Logger logger=Logger.getLogger(getClass());
	private int threadNum=DefaultConfiguration.THREAD_NUM;
	private String taskTag;
	private Map<Integer,Task> taskMap;
	private SqlSessionFactory sqlSessionFactory;
	private int taskCount=DefaultConfiguration.TASK_COUNT;
	private int taskIndex=0;
	private int ICOffset=0;
	private boolean debug=false;
	private Class<? extends AddressMapper> mapperClass;
	
	public NormalizeScheduler(String tag, Class<? extends AddressMapper> mapperClass){
		taskMap=new HashMap<Integer,Task>();
		this.taskTag=tag;
		this.mapperClass=mapperClass;
		Normalizer.clear();
		try{
		Reader reader=Resources.getResourceAsReader("conf.xml");
		sqlSessionFactory=new SqlSessionFactoryBuilder().build(reader);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	synchronized private void addTask(){
		NormalizeTask task=new NormalizeTask(taskIndex, sqlSessionFactory, taskTag, ICOffset, taskCount);
		task.setMapperClass(mapperClass);
		task.setTask(new TaskListener() {
			@Override
			public void onFinished(int id) {
				logger.info("Task ID "+id+ "finished");
				taskMap.remove(id);
				if(!debug) addTask();
				else{
					onCompleted(id);
				}
			}
			
			@Override
			public void onCompleted(int id) {
				logger.info("Task ID "+id+ "completed");
				taskMap.remove(id);
				if(taskMap.size()==0){
					Set<Character> charSet=Normalizer.getCharSet();
					for(Character ch:charSet){
						logger.error(ch);
					}
				}
			}
		});
		taskMap.put(taskIndex, task);
		new Thread(task).start();
		logger.info("Task ID "+taskIndex+" created");
		taskIndex++;
		ICOffset+=taskCount;
	}
	
	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public void start(){
		for(int i=0;i<threadNum;i++){
			addTask();
		}
	}
	
	public interface TaskListener{
		void onFinished(int id);
		void onCompleted(int id);
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setMapperClass(Class<? extends AddressMapper> mapperClass) {
		this.mapperClass = mapperClass;
	}

}
