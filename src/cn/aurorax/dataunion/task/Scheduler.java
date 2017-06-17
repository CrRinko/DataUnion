package cn.aurorax.dataunion.task;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import cn.aurorax.dataunion.conf.DefaultConfiguration;
import cn.aurorax.dataunion.utils.Normalizer;

public class Scheduler {
	private Logger logger=Logger.getLogger(getClass());
	private int threadNum=DefaultConfiguration.THREAD_NUM;
	private String taskTag;
	private Map<Integer,NormalizeTask> taskMap;
	private SqlSessionFactory sqlSessionFactory;
	private int taskCount=DefaultConfiguration.TASK_COUNT;
	private int taskIndex=0;
	private int ICOffset=0;
	private boolean debug=false;
	
	public Scheduler(String tag){
		taskMap=new HashMap<Integer,NormalizeTask>();
		this.taskTag=tag;
		try{
		Reader reader=Resources.getResourceAsReader("conf.xml");
		sqlSessionFactory=new SqlSessionFactoryBuilder().build(reader);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void addICTask(){
		NormalizeTask task=new ICNormalizeTask(taskIndex, sqlSessionFactory, taskTag, ICOffset, taskCount);
		task.setTask(new TaskListener() {
			
			@Override
			public void onFinished(int id) {
				taskMap.remove(id);
				if(!debug) addICTask();
				else{
					onCompleted(id);
				}
			}
			
			@Override
			public void onCompleted(int id) {
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
			addICTask();
		}
		for(Entry<Integer, NormalizeTask> entry:taskMap.entrySet()){
			new Thread(entry.getValue()).start();
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
}
