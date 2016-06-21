package com.waseemh.sherlock.exec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.waseemh.sherlock.configuration.Configuration;

public class ComparisonTaskManager {

	private Queue<ComparisonTask> tasksQueue = new ConcurrentLinkedQueue<ComparisonTask>();
	private Configuration configuration;

	public ComparisonTaskManager(Configuration configuration) {
		this.configuration = configuration;
	}

	public void addTask(ComparisonTask task) {
		tasksQueue.add(task);
	}

	public void removeTask(ComparisonTask task) {
		tasksQueue.remove(task);
	}

	public void removeQueuedTasks(Collection<ComparisonTask> tasks) {
		tasksQueue.removeAll(tasks);
	}
	
	public void executeAllTasks() {
		executeTasks(tasksQueue);
	}

	public void executeTasks(String captureName) {
		List<ComparisonTask> tasks = new ArrayList<ComparisonTask>();
		//find requested tasks
		for(ComparisonTask task : tasksQueue) {
			if(task.getCaptureName().equals(captureName)) {
				tasks.add(task);
			}
		}
		executeTasks(tasks);
	}

	private void executeTasks(Collection<ComparisonTask> tasks) {
		//execute comparison tasks
		try {
			configuration.getExecutor().executeTasks(tasks);
		}
		finally{
			removeQueuedTasks(tasks); //remove all tasks from queue
		}
	}

}
