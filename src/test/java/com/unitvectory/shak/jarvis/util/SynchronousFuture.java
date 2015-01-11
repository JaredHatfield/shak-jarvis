package com.unitvectory.shak.jarvis.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The synchronous future used in testing.
 * 
 * @author Jared Hatfield
 *
 * @param <T>
 */
public class SynchronousFuture<T> implements Future<T> {

	private T result;

	private ExecutionException failure;

	public SynchronousFuture() {
	}

	public SynchronousFuture(T result, Exception exception) {
		this.result = result;
		if (exception != null) {
			this.failure = new ExecutionException(exception);
		}
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	public boolean isCancelled() {
		return false;
	}

	public boolean isDone() {
		return true;
	}

	public T get() throws InterruptedException, ExecutionException {
		if (this.failure != null) {
			throw this.failure;
		}

		return this.result;
	}

	public T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		if (this.failure != null) {
			throw this.failure;
		}

		return this.result;
	}

}
