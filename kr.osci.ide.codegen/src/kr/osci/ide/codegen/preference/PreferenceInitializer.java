	/**
 * 
 */
package kr.osci.ide.codegen.preference;

import kr.osci.ide.codegen.Activator;
import kr.osci.ide.codegen.generator.SourceCodeGenerator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * <pre>
 * preference default ¼³Á¤.
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * 
	 */
	public PreferenceInitializer() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(SourceCodeGenerator.PREFER_PROP_SUFFIX_MAPPER, "Mapper");
		store.setDefault(SourceCodeGenerator.PREFER_PROP_SUFFIX_DAO, "Repository");

	}

}
