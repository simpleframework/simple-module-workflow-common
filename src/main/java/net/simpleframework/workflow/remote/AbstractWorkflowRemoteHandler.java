package net.simpleframework.workflow.remote;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractWorkflowRemoteHandler extends ObjectEx
		implements IWorkflowRemoteHandler {

	@Override
	public Map<String, Object> call(final String url, final String method) throws IOException {
		return call(url, method, null);
	}
}
