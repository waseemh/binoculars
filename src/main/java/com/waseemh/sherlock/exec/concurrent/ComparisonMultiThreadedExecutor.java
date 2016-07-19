package com.waseemh.sherlock.exec.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.waseemh.sherlock.configuration.Configuration;
import com.waseemh.sherlock.exceptions.BinocularsWrappedException;
import com.waseemh.sherlock.exec.ComparisonResult;
import com.waseemh.sherlock.exec.ComparisonTask;
import com.waseemh.sherlock.exec.ComparisonTasksExecutor;

public class ComparisonMultiThreadedExecutor implements ComparisonTasksExecutor{

	private final static int NUM_THREADS=4;

	ExecutorService threadPool;
	CompletionService<ComparisonResult> pool;

	Configuration configuration;

	public ComparisonMultiThreadedExecutor(Configuration configuration) {
		this.threadPool = Executors.newFixedThreadPool(NUM_THREADS);
		this.pool = new ExecutorCompletionService<ComparisonResult>(threadPool);
		this.configuration = configuration;
	}

	public void executeTasks(Collection<ComparisonTask> compareTasks) {

		List<ComparisonResult> results = new ArrayList<ComparisonResult>();

		Iterator<ComparisonTask> it = compareTasks.iterator();

		ComparisonResult result = null;

		while(it.hasNext()) {
			pool.submit(it.next());
		}

		for(int i = 0; i < compareTasks.size(); i++){
			try {
				result = pool.take().get();
			}
			catch (Exception e) {
				throw new BinocularsWrappedException("Error while retrieving comparison result ",e);
			}
			results.add(result);
		}

		//threadPool.shutdown();
		
		configuration.getHandler().handle(results);
	}

}