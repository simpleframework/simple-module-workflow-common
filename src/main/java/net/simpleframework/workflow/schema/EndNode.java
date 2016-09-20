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
public class EndNode extends AbstractTaskNode {

	public EndNode(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement == null ? addNode(processNode, "end-node") : beanElement, processNode);
	}

	@Override
	public short getTasknodeType() {
		return TT_END;
	}

	@Override
	public String toString() {
		return StringUtils.text(super.toString(), $m("EndNode.0"));
	}
}
