package net.simpleframework.workflow.schema;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MergeNode extends AbstractTaskNode {

	/* 计数器，决定合并是否完成 */
	private int count;

	public MergeNode(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement == null ? addNode(processNode, "merge-node") : beanElement, processNode);
	}

	public int getCount() {
		return count;
	}

	public MergeNode setCount(final int count) {
		this.count = count;
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
