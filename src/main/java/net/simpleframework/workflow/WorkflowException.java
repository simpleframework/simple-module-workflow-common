package net.simpleframework.workflow;

import net.simpleframework.common.th.RuntimeExceptionEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class WorkflowException extends RuntimeExceptionEx {
	private static final long serialVersionUID = 7984366934401318860L;

	public WorkflowException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException of(final Throwable throwable) {
		return _of(WorkflowException.class, null, throwable);
	}

	public static RuntimeException of(final String msg) {
		return _of(WorkflowException.class, msg);
	}
}
