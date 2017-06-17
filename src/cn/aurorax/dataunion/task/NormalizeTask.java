package cn.aurorax.dataunion.task;

import cn.aurorax.dataunion.task.Scheduler.TaskListener;

public abstract class NormalizeTask implements Runnable{
	private final int ID;
	protected TaskListener listener;

	public NormalizeTask(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

	public void setTask(TaskListener listener) {
		this.listener = listener;
	}
}
