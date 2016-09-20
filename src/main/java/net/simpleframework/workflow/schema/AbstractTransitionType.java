package net.simpleframework.workflow.schema;

import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AbstractTransitionType extends AbstractNode {

	public AbstractTransitionType(final XmlElement beanElement, final TransitionNode parent) {
		super(beanElement, parent);
	}

	public static class Conditional extends AbstractTransitionType {
		private boolean manual;

		private String expression;

		public Conditional(final XmlElement beanElement, final TransitionNode parent) {
			super(beanElement == null ? addTransitionType(parent, "conditional") : beanElement,
					parent);
		}

		public String getExpression() {
			return expression;
		}

		public Conditional setExpression(final String expression) {
			this.expression = expression;
			return this;
		}

		public boolean isManual() {
			return manual;
		}

		public Conditional setManual(final boolean manual) {
			this.manual = manual;
			return this;
		}
	}

	public static class LogicConditional extends AbstractTransitionType {
		private boolean manual;

		private ETransitionLogic logic;

		private String transitionId;

		public LogicConditional(final XmlElement beanElement, final TransitionNode parent) {
			super(beanElement == null ? addTransitionType(parent, "logic-conditional") : beanElement,
					parent);
		}

		public ETransitionLogic getLogic() {
			return logic;
		}

		public LogicConditional setLogic(final ETransitionLogic logic) {
			this.logic = logic;
			return this;
		}

		public String getTransitionId() {
			return transitionId;
		}

		public LogicConditional setTransitionId(final String transitionId) {
			this.transitionId = transitionId;
			return this;
		}

		public boolean isManual() {
			return manual;
		}

		public LogicConditional setManual(final boolean manual) {
			this.manual = manual;
			return this;
		}
	}

	public static class Interface extends AbstractTransitionType {
		private String handlerClass;

		public Interface(final XmlElement beanElement, final TransitionNode parent) {
			super(beanElement == null ? addTransitionType(parent, "interface") : beanElement, parent);
		}

		public String getHandlerClass() {
			return handlerClass;
		}

		public Interface setHandlerClass(final String handlerClass) {
			this.handlerClass = handlerClass;
			return this;
		}
	}

	static XmlElement addTransitionType(final TransitionNode transition, final String name) {
		return transition.child("transition-type", true).clearContent().addElement(name);
	}
}
