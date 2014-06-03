/*
 * @ButtonActions.java 1.0 May 31, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 31, 2013
 * 
 */
public enum ButtonAction {
	KEYUP {

		@Override
		String getActionName() {

			return "keyup";
		}
	},
	MOUSEDOWN {

		@Override
		String getActionName() {

			return "mousedown";
		}
	},
	CLICK {

		@Override
		String getActionName() {

			return "click";
		}
	},
	MOUSEOVER {

		@Override
		String getActionName() {

			return "mouseover";
		}
	},
	BEGIN {

		@Override
		String getActionName() {

			return "begin";
		}
	},
	MOUSEMOVE {

		@Override
		String getActionName() {

			return "mousemove";
		}
	},
	KEYDOWN {

		@Override
		String getActionName() {

			return "keydown";
		}
	},
	MOUSEOUT {

		@Override
		String getActionName() {

			return "mouseout";
		}
	},
	BEFOREDOMUPDATE {

		@Override
		String getActionName() {

			return "beforedomupdate";
		}
	},
	KEYPRESS {

		@Override
		String getActionName() {

			return "keypress";
		}
	},
	COMPLETE {

		@Override
		String getActionName() {

			return "complete";
		}
	},
	DBLCLICK {

		@Override
		String getActionName() {

			return "dblclick";
		}
	},
	ACTION {

		@Override
		String getActionName() {

			return "action";
		}
	},
	MOUSEUP {

		@Override
		String getActionName() {

			return "mouseup";
		}
	};

	abstract String getActionName();

}
