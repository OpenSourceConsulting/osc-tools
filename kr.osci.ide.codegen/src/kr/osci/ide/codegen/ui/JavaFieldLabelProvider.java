/**
 * 
 */
package kr.osci.ide.codegen.ui;

import kr.osci.ide.codegen.generator.JavaField;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class JavaFieldLabelProvider extends LabelProvider implements ITableLabelProvider {

	/**
	 * 
	 */
	public JavaFieldLabelProvider() {
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
		JavaField data = (JavaField) element;

		switch (columnIndex) {
			case 0:
				return data.getColumn().getName();
			case 1:
				return data.getColumn().getTypeName();
			case 2:
				return data.getColumn().getScale()+"";
			default:
				return ""; //$NON-NLS-1$
		}
	}

}
