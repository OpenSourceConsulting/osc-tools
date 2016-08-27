/**
 * 
 */
package kr.osci.ide.codegen.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.corext.codemanipulation.AddUnimplementedConstructorsOperation;
import org.eclipse.jdt.internal.corext.codemanipulation.AddUnimplementedMethodsOperation;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.codemanipulation.GetterSetterUtil;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.corext.refactoring.StubTypeContext;
import org.eclipse.jdt.internal.corext.refactoring.TypeContextChecker;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Strings;
import org.eclipse.jdt.internal.ui.javaeditor.ASTProvider;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.text.edits.TextEdit;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author Bongjin Kwon
 *
 */
public class JavaClassCreator {
	
	private IPackageFragmentRoot root;
	private IPackageFragment pack;
	private String typeName;
	
	private String superclass = "";
	private List<String> interfaces = new ArrayList<String>();
	private int fTypeKind = NewTypeWizardPage.CLASS_TYPE;
	
	/**
	 * a handle to the type to be created (does usually not exist, can be null)
	 */
	private IType fCurrType;
	
	private StubTypeContext fSuperClassStubTypeContext;
	private StubTypeContext fSuperInterfaceStubTypeContext;
	
	private JavaField[] fields = new JavaField[0];
	
	
	public JavaClassCreator(IPackageFragmentRoot root, IPackageFragment pack, String typeName) {
		this.root = root;
		this.pack = pack;
		this.typeName = typeName;
	}
	
	public void createType(IProgressMonitor monitor, String tableName) throws CoreException, InterruptedException {
		
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		monitor.beginTask("클래스 생성.", 6);

		if (pack == null) {
			pack= root.getPackageFragment(""); //$NON-NLS-1$
		}

		if (!pack.exists()) {
			String packName= pack.getElementName();
			pack= root.createPackageFragment(packName, true, new SubProgressMonitor(monitor, 1));
		} else {
			monitor.worked(1);
		}

		boolean needsSave;
		ICompilationUnit connectedCU= null;
		
		try{
			
			IType createdType;
			//ImportsManager imports;
			int indent= 0;

			Set<String> existingImports;

			String lineDelimiter= StubUtility.getLineDelimiterUsed(pack.getJavaProject());

			String cuName= typeName + JavaModelUtil.DEFAULT_CU_SUFFIX;
			ICompilationUnit parentCU= pack.createCompilationUnit(cuName, "", false, new SubProgressMonitor(monitor, 2)); //$NON-NLS-1$
			// create a working copy with a new owner

			needsSave= true;
			parentCU.becomeWorkingCopy(new SubProgressMonitor(monitor, 1)); // cu is now a (primary) working copy
			connectedCU= parentCU;

			IBuffer buffer= parentCU.getBuffer();

			//String simpleTypeStub= constructSimpleTypeStub();
			String simpleTypeStub= "public class " + typeName + "{ }";
			String cuContent= constructCUContent(parentCU, typeName, simpleTypeStub, lineDelimiter, tableName);
			buffer.setContents(cuContent);

			CompilationUnit astRoot= createASTForImports(parentCU);
			existingImports= getExistingImports(astRoot);

			//imports= new ImportsManager(astRoot);
			ImportRewrite fImportsRewrite = StubUtility.createImportRewrite(astRoot, true);
			
			// add an import that will be removed again. Having this import solves 14661
			//imports.addImport(JavaModelUtil.concatenateName(pack.getElementName(), typeName));
			fImportsRewrite.addImport(JavaModelUtil.concatenateName(pack.getElementName(), typeName));

			String typeContent= constructTypeStub(parentCU, fImportsRewrite, lineDelimiter);

			int index= cuContent.lastIndexOf(simpleTypeStub);
			if (index == -1) {
				AbstractTypeDeclaration typeNode= (AbstractTypeDeclaration) astRoot.types().get(0);
				int start= ((ASTNode) typeNode.modifiers().get(0)).getStartPosition();
				int end= typeNode.getStartPosition() + typeNode.getLength();
				buffer.replace(start, end - start, typeContent);
			} else {
				buffer.replace(index, simpleTypeStub.length(), typeContent);
			}

			createdType= parentCU.getType(typeName);
			
			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}

			// add imports for superclass/interfaces, so types can be resolved correctly

			ICompilationUnit cu= createdType.getCompilationUnit();

			//imports.create(false, new SubProgressMonitor(monitor, 1));
			TextEdit edit= fImportsRewrite.rewriteImports(new SubProgressMonitor(monitor, 1));
			JavaModelUtil.applyEdit(fImportsRewrite.getCompilationUnit(), edit, needsSave, null);

			JavaModelUtil.reconcile(cu);

			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}

			// set up again
			astRoot = createASTForImports(fImportsRewrite.getCompilationUnit());
			fImportsRewrite = StubUtility.createImportRewrite(astRoot, true);

			createTypeMembers(createdType, fImportsRewrite, new SubProgressMonitor(monitor, 1));
			
			if(fields.length > 0){
				createFields(createdType, cu, lineDelimiter, monitor);
			}

			// add imports
			//imports.create(false, new SubProgressMonitor(monitor, 1));
			edit= fImportsRewrite.rewriteImports(new SubProgressMonitor(monitor, 1));
			JavaModelUtil.applyEdit(fImportsRewrite.getCompilationUnit(), edit, needsSave, null);

			//removeUnusedImports(cu, existingImports, false);

			JavaModelUtil.reconcile(cu);

			ISourceRange range= createdType.getSourceRange();

			IBuffer buf= cu.getBuffer();
			String originalContent= buf.getText(range.getOffset(), range.getLength());

			String formattedContent= CodeFormatterUtil.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, originalContent, indent, lineDelimiter, pack.getJavaProject());
			formattedContent= Strings.trimLeadingTabsAndSpaces(formattedContent);
			buf.replace(range.getOffset(), range.getLength(), formattedContent);
			
			/*
			if (!isInnerClass) {
				String fileComment= getFileComment(cu);
				if (fileComment != null && fileComment.length() > 0) {
					buf.replace(0, 0, fileComment + lineDelimiter);
				}
			}
			*/
			//fCreatedType= createdType;

			if (needsSave) {
				cu.commitWorkingCopy(true, new SubProgressMonitor(monitor, 1));
			} else {
				monitor.worked(1);
			}
			
		} finally {
			if (connectedCU != null) {
				connectedCU.discardWorkingCopy();
			}
			monitor.done();
		}
	}
	
	/**
	 * Uses the New Java file template from the code template page to generate a
	 * compilation unit with the given type content.
	 * 
	 * @param cu The new created compilation unit
	 * @param typeContent The content of the type, including signature and type
	 * body.
	 * @param lineDelimiter The line delimiter to be used.
	 * @return String Returns the result of evaluating the new file template
	 * with the given type content.
	 * @throws CoreException when fetching the file comment fails or fetching the content for the
	 *             new compilation unit fails
	 * @since 2.1
	 */
	protected String constructCUContent(ICompilationUnit cu, String typeName, String typeContent, String lineDelimiter, String tableName) throws CoreException {
		String fileComment= CodeGeneration.getFileComment(cu, lineDelimiter);
		//String typeComment= getTypeComment(cu, lineDelimiter);
		String typeComment = CodeGeneration.getTypeComment(cu, typeName, new String[0], lineDelimiter);
		
		if (JavaField.useJPA) {
			typeComment = typeComment + "@Entity\r\n@Table(name = \""+tableName+"\")";
		}
		
		IPackageFragment pack= (IPackageFragment) cu.getParent();
		String content= CodeGeneration.getCompilationUnitContent(cu, fileComment, typeComment, typeContent, lineDelimiter);
		if (content != null) {
			ASTParser parser= ASTParser.newParser(ASTProvider.SHARED_AST_LEVEL);
			parser.setProject(cu.getJavaProject());
			parser.setSource(content.toCharArray());
			CompilationUnit unit= (CompilationUnit) parser.createAST(null);
			if ((pack.isDefaultPackage() || unit.getPackage() != null) && !unit.types().isEmpty()) {
				return content;
			}
		}
		StringBuffer buf= new StringBuffer();
		if (!pack.isDefaultPackage()) {
			buf.append("package ").append(pack.getElementName()).append(';'); //$NON-NLS-1$
		}
		buf.append(lineDelimiter).append(lineDelimiter);
		if (typeComment != null) {
			buf.append(typeComment).append(lineDelimiter);
		}
		buf.append(typeContent);
		return buf.toString();
	}
	
	private CompilationUnit createASTForImports(ICompilationUnit cu) {
		ASTParser parser= ASTParser.newParser(ASTProvider.SHARED_AST_LEVEL);
		parser.setSource(cu);
		parser.setResolveBindings(true);
		parser.setFocalPosition(0);
		return (CompilationUnit) parser.createAST(null);
	}


	private Set<String> getExistingImports(CompilationUnit root) {
		List<ImportDeclaration> imports= root.imports();
		Set<String> res= new HashSet<String>(imports.size());
		for (int i= 0; i < imports.size(); i++) {
			res.add(ASTNodes.asString(imports.get(i)));
		}
		return res;
	}
	
	/*
	 * Called from createType to construct the source for this type
	 */
	private String constructTypeStub(ICompilationUnit parentCU, ImportRewrite imports, String lineDelimiter) throws CoreException {
		StringBuffer buf= new StringBuffer();

		//int modifiers= getModifiers();
		//buf.append(Flags.toString(modifiers));
		buf.append("public ");
		//if (modifiers != 0) {
			buf.append(' ');
		//}
		
		String type= ""; //$NON-NLS-1$
		String templateID= ""; //$NON-NLS-1$
		switch (fTypeKind) {
			case NewTypeWizardPage.CLASS_TYPE:
				type= "class ";  //$NON-NLS-1$
				templateID= CodeGeneration.CLASS_BODY_TEMPLATE_ID;
				break;
			case NewTypeWizardPage.INTERFACE_TYPE:
				type= "interface "; //$NON-NLS-1$
				templateID= CodeGeneration.INTERFACE_BODY_TEMPLATE_ID;
				break;
			case NewTypeWizardPage.ENUM_TYPE:
				type= "enum "; //$NON-NLS-1$
				templateID= CodeGeneration.ENUM_BODY_TEMPLATE_ID;
				break;
			case NewTypeWizardPage.ANNOTATION_TYPE:
				type= "@interface "; //$NON-NLS-1$
				templateID= CodeGeneration.ANNOTATION_BODY_TEMPLATE_ID;
				break;
		}
		buf.append(type);
		buf.append(getTypeName());
		writeSuperClass(buf, imports);
		writeSuperInterfaces(buf, imports);

		buf.append(" {").append(lineDelimiter); //$NON-NLS-1$
		String typeBody= CodeGeneration.getTypeBody(templateID, parentCU, getTypeName(), lineDelimiter);
		if (typeBody != null) {
			buf.append(typeBody);
		} else {
			buf.append(lineDelimiter);
		}
		buf.append('}').append(lineDelimiter);
		return buf.toString();
	}
	
	// ---- construct CU body----------------

		private void writeSuperClass(StringBuffer buf, ImportRewrite imports) {
			if (fTypeKind == NewTypeWizardPage.CLASS_TYPE && superclass.length() > 0 && !"java.lang.Object".equals(superclass)) { //$NON-NLS-1$
				buf.append(" extends "); //$NON-NLS-1$

				ITypeBinding binding= null;
				if (fCurrType != null) {
					binding= TypeContextChecker.resolveSuperClass(superclass, fCurrType, getSuperClassStubTypeContext());
				}
				if (binding != null) {
					buf.append(imports.addImport(binding));
				} else {
					buf.append(imports.addImport(superclass));
				}
			}
		}

		private void writeSuperInterfaces(StringBuffer buf, ImportRewrite imports) {
			int last= interfaces.size() - 1;
			if (last >= 0) {
			    if (fTypeKind != NewTypeWizardPage.INTERFACE_TYPE) {
					buf.append(" implements "); //$NON-NLS-1$
				} else {
					buf.append(" extends "); //$NON-NLS-1$
				}
				String[] intfs= interfaces.toArray(new String[interfaces.size()]);
				ITypeBinding[] bindings;
				if (fCurrType != null) {
					bindings= TypeContextChecker.resolveSuperInterfaces(intfs, fCurrType, getSuperInterfacesStubTypeContext());
				} else {
					bindings= new ITypeBinding[intfs.length];
				}
				for (int i= 0; i <= last; i++) {
					ITypeBinding binding= bindings[i];
					if (binding != null) {
						buf.append(imports.addImport(binding));
					} else {
						buf.append(imports.addImport(intfs[i]));
					}
					if (i < last) {
						buf.append(',');
					}
				}
			}
		}
		
		private StubTypeContext getSuperClassStubTypeContext() {
			if (fSuperClassStubTypeContext == null) {
				String typeName;
				if (fCurrType != null) {
					typeName= getTypeName();
				} else {
					typeName= JavaTypeCompletionProcessor.DUMMY_CLASS_NAME;
				}
				fSuperClassStubTypeContext= TypeContextChecker.createSuperClassStubTypeContext(typeName, null, pack);
			}
			return fSuperClassStubTypeContext;
		}
		
		private StubTypeContext getSuperInterfacesStubTypeContext() {
			if (fSuperInterfaceStubTypeContext == null) {
				String typeName;
				if (fCurrType != null) {
					typeName= getTypeName();
				} else {
					typeName= JavaTypeCompletionProcessor.DUMMY_CLASS_NAME;
				}
				fSuperInterfaceStubTypeContext= TypeContextChecker.createSuperInterfaceStubTypeContext(typeName, null, pack);
			}
			return fSuperInterfaceStubTypeContext;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setInterfaces(List<String> interfaces) {
			this.interfaces = interfaces;
		}

		public void setfTypeKind(int fTypeKind) {
			this.fTypeKind = fTypeKind;
		}
		
		public void setFields(JavaField[] fields) {
			this.fields = fields;
		}
		
		public void setSuperclass(String superclass) {
			this.superclass = superclass;
		}

		/*
		 * @see NewClassWizardPage#createTypeMembers
		 */
		protected void createTypeMembers(IType type, ImportRewrite imports, IProgressMonitor monitor) throws CoreException {
			boolean doMain= false;
			boolean doConstr= true;
			boolean doInherited= false;
			createInheritedMethods(type, doConstr, doInherited, imports, new SubProgressMonitor(monitor, 1));

			if (monitor != null) {
				monitor.done();
			}
		}
		
		protected IMethod[] createInheritedMethods(IType type, boolean doConstructors, boolean doUnimplementedMethods, ImportRewrite imports, IProgressMonitor monitor) throws CoreException {
			final ICompilationUnit cu= type.getCompilationUnit();
			JavaModelUtil.reconcile(cu);
			IMethod[] typeMethods= type.getMethods();
			Set<String> handleIds= new HashSet<String>(typeMethods.length);
			for (int index= 0; index < typeMethods.length; index++)
				handleIds.add(typeMethods[index].getHandleIdentifier());
			ArrayList<IMethod> newMethods= new ArrayList<IMethod>();
			
			CodeGenerationSettings settings= JavaPreferencesSettings.getCodeGenerationSettings(type.getJavaProject());
			settings.createComments= isAddComments();
			ASTParser parser= ASTParser.newParser(ASTProvider.SHARED_AST_LEVEL);
			parser.setResolveBindings(true);
			parser.setSource(cu);
			CompilationUnit unit= (CompilationUnit) parser.createAST(new SubProgressMonitor(monitor, 1));
			final ITypeBinding binding= ASTNodes.getTypeBinding(unit, type);
			if (binding != null) {
				if (doUnimplementedMethods) {
					AddUnimplementedMethodsOperation operation= new AddUnimplementedMethodsOperation(unit, binding, null, -1, false, true, false);
					operation.setCreateComments(isAddComments());
					operation.run(monitor);
					createImports(imports, operation.getCreatedImports());
				}
				if (doConstructors) {
					AddUnimplementedConstructorsOperation operation= new AddUnimplementedConstructorsOperation(unit, binding, null, -1, false, true, false);
					operation.setOmitSuper(true);
					operation.setCreateComments(isAddComments());
					operation.run(monitor);
					createImports(imports, operation.getCreatedImports());
				}
			}
			JavaModelUtil.reconcile(cu);
			typeMethods= type.getMethods();
			for (int index= 0; index < typeMethods.length; index++)
				if (!handleIds.contains(typeMethods[index].getHandleIdentifier()))
					newMethods.add(typeMethods[index]);
			IMethod[] methods= new IMethod[newMethods.size()];
			newMethods.toArray(methods);
			return methods;
		}
		
		public boolean isAddComments() {
			/*
			if (fUseAddCommentButtonValue) {
				return fAddCommentButton.isSelected();
			}
			return StubUtility.doAddComments(getJavaProject());
			*/
			return true;
		}
		
		private void createImports(ImportRewrite imports, String[] createdImports) {
			for (int index= 0; index < createdImports.length; index++)
				imports.addImport(createdImports[index]);
		}
		
		private void createFields(IType createdType, ICompilationUnit cu, String lineDelimiter, IProgressMonitor monitor)throws JavaModelException, CoreException{
			
			for (int i = 0; i < fields.length; i++) {
				
				
				IField field = createdType.createField(fields[i].getFieldContents(i), null, false, monitor);
				
				
				String methodName = GetterSetterUtil.getGetterName(field, null);
				
				String contents = CodeGeneration.getGetterComment(cu, typeName, methodName, field.getElementName(), field.getTypeSignature(), field.getElementName(), lineDelimiter);
				
				contents = contents + lineDelimiter + GetterSetterUtil.getGetterStub(field, methodName, false, Flags.AccPublic);
				
				createdType.createMethod(contents, null, false, monitor);
				
				
				//----------------- setter
				methodName = GetterSetterUtil.getSetterName(field, null);
				
				contents = CodeGeneration.getSetterComment(cu, typeName, methodName, field.getElementName(), field.getTypeSignature(), field.getElementName(), field.getElementName(), lineDelimiter);
				
				contents = contents + lineDelimiter + GetterSetterUtil.getSetterStub(field, methodName, false, Flags.AccPublic);
				
				createdType.createMethod(contents, null, false, monitor);
				
			}
			
		}
		

}
