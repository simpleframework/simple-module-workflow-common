package net.simpleframework.workflow.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractTaskNode extends Node {
	public static final short STARTNODE_TYPE = 0;

	public static final short ENDNODE_TYPE = 1;

	public static final short USERNODE_TYPE = 2;

	public static final short SUBNODE_TYPE = 3;

	public AbstractTaskNode(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement, processNode);
	}

	public abstract short getTasknodeType();

	public Collection<TransitionNode> fromTransitions() {
		final ArrayList<TransitionNode> al = new ArrayList<TransitionNode>();
		for (final Node node : ((ProcessNode) parent()).nodes()) {
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
		for (final Node node : ((ProcessNode) parent()).nodes()) {
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

		XmlElement listenersElement = child("listeners");
		if (listenersElement == null) {
			listenersElement = addElement("listeners");
		} else {
			listenersElement.clearContent();
		}

		for (final String listenerClass : listeners()) {
			addElement(listenersElement, "listener").setText(listenerClass);
		}
	}

	@Override
	public void parseElement() {
		super.parseElement();

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
