package net.simpleframework.workflow.schema;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.workflow.WorkflowException;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class VariableNode extends AbstractNode {
	private String name;

	private EVariableType type;

	/**
	 * 如果是集合，返回json格式
	 */
	private String value;

	/**
	 * 仅在流程变量时有效，所有流程实例共享此变量
	 */
	private boolean statically;

	/**
	 * 变量更改的历史记录数
	 */
	private int logs;

	private EVariableMode mode;

	private String description;

	public VariableNode(final XmlElement beanElement, final AbstractNode parent) {
		super(beanElement == null ? addVariable(parent, "variable") : beanElement, parent);
	}

	public String getName() {
		return name != null ? name.trim() : null;
	}

	public VariableNode setName(final String name) {
		this.name = name;
		return this;
	}

	public EVariableType getType() {
		return type == null ? EVariableType.vtString : type;
	}

	public VariableNode setType(final EVariableType type) {
		this.type = type;
		return this;
	}

	public EVariableMode getMode() {
		return mode == null ? EVariableMode.in : mode;
	}

	public VariableNode setMode(final EVariableMode mode) {
		this.mode = mode;
		return this;
	}

	public String getValue() {
		return value;
	}

	public VariableNode setValue(final String value) {
		this.value = value;
		return this;
	}

	public boolean isStatically() {
		return statically;
	}

	public VariableNode setStatically(final boolean statically) {
		this.statically = statically;
		return this;
	}

	public int getLogs() {
		return logs;
	}

	public VariableNode setLogs(final int logs) {
		this.logs = logs;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public VariableNode setDescription(final String description) {
		this.description = description;
		return this;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static void assertName(final String name) {
		if (name != null) {
			final char[] c = name.toCharArray();
			boolean b = Character.isJavaIdentifierStart(c[0]);
			if (b) {
				for (int i = 1; i < c.length; i++) {
					b = Character.isJavaIdentifierPart(c[i]);
					if (!b) {
						break;
					}
				}
			}
			if (!b) {
				throw WorkflowException.of($m("VariableNode.0", name));
			}
		}
	}

	static XmlElement addVariable(final AbstractNode parent, final String name) {
		return parent.child("variables", true).addElement(name);
	}
}
