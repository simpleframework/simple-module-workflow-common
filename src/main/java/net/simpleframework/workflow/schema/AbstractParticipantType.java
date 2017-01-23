package net.simpleframework.workflow.schema;

import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractParticipantType extends AbstractNode {
	private String participant;

	public AbstractParticipantType(final XmlElement beanElement, final AbstractNode parent) {
		super(beanElement, parent);
	}

	public String getParticipant() {
		return participant != null ? participant.trim() : participant;
	}

	public AbstractParticipantType setParticipant(final String participant) {
		this.participant = participant;
		return this;
	}

	public static class BaseRole extends AbstractParticipantType {

		public BaseRole(final XmlElement beanElement, final AbstractNode parent) {
			super(beanElement == null ? addParticipant(parent, "role") : beanElement, parent);
		}
	}

	public static class User extends AbstractParticipantType {

		public User(final XmlElement beanElement, final AbstractNode parent) {
			super(beanElement == null ? addParticipant(parent, "user") : beanElement, parent);
		}
	}

	static XmlElement addParticipant(final AbstractNode parent, final String name) {
		return parent.child("participant-type", true).clearContent().addElement(name);
	}
}
