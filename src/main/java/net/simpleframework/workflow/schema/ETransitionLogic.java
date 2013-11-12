package net.simpleframework.workflow.schema;

import static net.simpleframework.common.I18n.$m;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum ETransitionLogic {
	and {

		@Override
		public String toString() {
			return $m("ETransitionLogic.0");
		}
	},

	not {

		@Override
		public String toString() {
			return $m("ETransitionLogic.1");
		}
	}
}
