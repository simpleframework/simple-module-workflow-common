package net.simpleframework.workflow.remote;

import java.io.IOException;
import java.util.Map;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IWorkflowRemote {

	Map<String, Object> call(String url, String method) throws IOException;

	/**
	 * 
	 * @param url
	 * @param method
	 * @param data
	 * @return
	 * @throws IOException
	 */
	Map<String, Object> call(String url, String method, Map<String, Object> data) throws IOException;
}
