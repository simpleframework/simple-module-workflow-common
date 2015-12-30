package net.simpleframework.workflow.schema;

import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ID;
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
		final ProcessDocument doc = new ProcessDocument(new StringReader(toString()));
		// 替换所有的id
		final Map<String, String> idmap = new HashMap<String, String>();
		final ProcessNode pn = doc.getProcessNode();
		final ArrayList<TransitionNode> transitions = new ArrayList<TransitionNode>();
		for (final Node node : pn.nodes()) {
			final String id = ID.uuid().toString();
			idmap.put(node.getId(), id);
			node.setId(id);

			if (node instanceof TransitionNode) {
				transitions.add((TransitionNode) node);
			}
		}

		for (final TransitionNode transition : transitions) {
			transition.setFrom(idmap.get(transition.getFrom()));
			transition.setTo(idmap.get(transition.getTo()));
		}

		pn.setId(ID.uuid().toString());
		return doc;
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
