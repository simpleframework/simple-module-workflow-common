package net.simpleframework.workflow.schema;

import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.ctx.common.xml.XmlDocument;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ProcessDocument extends XmlDocument {
	public ProcessDocument(final InputStream inputStream) {
		super(inputStream);
	}

	public ProcessDocument(final char[] charArray) {
		super(new CharArrayReader(charArray));
	}

	public ProcessDocument(final Reader reader) {
		super(reader);
	}

	public ProcessDocument() {
		super(ClassUtils.getResourceAsStream(ProcessDocument.class, "null-pdl.xml"));
	}

	private ProcessNode processNode;

	@Override
	public String toString() {
		getProcessNode().syncElement();
		return super.toString();
	}

	@Override
	public ProcessDocument clone() {
		return new ProcessDocument(new StringReader(toString()));
	}

	public ProcessNode getProcessNode() {
		if (processNode == null) {
			processNode = new ProcessNode(getRoot().element());
			processNode.startNode();
			processNode.endNode();
		}
		return processNode;
	}
}
