/**
 * 
 */
package kr.osci.ide.codegen.ui;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import kr.osci.ide.codegen.generator.SqlStatement;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class SqlStatementLabelProvider extends LabelProvider implements ITableLabelProvider {

	/**
	 * 
	 */
	public SqlStatementLabelProvider() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		SqlStatement data = (SqlStatement) element;

		switch (columnIndex) {
			case 0:
				return data.getSqlID();
			case 1:
				return data.getSqlType();
			case 2:
				return data.getParamSimpleName();
			default:
				return ""; //$NON-NLS-1$
		}
	}

}
