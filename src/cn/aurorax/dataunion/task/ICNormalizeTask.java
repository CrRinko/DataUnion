package cn.aurorax.dataunion.task;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.log4j.Logger;

import cn.aurorax.dataunion.conf.DefaultConfiguration;
import cn.aurorax.dataunion.mapper.AddressMapperIC;
import cn.aurorax.dataunion.model.AddressRecord;
import cn.aurorax.dataunion.utils.Normalizer;

public class ICNormalizeTask extends NormalizeTask {
	private Logger logger=Logger.getLogger(getClass());
	private int offset;
	private int maxCount;
	private int commitCount = DefaultConfiguration.COMMIT_COUNT;
	private SqlSessionFactory sqlSessionFactory;
	private Normalizer normalizer;
	private final String TAG;

	public ICNormalizeTask(int id, SqlSessionFactory factory, String tag, int offset, int maxCount) {
		super(id);
		this.sqlSessionFactory = factory;
		this.TAG = tag;
		this.offset = offset;
		this.maxCount = maxCount;
		if (commitCount > maxCount) {
			commitCount = maxCount;
		}
	}

	public ICNormalizeTask(int id, SqlSessionFactory factory, String tag, int offset, int maxCount, int commitCount) {
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
			AddressMapperIC addressMapperIC = session.getMapper(AddressMapperIC.class);
			int curIndex = offset;
			while (curIndex < offset + maxCount) {
				int remainCount = offset + maxCount - curIndex;
//				DUBUG
				long stime=System.currentTimeMillis();
				List<AddressRecord> list = addressMapperIC.getRecords(curIndex,
						commitCount < remainCount ? commitCount : remainCount);
				long etime=System.currentTimeMillis();
				logger.info("Query: "+(etime-stime)+"ms");
				if (list.size() == 0) {
					if (listener != null)
						listener.onCompleted(getID());
					break;
				}
//				DEBUG
				stime=System.currentTimeMillis();
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
				etime=System.currentTimeMillis();
				logger.info("Normailze: "+(etime-stime)+"ms");
//				DEBUG
				stime=System.currentTimeMillis();
				addressMapperIC.updateUnits(list, TAG);
				session.commit();
				etime=System.currentTimeMillis();
				logger.info("Update: "+(etime-stime)+"ms");
				curIndex += list.size();
			}
			session.close();
			if(listener!=null)listener.onFinished(getID());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
