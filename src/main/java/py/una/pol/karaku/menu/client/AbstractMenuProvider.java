/*
 * @AbstractMenuProvider.java 1.0 Oct 22, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.menu.client;

import java.util.ArrayList;
import java.util.List;
import py.una.med.base.menu.schemas.Menu;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 22, 2013
 *
 */
public abstract class AbstractMenuProvider implements IMenuProvider {

	private List<MenuChangeListener> listeners;

	/**
	 *
	 */
	public AbstractMenuProvider() {

		super();
	}

	/**
	 *
	 */
	protected void notifyMenuRebuild(List<Menu> builded) {

		for (MenuChangeListener listener : getListeners()) {
			listener.onChange(builded);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMenuChangeListener(MenuChangeListener mcl) {

		getListeners().add(mcl);
	}

	/**
	 * @return
	 */
	private List<MenuChangeListener> getListeners() {

		if (listeners == null) {
			listeners = new ArrayList<MenuChangeListener>();
		}

		return listeners;
	}

}
