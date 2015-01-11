package com.unitvectory.shak.jarvis.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The synchronous executor used in testing.
 * 
 * @author Jared Hatfield
 *
 */
public class SynchronousExecutor implements ExecutorService {

	private boolean running;

	public SynchronousExecutor() {
		this.running = true;
	}

	public void execute(Runnable command) {
		command.run();
	}

	public void shutdown() {
		this.running = false;
	}

	public List<Runnable> shutdownNow() {
		return null;
	}

	public boolean isShutdown() {
		return !this.running;
	}

	public boolean isTerminated() {
		return !this.running;
	}

	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return true;
	}

	public <T> Future<T> submit(Callable<T> task) {
		try {
			return new SynchronousFuture<T>(task.call(), null);
		} catch (Exception e) {
			return new SynchronousFuture<T>(null, e);
		}
	}

	public <T> Future<T> submit(Runnable task, T result) {
		try {
			task.run();
			return new SynchronousFuture<T>(result, null);
		} catch (Exception e) {
			return new SynchronousFuture<T>(null, e);
		}
	}

	@SuppressWarnings("rawtypes")
	public Future<?> submit(Runnable task) {
		task.run();
		return new SynchronousFuture();
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
			throws InterruptedException {

		List<Future<T>> list = new ArrayList<Future<T>>();
		for (Callable<T> task : tasks) {
			try {
				T result = task.call();
				list.add(new SynchronousFuture<T>(result, null));
			} catch (Exception e) {
				list.add(new SynchronousFuture<T>(null, e));
			}

		}

		return list;
	}

	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return this.invokeAll(tasks);
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		List<Future<T>> list = new ArrayList<Future<T>>();
		T out = null;
		for (Callable<T> task : tasks) {
			try {
				T result = task.call();
				list.add(new SynchronousFuture<T>(result, null));
				if (result != null) {
					out = result;
				}
			} catch (Exception e) {
				list.add(new SynchronousFuture<T>(null, e));
			}

		}

		return out;
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
			long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return this.invokeAny(tasks);
	}

}
