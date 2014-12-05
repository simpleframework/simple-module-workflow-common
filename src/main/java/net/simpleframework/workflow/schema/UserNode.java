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
public class UserNode extends AbstractTaskNode {
	/* 表单类 */
	private String formClass;

	private String timoutHours;

	private AbstractParticipantType participantType;

	/* 是否允许选择多个后续路由(手动模式) */
	private boolean multiTransitionSelected;

	public UserNode(final XmlElement beanElement, final ProcessNode processNode) {
		super(beanElement == null ? addNode(processNode, "user-node") : beanElement, processNode);
	}

	public boolean isMultiTransitionSelected() {
		return multiTransitionSelected;
	}

	public UserNode setMultiTransitionSelected(final boolean multiTransitionSelected) {
		this.multiTransitionSelected = multiTransitionSelected;
		return this;
	}

	public AbstractParticipantType getParticipantType() {
		if (participantType == null) {
			participantType = new AbstractParticipantType.User(null, this);
		}
		return participantType;
	}

	public UserNode setParticipantType(final AbstractParticipantType participantType) {
		this.participantType = participantType;
		return this;
	}

	public String getFormClass() {
		return formClass;
	}

	public UserNode setFormClass(final String formClass) {
		this.formClass = formClass;
		return this;
	}

	public String getTimoutHours() {
		return timoutHours;
	}

	public UserNode setTimoutHours(final String timoutHours) {
		this.timoutHours = timoutHours;
		return this;
	}

	@Override
	public short getTasknodeType() {
		return TT_USER;
	}

	@Override
	public String toString() {
		return StringUtils.text(super.toString(), $m("UserNode.0"));
	}

	@Override
	public void syncElement() {
		super.syncElement();
		getParticipantType().syncElement();
	}

	@Override
	public void parseElement() {
		super.parseElement();

		final XmlElement element = getElement().element("participant-type");
		if (element == null) {
			return;
		}
		XmlElement ele2;
		if ((ele2 = element.element("role")) != null) {
			setParticipantType(new Role(ele2, this));
		} else if ((ele2 = element.element("relative-role")) != null) {
			setParticipantType(new RelativeRole(ele2, this));
		} else if ((ele2 = element.element("user")) != null) {
			setParticipantType(new AbstractParticipantType.User(ele2, this));
		} else if ((ele2 = element.element("rule-role")) != null) {
			setParticipantType(new RuleRole(ele2, this));
		}
	}

	public static class Role extends AbstractParticipantType.BaseRole {
		private String responseValue;

		/* 当多个参与者时，是否顺序执行当前任务 */
		private boolean sequential;

		/* 当多个参与者时，是否共享同一个任务实例 */
		private boolean instanceShared;

		/* 手动模式，选取参与者 */
		private boolean manual;

		/* 在手动模式下，是否允许选择多个参与者 */
		private boolean multiSelected;

		public Role(final XmlElement beanElement, final UserNode parent) {
			super(beanElement == null ? addParticipant(parent, "role") : beanElement, parent);
		}

		public String getResponseValue() {
			return responseValue;
		}

		public void setResponseValue(final String responseValue) {
			this.responseValue = responseValue;
		}

		public boolean isSequential() {
			return sequential;
		}

		public void setSequential(final boolean sequential) {
			this.sequential = sequential;
		}

		public boolean isInstanceShared() {
			return instanceShared;
		}

		public void setInstanceShared(final boolean instanceShared) {
			this.instanceShared = instanceShared;
		}

		public boolean isManual() {
			return manual;
		}

		public BaseRole setManual(final boolean manual) {
			this.manual = manual;
			return this;
		}

		public boolean isMultiSelected() {
			return multiSelected;
		}

		public void setMultiSelected(final boolean multiSelected) {
			this.multiSelected = multiSelected;
		}

		@Override
		public void parseElement() {
			instanceShared = true;
			super.parseElement();
		}
	}

	// 相对角色
	public static class RelativeRole extends Role {
		private ERelativeType relativeType;

		/**
		 * 与ERelativeType的关系，与具体实现有关
		 * relative为空时，为ERelativeType定义的角色，否则匹配与ERelativeType定义角色的关系
		 */
		private String relative;

		/* 是否相对关系限定在同一部门 */
		private boolean indept;

		private String preActivity;

		public RelativeRole(final XmlElement beanElement, final UserNode parent) {
			super(beanElement == null ? addParticipant(parent, "relative-role") : beanElement, parent);
		}

		public ERelativeType getRelativeType() {
			return relativeType == null ? ERelativeType.processInitiator : relativeType;
		}

		public RelativeRole setRelativeType(final ERelativeType relativeType) {
			this.relativeType = relativeType;
			return this;
		}

		public String getRelative() {
			return relative;
		}

		public void setRelative(final String relative) {
			this.relative = relative;
		}

		public boolean isIndept() {
			return indept;
		}

		public RelativeRole setIndept(final boolean indept) {
			this.indept = indept;
			return this;
		}

		public String getPreActivity() {
			return preActivity;
		}

		public RelativeRole setPreActivity(final String preActivity) {
			this.preActivity = preActivity;
			return this;
		}
	}

	// 规则角色
	public static class RuleRole extends Role {
		/* 规则所需参数 */
		private String params;

		public RuleRole(final XmlElement beanElement, final UserNode parent) {
			super(beanElement == null ? addParticipant(parent, "rule-role") : beanElement, parent);
		}

		public String getParams() {
			return params;
		}

		public void setParams(final String params) {
			this.params = params;
		}
	}

	public static enum ERelativeType {
		/**
		 * 流程启动者
		 */
		processInitiator {
			@Override
			public String toString() {
				return $m("ERelativeType.processInitiator");
			}
		},

		/**
		 * 前一任务的参与者
		 */
		preActivityParticipant {
			@Override
			public String toString() {
				return $m("ERelativeType.preActivityParticipant");
			}
		},

		/**
		 * 前一指定(名称)任务的参与者
		 */
		preNamedActivityParticipant {
			@Override
			public String toString() {
				return $m("ERelativeType.preNamedActivityParticipant");
			}
		}
	}
}
