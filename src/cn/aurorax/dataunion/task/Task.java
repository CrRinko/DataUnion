package cn.aurorax.dataunion.task;

import cn.aurorax.dataunion.task.NormalizeScheduler.TaskListener;

public abstract class Task implements Runnable{
	private final int ID;
	protected TaskListener listener;

	public Task(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

	public void setTask(TaskListener listener) {
		this.listener = listener;
	}
}
