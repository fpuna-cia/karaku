/*
 * @ButtonColumn.java 1.0 Mar 13, 2013 Sistema Integral de Gestión Hospitalaria
 */
package py.una.med.base.dynamic.tables;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import py.una.med.base.dynamic.forms.Button;

/**
 * Columna que tiene una cabecera simple y N botones en el cuerpo, estos botones
 * son del tipo {@link HtmlCommandButton}, notar que los componentes no pueden
 * ser ajax
 * 
 * @author Arturo Volpe Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
public class ButtonColumn extends SimpleHeaderColumn {

	private List<Button> buttons;

	public ButtonColumn() {

		buttons = new ArrayList<Button>();
	}

	@Override
	public UIComponent getBody() {

		return null;
	}

	@Override
	protected boolean hasBoddy() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.tables.Column#build()
	 */
	@Override
	public void build() {

		for (Button button : buttons) {
			getBind().getChildren().add(button.getBind());
		}
		super.build();
	}

}
