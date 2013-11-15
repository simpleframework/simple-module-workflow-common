package net.simpleframework.workflow.schema;

import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractProcessStartupType extends AbstractNode {
	public AbstractProcessStartupType(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement, processNode);
	}

	public static class Manual extends AbstractProcessStartupType {
		private AbstractParticipantType participantType;

		public Manual(final XmlElement beanElement, final ProcessNode processNode) {
			super(beanElement == null ? addStartupType(processNode, "manual") : beanElement,
					processNode);
		}

		public AbstractParticipantType getParticipantType() {
			if (participantType == null) {
				participantType = new AbstractParticipantType.User(null, this);
			}
			return participantType;
		}

		public Manual setParticipantType(final AbstractParticipantType participantType) {
			this.participantType = participantType;
			return this;
		}

		@Override
		public void syncElement() {
			super.syncElement();
			getParticipantType().syncElement();
		}

		@Override
		public void parseElement() {
			super.parseElement();

			final XmlElement element = getBeanElement();
			final XmlElement ptElement = element != null ? element.element("participant-type") : null;
			if (ptElement == null) {
				return;
			}

			XmlElement ele2;
			if ((ele2 = ptElement.element("role")) != null) {
				participantType = new AbstractParticipantType.Role(ele2, this);
			} else if ((ele2 = ptElement.element("user")) != null) {
				participantType = new AbstractParticipantType.User(ele2, this);
			}
		}
	}

	public static class Email extends AbstractProcessStartupType {
		public Email(final XmlElement beanElement, final ProcessNode processNode) {
			super(beanElement == null ? addStartupType(processNode, "email") : beanElement,
					processNode);
		}
	}

	static XmlElement addStartupType(final ProcessNode processNode, final String name) {
		return processNode.addChild("startup-type", name, true);
	}
}
