package test;

import cn.aurorax.dataunion.task.Scheduler;

public class Main {

	public static void main(String[] args) {
		Scheduler scheduler=new Scheduler("TEST1");
		scheduler.start();
	}

}
