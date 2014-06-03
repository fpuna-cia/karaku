package py.una.pol.karaku.util;

import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

public class SIGHCustomJasperDataSource extends JRAbstractBeanDataSource {

	private List<?> objects;
	/**
	 * Representa a la fila actual
	 */
	private int row = -1;
	/**
	 * Representa a la columna actual;
	 */
	private int column;

	public SIGHCustomJasperDataSource(List<?> objects) {

		super(true);
		this.objects = objects;
	}

	@Override
	public void moveFirst() throws JRException {

		row = -1;
		column = 0;

	}

	@Override
	public boolean next() throws JRException {

		row++;
		column = 0;
		if (this.objects.size() <= row) {
			return false;
		}
		return true;
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {

		if (objects.get(row) instanceof String) {
			if (column == 0) {
				column++;
				return objects.get(row);
			} else {
				return null;
			}
		}
		Object[] columnas = (Object[]) objects.get(row);
		if (columnas.length <= column) {
			column++;
			return null;
		}
		Object object = ((Object[]) objects.get(row))[column];
		column++;
		return object;
	}

}
