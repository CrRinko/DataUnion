package cn.aurorax.dataunion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.aurorax.dataunion.model.AddressRecord;

public interface AddressMapper {
	List<AddressRecord> getRecords(@Param("offset") int offset, @Param("maxcount") int maxCount);
	
	void createTempTable();
	
	int insertIntoTemp(@Param("records") List<AddressRecord> records, @Param("tag") String tag);
	
	int updateFromTemp();
}
