package org.alixia.javalibrary.taskloader;

import java.util.Stack;

public class TaskLoader<T> {
	private Stack<T> tasks = new Stack<>();

	private final TaskHandler<T> handler;

	@SafeVarargs
	public TaskLoader(TaskHandler<T> handler, T... tasks) {
		this.handler = handler;
		addTasks(tasks);
	}

	public final void addTask(T t) {
		tasks.add(t);
		start();
	}

	private void start() {
		if (!thread.isAlive())
			thread.start();
	}

	@SafeVarargs
	public final void addTasks(T... tasks) {
		for (T t : tasks)
			this.tasks.add(t);
		start();
	}

	private boolean suppressWarnings = true;
	private String name;

	public boolean isSuppressWarnings() {
		return suppressWarnings;
	}

	public void setSuppressWarnings(boolean suppressWarnings) {
		this.suppressWarnings = suppressWarnings;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				while (!tasks.isEmpty())
					handler.handle(tasks.pop());
			} catch (Exception e) {
				if (!suppressWarnings)
					return;
				if (name != null)
					System.err.print(name + ": ");
				e.printStackTrace();
			}
			thread = new Thread(this);
		}
	});
}
