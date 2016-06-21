package com.waseemh.sherlock.exec;

import java.util.Collection;

public interface ComparisonTasksExecutor {

	void executeTasks(Collection<ComparisonTask> compareTasks);
	
}