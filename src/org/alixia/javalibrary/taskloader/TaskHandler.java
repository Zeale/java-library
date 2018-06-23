package org.alixia.javalibrary.taskloader;

public interface TaskHandler<T> {
	void handle(T task);
}
