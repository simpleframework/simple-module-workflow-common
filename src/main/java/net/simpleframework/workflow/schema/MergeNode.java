package net.simpleframework.workflow.schema;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MergeNode extends AbstractTaskNode {
	private static final long serialVersionUID = -2297274627939387050L;

	/* 计数器，决定合并是否完成 */
	private String condition;

	public MergeNode(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement == null ? addNode(processNode, "merge-node") : beanElement, processNode);
	}

	public String getCondition() {
		return condition;
	}

	public MergeNode setCondition(final String condition) {
		this.condition = condition;
		return this;
	}

	@Override
	public short getTasknodeType() {
		return TT_MERGE;
	}

	@Override
	public String toString() {
		return StringUtils.text(super.toString(), $m("MergeNode.0"));
	}
}
