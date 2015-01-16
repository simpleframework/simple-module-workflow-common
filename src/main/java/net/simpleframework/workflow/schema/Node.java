package net.simpleframework.workflow.schema;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.workflow.WorkflowException;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class Node extends AbstractNode {
	private String id;

	private String name, text;

	private String ext;

	private String description;

	public Node(final XmlElement beanElement, final ProcessNode parent) {
		super(beanElement, parent);
	}

	public String getId() {
		if (!StringUtils.hasText(id)) {
			id = ID.uuid().toString();
		}
		return id;
	}

	public Node setId(final String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Node setName(final String name) {
		this.name = name;
		return this;
	}

	public String getText() {
		if (!StringUtils.hasText(text)) {
			text = getName();
		}
		return text;
	}

	public Node setText(final String text) {
		this.text = text;
		return this;
	}

	public String getExt() {
		return ext;
	}

	public Node setExt(final String ext) {
		this.ext = ext;
		_ext = null;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Node setDescription(final String description) {
		this.description = description;
		return this;
	}

	protected Map<String, VariableNode> variables;

	public Map<String, VariableNode> variables() {
		return variables;
	}

	public VariableNode getVariableNodeByName(final String name) {
		final VariableNode variable = variables.get(name);
		if (variable == null) {
			throw WorkflowException.of($m("ProcessNode.0", name));
		}
		return variable;
	}

	public VariableNode removeVariable(final String name) {
		final VariableNode variable = variables.remove(name);
		removeElement(variable);
		return variable;
	}

	public VariableNode addVariable(final String name) {
		final VariableNode variable = new VariableNode(null, this);
		variable.setName(name);
		variables.put(variable.getName(), variable);
		return variable;
	}

	protected Set<String> listeners;

	public Set<String> listeners() {
		return listeners;
	}

	@Override
	public String toString() {
		final String txt = getText();
		return StringUtils.hasText(txt) ? txt : getName();
	}

	private Properties _ext;

	@Override
	public String getProperty(final String key) {
		String val = super.getProperty(key);
		if (val == null) {
			if (_ext == null) {
				_ext = new Properties();
				try {
					_ext.load(new StringReader(StringUtils.blank(getExt())));
				} catch (final IOException e) {
				}
			}
			val = _ext.getProperty(key);
		}
		return val;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "ext", "description" };
	}

	static XmlElement addNode(final ProcessNode processNode, final String name) {
		return processNode != null ? processNode.child("nodes", true).addElement(name) : null;
	}
}
