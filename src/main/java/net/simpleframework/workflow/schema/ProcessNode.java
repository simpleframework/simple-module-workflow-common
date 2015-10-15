package net.simpleframework.workflow.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.Version;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.workflow.WorkflowException;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ProcessNode extends Node {
	/* 版本 */
	private Version version;
	/* 作者 */
	private String author;
	/* 序号 */
	private int oorder;

	/* 含义：判断模型是流程实例共享，还是为每一个流程实例复制一个拷贝 */
	private boolean instanceShared;

	/* 待办表单 */
	private String formClass;
	/* 待阅表单 */
	private String viewClass;

	private AbstractProcessStartupType startupType;

	private Map<String, Node> nodes;

	public ProcessNode(final XmlElement beanElement) {
		super(beanElement, null);
	}

	public Version getVersion() {
		if (version == null) {
			version = new Version(1, 0, 0);
		}
		return version;
	}

	public ProcessNode setVersion(final Version version) {
		this.version = version;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public ProcessNode setAuthor(final String author) {
		this.author = author;
		return this;
	}

	public int getOorder() {
		return oorder;
	}

	public ProcessNode setOorder(final int oorder) {
		this.oorder = oorder;
		return this;
	}

	public boolean isInstanceShared() {
		return instanceShared;
	}

	public ProcessNode setInstanceShared(final boolean instanceShared) {
		this.instanceShared = instanceShared;
		return this;
	}

	public String getFormClass() {
		return formClass;
	}

	public ProcessNode setFormClass(final String formClass) {
		this.formClass = formClass;
		return this;
	}

	public String getViewClass() {
		return viewClass;
	}

	public ProcessNode setViewClass(final String viewClass) {
		this.viewClass = viewClass;
		return this;
	}

	public AbstractProcessStartupType getStartupType() {
		if (startupType == null) {
			startupType = new AbstractProcessStartupType.Manual(null, this);
		}
		return startupType;
	}

	public ProcessNode setStartupType(final AbstractProcessStartupType startupType) {
		this.startupType = startupType;
		return this;
	}

	public Node getNodeById(final String id) {
		return nodes.get(id);
	}

	public Node getNodeByName(final String name) {
		if (name != null) {
			for (final Node node : nodes.values()) {
				if (name.equals(node.getName())) {
					return node;
				}
			}
		}
		return null;
	}

	public Node removeNode(final String id) {
		final Node node = nodes.remove(id);
		removeElement(node);
		return node;
	}

	public Node addNode(final Class<? extends Node> nodeClass) {
		return addNode(null, nodeClass);
	}

	public Node addNode(final String text, final Class<? extends Node> nodeClass) {
		try {
			final Node t = nodeClass.getConstructor(XmlElement.class, ProcessNode.class).newInstance(
					null, this);
			t.setText(text);
			nodes.put(t.getId(), t);
			return t;
		} catch (final Exception e) {
			throw WorkflowException.of(e);
		}
	}

	public Collection<Node> nodes() {
		return nodes.values();
	}

	public TransitionNode addTransition(final AbstractTaskNode from, final AbstractTaskNode to) {
		return addTransition(null, from, to);
	}

	public TransitionNode addTransition(final String id, final AbstractTaskNode from,
			final AbstractTaskNode to) {
		final TransitionNode transition = (TransitionNode) addNode(id, TransitionNode.class);
		if (from != null) {
			transition.setFrom(from.getId());
		}
		if (to != null) {
			transition.setTo(to.getId());
		}
		return transition;
	}

	private StartNode startNode;

	public StartNode startNode() {
		if (startNode == null) {
			startNode = new StartNode(null, this);
			nodes.put(startNode.getId(), startNode);
		}
		return startNode;
	}

	private EndNode endNode;

	public EndNode endNode() {
		if (endNode == null) {
			endNode = new EndNode(null, this);
			nodes.put(endNode.getId(), endNode);
		}
		return endNode;
	}

	@Override
	public void syncElement() {
		super.syncElement();
		getStartupType().syncElement();

		for (final Node node : nodes()) {
			node.syncElement();
		}

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
		XmlElement ele = getElement().element("startup-type");
		if (ele != null) {
			XmlElement ele2;
			if ((ele2 = ele.element("manual")) != null) {
				setStartupType(new AbstractProcessStartupType.Manual(ele2, this));
			} else if ((ele2 = ele.element("email")) != null) {
				setStartupType(new AbstractProcessStartupType.Email(ele2, this));
			}
		}

		nodes = new LinkedHashMap<String, Node>();
		Iterator<?> it = children("nodes");
		while (it.hasNext()) {
			ele = (XmlElement) it.next();
			final String name = ele.getTagName();
			Node node = null;
			if ("user-node".equals(name)) {
				node = new UserNode(ele, this);
			} else if ("transition".equals(name)) {
				node = new TransitionNode(ele, this);
			} else if ("merge-node".equals(name)) {
				node = new MergeNode(ele, this);
			} else if ("sub-node".equals(name)) {
				node = new SubNode(ele, this);
			} else if ("start-node".equals(name)) {
				node = startNode = new StartNode(ele, this);
			} else if ("end-node".equals(name)) {
				node = endNode = new EndNode(ele, this);
			}
			if (node != null) {
				nodes.put(node.getId(), node);
			}
		}

		variables = new HashMap<String, VariableNode>();
		it = children("variables");
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
