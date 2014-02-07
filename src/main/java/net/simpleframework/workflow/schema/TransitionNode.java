package net.simpleframework.workflow.schema;

import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TransitionNode extends Node {

	private AbstractTransitionType transitionType;

	private String from, to;

	public TransitionNode(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement == null ? addNode(processNode, "transition") : beanElement, processNode);
	}

	public String getFrom() {
		return from;
	}

	public AbstractTaskNode from() {
		return (AbstractTaskNode) ((ProcessNode) parent()).getNodeById(getFrom());
	}

	public TransitionNode setFrom(final String from) {
		this.from = from;
		return this;
	}

	public String getTo() {
		return to;
	}

	public AbstractTaskNode to() {
		return (AbstractTaskNode) ((ProcessNode) parent()).getNodeById(getTo());
	}

	public TransitionNode setTo(final String to) {
		this.to = to;
		return this;
	}

	public AbstractTransitionType getTransitionType() {
		if (transitionType == null) {
			transitionType = new AbstractTransitionType.Conditional(null, this);
		}
		return transitionType;
	}

	public void setTransitionType(final AbstractTransitionType transitionType) {
		this.transitionType = transitionType;
	}

	@Override
	public void syncElement() {
		super.syncElement();
		getTransitionType().syncElement();
	}

	@Override
	public void parseElement() {
		super.parseElement();
		final XmlElement ele = getElement().element("transition-type");
		if (ele == null) {
			return;
		}
		XmlElement ele2;
		if ((ele2 = ele.element("conditional")) != null) {
			setTransitionType(new AbstractTransitionType.Conditional(ele2, this));
		} else if ((ele2 = ele.element("logic-conditional")) != null) {
			setTransitionType(new AbstractTransitionType.LogicConditional(ele2, this));
		} else if ((ele2 = ele.element("interface")) != null) {
			setTransitionType(new AbstractTransitionType.Interface(ele2, this));
		}
	}
}
