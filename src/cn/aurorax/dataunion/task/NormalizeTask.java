package cn.aurorax.dataunion.task;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;

import cn.aurorax.dataunion.conf.DefaultConfiguration;
import cn.aurorax.dataunion.mapper.AddressMapper;
import cn.aurorax.dataunion.model.AddressRecord;
import cn.aurorax.dataunion.utils.Normalizer;

public class NormalizeTask extends Task {
	private int offset;
	private int maxCount;
	private int commitCount = DefaultConfiguration.COMMIT_COUNT;
	private SqlSessionFactory sqlSessionFactory;
	private Normalizer normalizer;
	private final String TAG;
	private Class<? extends AddressMapper> mapperClass;

	public NormalizeTask(int id, SqlSessionFactory factory, String tag, int offset, int maxCount) {
		super(id);
		this.sqlSessionFactory = factory;
		this.TAG = tag;
		this.offset = offset;
		this.maxCount = maxCount;
		if (commitCount > maxCount) {
			commitCount = maxCount;
		}
	}

	public NormalizeTask(int id, SqlSessionFactory factory, String tag, int offset, int maxCount, int commitCount) {
		super(id);
		this.sqlSessionFactory = factory;
		this.offset = offset;
		this.maxCount = maxCount;
		this.commitCount = commitCount;
		this.TAG = tag;
		if (commitCount > maxCount) {
			commitCount = maxCount;
		}
	}

	@Override
	public void run() {
		SqlSession session = SqlSessionManager.newInstance(sqlSessionFactory).openSession(ExecutorType.BATCH);
		normalizer = new Normalizer();
		try {
			AddressMapper addressMapper = session.getMapper(mapperClass);
			int curIndex = offset;
			addressMapper.createTempTable();
			session.commit();
			while (curIndex < offset + maxCount) {
				int remainCount = offset + maxCount - curIndex;
//				DUBUG
//				long stime=System.currentTimeMillis();
				List<AddressRecord> list = addressMapper.getRecords(curIndex,
						commitCount < remainCount ? commitCount : remainCount);
//				long etime=System.currentTimeMillis();
//				logger.info("Query: "+(etime-stime)+"ms");
				if (list.size() == 0) {
					if (listener != null)
						listener.onCompleted(getID());
					session.close();
					return;
				}
//				DEBUG
//				stime=System.currentTimeMillis();
				for (AddressRecord record : list) {
					String door = record.getDoor();
					String unit = record.getUnit();
					String building = record.getBuilding();
					if (door != null) {
						door=normalizer.normalize(door);
						record.setDoor(door);
					}
					if (unit != null) {
						unit=normalizer.normalize(unit);
						record.setUnit(unit);
					}
					if (building != null) {
						building=normalizer.normalize(building);
						record.setBuilding(building);
					}
				}
//				etime=System.currentTimeMillis();
//				logger.info("Normailze: "+(etime-stime)+"ms");
//				DEBUG
//				stime=System.currentTimeMillis();
				addressMapper.insertIntoTemp(list, TAG);
				session.commit();
//				etime=System.currentTimeMillis();
//				logger.info("Insert: "+(etime-stime)+"ms");
				curIndex += list.size();
			}
//			DEBUG
//			long stime=System.currentTimeMillis();
			addressMapper.updateFromTemp();
			session.commit();
//			long etime=System.currentTimeMillis();
//			logger.info("Update: "+(etime-stime)+"ms");
			session.close();
			if(listener!=null)listener.onFinished(getID());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMapperClass(Class<? extends AddressMapper> mapperClass) {
		this.mapperClass = mapperClass;
	}
}
