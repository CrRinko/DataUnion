package test;

import cn.aurorax.dataunion.mapper.AddressMapperIC;
import cn.aurorax.dataunion.mapper.AddressMapperKF;
import cn.aurorax.dataunion.task.NormalizeScheduler;

public class Main {

	public static void main(String[] args) {
		NormalizeScheduler scheduler=new NormalizeScheduler("WORK3",AddressMapperIC.class);
		scheduler.start();
		NormalizeScheduler scheduler2=new NormalizeScheduler("WORK3", AddressMapperKF.class);
		scheduler2.start();
	}

}
