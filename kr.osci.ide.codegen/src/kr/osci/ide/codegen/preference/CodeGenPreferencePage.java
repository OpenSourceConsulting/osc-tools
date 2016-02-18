/**
 * 
 */
package kr.osci.ide.codegen.preference;

import kr.osci.ide.codegen.Activator;
import kr.osci.ide.codegen.generator.SourceCodeGenerator;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Administrator
 *
 */
public class CodeGenPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * 
	 */
	public CodeGenPreferencePage() {
		setDescription("이 페이지는 OSC Tools CRUD 코드 자동생성기 환경설정 페이지 입니다.");
	}



	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		
	}


	@Override
	protected void createFieldEditors() {
		
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_MAPPER, 		"Mapper Sub Package", getFieldEditorParent()));
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_DTO, 		"Dto Sub Package", getFieldEditorParent()));
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_DAO, 		"Dao Sub Package", getFieldEditorParent()));
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_SERVICE, 	"Service Sub Package", getFieldEditorParent()));
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_CONTROLLER,  "Controller Sub Package", getFieldEditorParent()));
		
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUFFIX_MAPPER, 	"Mapper Suffix", getFieldEditorParent()));
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUFFIX_DTO, 		"Dto Suffix", getFieldEditorParent()));
		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_SUFFIX_DAO, 		"Dao Suffix", getFieldEditorParent()));

		addField(new StringFieldEditor(SourceCodeGenerator.PREFER_PROP_DTO_PARENT, 		"Dto Parent Class", getFieldEditorParent()));
		
	}

}
