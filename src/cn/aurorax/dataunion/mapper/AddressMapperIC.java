package cn.aurorax.dataunion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.aurorax.dataunion.model.AddressRecord;

public interface AddressMapperIC {
	List<AddressRecord> getRecords(@Param("offset") int offset, @Param("maxcount") int maxCount);

	int updateUnits(@Param("records") List<AddressRecord> records, @Param("tag") String tag);
	
	void createTempTable();
	
	int insertIntoTemp(@Param("records") List<AddressRecord> records, @Param("tag") String tag);
	
	int updateFromTemp();
}