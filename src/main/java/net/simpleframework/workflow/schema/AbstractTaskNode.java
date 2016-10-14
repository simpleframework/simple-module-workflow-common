package net.simpleframework.workflow.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractTaskNode extends Node {

	public static final short TT_START = 0;
	public static final short TT_END = 1;
	public static final short TT_USER = 2;
	public static final short TT_SUB = 3;
	public static final short TT_MERGE = 4;

	public AbstractTaskNode(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement, processNode);
	}

	public abstract short getTasknodeType();

	public Collection<TransitionNode> fromTransitions() {
		final ArrayList<TransitionNode> al = new ArrayList<TransitionNode>();
		for (final Node node : ((ProcessNode) getParent()).nodes()) {
			if (node instanceof TransitionNode) {
				final TransitionNode transition = (TransitionNode) node;
				if (getId().equals(transition.getTo())) {
					al.add(transition);
				}
			}
		}
		return al;
	}

	public Collection<TransitionNode> toTransitions() {
		final ArrayList<TransitionNode> al = new ArrayList<TransitionNode>();
		for (final Node node : ((ProcessNode) getParent()).nodes()) {
			if (node instanceof TransitionNode) {
				final TransitionNode transition = (TransitionNode) node;
				if (getId().equals(transition.getFrom())) {
					al.add(transition);
				}
			}
		}
		return al;
	}

	@Override
	public void syncElement() {
		super.syncElement();
		for (final VariableNode variable : variables().values()) {
			variable.syncElement();
		}

		final Set<String> listeners = listeners();
		final XmlElement listenersElement = child("listeners", listeners.size() > 0);
		if (listenersElement != null) {
			listenersElement.clearContent();
			for (final String listenerClass : listeners) {
				listenersElement.addElement("listener").setText(listenerClass);
			}
		}
	}

	@Override
	public void parseElement(final IScriptEval scriptEval) {
		super.parseElement(scriptEval);

		variables = new HashMap<String, VariableNode>();
		Iterator<?> it = children("variables");
		while (it.hasNext()) {
			final VariableNode variable = new VariableNode((XmlElement) it.next(), this);
			variables.put(variable.getName(), variable);
		}

		listeners = new LinkedHashSet<String>();
		it = children("listeners");
		while (it.hasNext()) {
			final XmlElement ele2 = (XmlElement) it.next();
			listeners.add(ele2.getText());
		}
	}
}
