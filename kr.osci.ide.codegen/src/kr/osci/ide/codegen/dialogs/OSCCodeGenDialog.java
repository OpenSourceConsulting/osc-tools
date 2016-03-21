/**
 * 
 */
package kr.osci.ide.codegen.dialogs;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.osci.ide.codegen.Activator;
import kr.osci.ide.codegen.generator.CodeModel;
import kr.osci.ide.codegen.generator.GenerateModel;
import kr.osci.ide.codegen.generator.JavaClassCreator;
import kr.osci.ide.codegen.generator.JavaField;
import kr.osci.ide.codegen.generator.SourceCodeGenerator;
import kr.osci.ide.codegen.ui.JavaFieldLabelProvider;
import kr.osci.ide.codegen.utils.JDBCUtils;
import kr.osci.ide.codegen.utils.PluginUtil;
import kr.osci.ide.codegen.utils.StringUtil;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.util.JavaConventionsUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.dialogs.PackageSelectionDialog;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.preferences.PreferencesMessages;
import org.eclipse.jdt.internal.ui.refactoring.nls.SourceContainerDialog;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.ui.wizards.NewPackageWizardPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * <pre>
 * 家胶内靛 积己扁.
 * </pre>
 * 
 * @author Bongjin Kwon
 *
 */
public class OSCCodeGenDialog extends TitleAreaDialog {
	
	private static final String DB_DRIVER_KEY = "dbDriverClassName";
	private static final String DB_URL_KEY = "dbURL";
	private static final String DB_USER_KEY = "dbUser";
	
	private static final String DB_MYSQL = "com.mysql.jdbc.Driver";
	private static final String DB_ORACLE = "oracle.jdbc.OracleDriver";

	
	private NewPackageWizardPage pkgCreator = new NewPackageWizardPage();
	private IPackageFragmentRoot pkgroot;// java source folder.
	private Text pkgText;
	private SelectionButtonDialogFieldGroup codeTypes;
	
	private CheckboxTableViewer tablesViewer;
	private CheckboxTableViewer columnsViewer;
	
	private GenerateModel genModel = new GenerateModel();
	
	private Combo driverCombo;
	private Text dbUrlText;
	private Text dbUserText;
	private Text dbPassText;
	
	private String selectedTableName;
	
	/**
	 * @param parentShell
	 */
	public OSCCodeGenDialog(Shell parentShell, IStructuredSelection selection) {
		super(parentShell);
		
		//------- modeless
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE);
		setBlockOnOpen(false);
		
		if(selection.getFirstElement() instanceof IPackageFragmentRoot || selection.getFirstElement() instanceof IPackageFragment){
			pkgCreator.init(selection);
			pkgroot = pkgCreator.getPackageFragmentRoot();
		}
		
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		
		setTitle("OSC Code Generator");
		setMessage("OSC CRUD 家胶 内靛 积己扁");
 
		Composite composite = (Composite) super.createDialogArea(parent);
 
		//composite.getShell().setText("HiWAY Code Generator");
 

		Composite container = new Composite(composite, SWT.NONE);
	    //container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
	    GridLayout layout = new GridLayout(3, false);
	    
	    GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
	    layoutData.heightHint = 400;
	    layoutData.widthHint = 550;
	    
	    container.setLayoutData(layoutData);
	    container.setLayout(layout);
 
		draw(container); // Contents of Dialog
 
		return composite;
	}
 
	private void draw(Composite composite) {
 
		 
		final List<IPackageFragmentRoot> consideredRoots = new ArrayList<IPackageFragmentRoot>();
		GridData fillGridData = new GridData();
		fillGridData.grabExcessHorizontalSpace = true;
		fillGridData.horizontalAlignment = GridData.FILL;
		
		// Project
		createProjectField(composite, fillGridData, consideredRoots);
		
		// Package
		createPackageField(composite, fillGridData, consideredRoots);
		

		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gridData.heightHint = 25;
		
		
		//------------------------- code types (CheckBox)
		String[] buttonNames = new String[]{"SQL Mapper","Dto","Dao","Service","Controller","JPA","Bootstrap"};
		codeTypes = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, buttonNames.length);
		codeTypes.setLabelText("Code Types: ");
		
		LayoutUtil.setHorizontalSpan(codeTypes.getLabelControl(composite), 1);

		Control control= codeTypes.getSelectionButtonsGroup(composite);
		control.setLayoutData(gridData);
		
		((GridLayout)((Composite)control).getLayout()).makeColumnsEqualWidth = false;
		
		for (int i = 0; i < buttonNames.length; i++) {
			codeTypes.setSelection(i, true);
		}
		
		
		
		//--------------------- db 
		
		Label dbDriverLabel = new Label(composite, SWT.LEFT);
		dbDriverLabel.setText("DB Driver: ");
		
		driverCombo = new Combo(composite, SWT.READ_ONLY);
		driverCombo.setLayoutData(gridData);
		driverCombo.add(DB_MYSQL);
		driverCombo.add(DB_ORACLE);
		driverCombo.select(0);
		
		final IDialogSettings settigs = Activator.getDefault().getDialogSettings();
		String dbDriver = settigs.get(DB_DRIVER_KEY);
		
		if(dbDriver != null){
			driverCombo.setText(dbDriver);
		}
		
		driverCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				settigs.put(DB_DRIVER_KEY, driverCombo.getText());
			}

		});
		
		
		new Label(composite, SWT.LEFT).setText("DB URL: ");
		
		dbUrlText = new Text(composite, SWT.BORDER);
		dbUrlText.setLayoutData(gridData);
		
		String dbURL = settigs.get(DB_URL_KEY);
		if(dbURL != null){
			dbUrlText.setText(dbURL);
		}
		
		new Label(composite, SWT.LEFT).setText("DB UserName: ");
		
		dbUserText = new Text(composite, SWT.BORDER);
		dbUserText.setLayoutData(gridData);
		
		String dbUser = settigs.get(DB_USER_KEY);
		if(dbUser != null){
			dbUserText.setText(dbUser);
		}
		
		
		new Label(composite, SWT.LEFT).setText("DB Password: ");;
		
		dbPassText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		dbPassText.setLayoutData(gridData);
		
		Button loadTableBtn = new Button(composite, SWT.PUSH);
		loadTableBtn.setText("connect");
		loadTableBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				final String driver = driverCombo.getText();
				final String dburl = dbUrlText.getText();
				final String user = dbUserText.getText();
				final String pass = dbPassText.getText();
				
				settigs.put(DB_URL_KEY, dburl);
				settigs.put(DB_USER_KEY, user);
				
				columnsViewer.getTable().removeAll();
				
				Job job = new Job("get tables") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						
						
						try {
							//Thread.sleep(5000);
							Connection conn = null;
							try{
								conn = JDBCUtils.getSimpleConnection(driver, dburl, user, pass);
								
								final String[] tables = JDBCUtils.getTables(conn);
								Display.getDefault().syncExec(new Runnable() {

									@Override
									public void run() {
										tablesViewer.setInput(tables);
										//tablesViewer.setAllChecked(true);
									}
								});
							
							}finally{
								JDBCUtils.close(conn);
							}

							return Status.OK_STATUS;

						} catch (Exception ex) {

							return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "db connection fail.", ex);
						}
					}

				};

				job.setUser(true);
				//job.setProperty(IProgressConstants2.PROPERTY_IN_DIALOG, true);
				job.schedule();
				
			}

		});
		
		
		
		Composite container = new Composite(composite, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		
		GridData gridData3 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gridData3.horizontalSpan = 3;
		gridData3.grabExcessVerticalSpace = true;
		
		container.setLayoutData(gridData3);
		
		
		Group group = new Group(container, SWT.NULL);
		group.setText("Tables");
		group.setLayout(new FillLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		createTableList(group);
		
		Group group2 = new Group(container, SWT.NULL);
		group2.setText("Colums");
		group2.setLayout(new FillLayout());
		group2.setLayoutData(group.getLayoutData());
		
		createColumnList(group2);
	
	}

	@Override
	protected boolean isResizable() {
		
		return true;
	}
	
	private void createProjectField(Composite composite, GridData fillGridData, final List<IPackageFragmentRoot> consideredRoots){
		Label prjLabel = new Label(composite, SWT.LEFT);
		prjLabel.setText("Source folder: ");
		
		final Text prjText = new Text(composite, SWT.BORDER);
		prjText.setLayoutData(fillGridData);
		
		if(pkgCreator.getPackageFragmentRoot() != null){
			pkgroot = pkgCreator.getPackageFragmentRoot();
			
			prjText.setText(getPackageRootPath(pkgroot));
		}
		
		
		Button selProject = new Button(composite, SWT.PUSH);
		selProject.setText("Browse...");
		
		
		selProject.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				pkgroot = SourceContainerDialog.getSourceContainer(getShell(), ResourcesPlugin.getWorkspace().getRoot(), null);
				
				if(pkgroot != null){
					consideredRoots.add(pkgroot);
					prjText.setText(getPackageRootPath(pkgroot));
					
					pkgCreator.setPackageFragmentRoot(pkgroot, false);
					
				}
				
			}
			
		});
	}
	
	private void createPackageField(Composite composite, GridData fillGridData, final List<IPackageFragmentRoot> consideredRoots){
		Label pkgLabel = new Label(composite, SWT.NONE);
		pkgLabel.setText("Package: ");
		
		pkgText = new Text(composite, SWT.BORDER);
		pkgText.setLayoutData(fillGridData);
		
		if(pkgCreator.getPackageText() != null){
			pkgText.setText(pkgCreator.getPackageText());
		}
		
		
		pkgText.addKeyListener(new KeyAdapter(){

			@Override
			public void keyReleased(KeyEvent e) {
				
				IStatus packageStatus= getPackageStatus(pkgText.getText());
				
				updateStatus(packageStatus);
				
				if(packageStatus.getSeverity() == IStatus.OK){
					pkgCreator.setPackageText(pkgText.getText(), false);
				}
			}
			
		});

		
		Button selPkg = new Button(composite, SWT.PUSH);
		selPkg.setText("Browse...");
 
		
		
		selPkg.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IRunnableContext context= new BusyIndicatorRunnableContext();
				IJavaSearchScope scope= SearchEngine.createJavaSearchScope(consideredRoots.toArray(new IJavaElement[consideredRoots.size()]));//SearchEngine.createWorkspaceScope();
				int style= PackageSelectionDialog.F_REMOVE_DUPLICATES | PackageSelectionDialog.F_SHOW_PARENTS | PackageSelectionDialog.F_HIDE_DEFAULT_PACKAGE;
				PackageSelectionDialog dialog= new PackageSelectionDialog(getShell(), context, style, scope);
				//dialog.setFilter("javaprj1");
				dialog.setIgnoreCase(false);
				dialog.setTitle(PreferencesMessages.ImportOrganizeInputDialog_ChoosePackageDialog_title);
				dialog.setMessage(PreferencesMessages.ImportOrganizeInputDialog_ChoosePackageDialog_description);
				dialog.setEmptyListMessage(PreferencesMessages.ImportOrganizeInputDialog_ChoosePackageDialog_empty);
				if (dialog.open() == Window.OK) {
					IPackageFragment res= (IPackageFragment) dialog.getFirstResult();
					pkgText.setText(res.getElementName());
					
					pkgCreator.setPackageText(res.getElementName(), false);
				}
			}
			
		});
	}
	
	
	private void createTableList(Group group){
		Table table= new Table(group, SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		//table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);


		TableColumn column1= new TableColumn(table, SWT.NONE);
		column1.setText("Table Name");
		column1.setWidth(150);

		
		tablesViewer = new CheckboxTableViewer(table);
		tablesViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		tablesViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				
				selectedTableName = (String)sel.getFirstElement();
				
				StatusManager.getManager().handle(new Status(IStatus.INFO, Activator.PLUGIN_ID, "select : " + selectedTableName));
				
				//tablesViewer.setCheckedElements(new String[]{selectedTableName});
				
				final String driver = driverCombo.getText();
				final String dburl = dbUrlText.getText();
				final String user = dbUserText.getText();
				final String pass = dbPassText.getText();
				
				Job job = new Job("get columns..") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							
							Connection conn = null;
							try{
								conn = JDBCUtils.getSimpleConnection(driver, dburl, user, pass);
								
								final List<JavaField> fields = JDBCUtils.getColumns(conn, selectedTableName);
								Display.getDefault().syncExec(new Runnable() {

									@Override
									public void run() {
										try{
											columnsViewer.setInput(fields.toArray(new JavaField[0]));
											columnsViewer.setAllChecked(true);
										}catch(Exception ex){
											throw new RuntimeException(ex);
										}
									}
								});
							
							}finally{
								JDBCUtils.close(conn);
							}

							return Status.OK_STATUS;

						} catch (Exception ex) {

							return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "db error.", ex);
						}
					}

				};

				job.setUser(true);
				job.schedule();
				
			}
		});
	}

	private void createColumnList(Group group){
		Table table= new Table(group, SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		//table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);


		TableColumn column1= new TableColumn(table, SWT.NONE);
		column1.setText("Column");
		column1.setWidth(150);
		
		TableColumn column2= new TableColumn(table, SWT.NONE);
		column2.setText("Type");
		column2.setWidth(100);
		
		TableColumn column3= new TableColumn(table, SWT.NONE);
		column3.setText("Scale");
		column3.setWidth(80);

		
		columnsViewer = new CheckboxTableViewer(table);
		columnsViewer.setContentProvider(ArrayContentProvider.getInstance());
		columnsViewer.setLabelProvider(new JavaFieldLabelProvider());
		columnsViewer.setAllChecked(true);
	}
	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		
		createCustomButton(parent, "Create").addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (selectedTableName == null) {
					MessageDialog.openWarning(
				            getShell(),
				            "Warning",
				            "积己且 抛捞喉阑 急琶秦林技夸.");
					return;
				}
				
				final String driver = driverCombo.getText();
				final String dburl = dbUrlText.getText();
				final String user = dbUserText.getText();
				final String pass = dbPassText.getText();
				
				final String dtoSuperClass = PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_DTO_PARENT);
				final String subPackageDto = PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUB_PACKAGE_DTO);
				final String suffixDto = PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUFFIX_DTO);
				final String suffixDao = PluginUtil.getPreferenceProperty(SourceCodeGenerator.PREFER_PROP_SUFFIX_DAO);
				
				final IPackageFragmentRoot root = pkgCreator.getPackageFragmentRoot();
				final String dtoTypeName = StringUtil.convertUnderscoreNameToClassName(selectedTableName)+suffixDto ;
				final Object[] columns = columnsViewer.getCheckedElements();
				
				
				
				Job job = new Job("CRUD Code 积己.") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						
						monitor.beginTask("CRUD Code 积己.", 5);
						try {
							JavaField.useJPA = codeTypes.isSelected(5);
							JavaField[] fields = Arrays.copyOf(columns, columns.length, new JavaField[0].getClass());
							
							CodeModel codeModel = new CodeModel(pkgCreator.getPackageText(), dtoTypeName, selectedTableName, suffixDao, suffixDto);
							codeModel.setFields(fields);
							
							if(codeTypes.isSelected(1)){
								String dtoPackageName = pkgCreator.getPackageText() + subPackageDto;
								
								IPackageFragment pack = root.getPackageFragment(dtoPackageName);;
								if(!pack.exists()){
									pack = root.createPackageFragment(dtoPackageName, true, monitor);
								}
								
								JavaClassCreator creator = new JavaClassCreator(root, pack, dtoTypeName);
								
								creator.setSuperclass(dtoSuperClass);
								creator.setFields( fields );
								
								creator.createType(new SubProgressMonitor(monitor, 1), selectedTableName);
							}
							
							
							Connection conn = null;
							try{
								
								if(codeTypes.isSelected(0)){
									
									//-------------- create sql mapper xml
									conn = JDBCUtils.getSimpleConnection(driver, dburl, user, pass);
									
									List<JavaField> pks = JDBCUtils.getPrimaryKeys(conn, selectedTableName);
									
									codeModel.setPks(pks);
									
									SourceCodeGenerator.createSqlMapperFile(root, pkgCreator.getPackageText(), codeModel, new SubProgressMonitor(monitor, 1));
								}
								
								if(codeTypes.isSelected(2)){
									SourceCodeGenerator.createDao(root, pkgCreator.getPackageText(), codeModel, new SubProgressMonitor(monitor, 1));
								}
								
								if(codeTypes.isSelected(3)){
									SourceCodeGenerator.createService(root, pkgCreator.getPackageText(), codeModel, new SubProgressMonitor(monitor, 1));
								}
								
								if(codeTypes.isSelected(4)){
									SourceCodeGenerator.createController(root, pkgCreator.getPackageText(), codeModel, new SubProgressMonitor(monitor, 1));
								}
								
								if(codeTypes.isSelected(6)){
									SourceCodeGenerator.createBootstrapFile(root, pkgCreator.getPackageText(), codeModel, new SubProgressMonitor(monitor, 1));
								}
								
							}finally{
								JDBCUtils.close(conn);
							}
							

							PluginUtil.openInformation(getShell(), "舅覆", "CRUD Code 积己肯丰.");

							return Status.OK_STATUS;

						} catch (Exception ex) {

							return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "CRUD Code 积己 角菩", ex);
						} finally {
							monitor.done();
						}
					}


				};

				job.setUser(true);
				job.schedule();
			}
		});
		
		super.createButtonsForButtonBar(parent);
	}
	

	protected Button createCustomButton(Composite parent, String label) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//buttonPressed(((Integer) event.widget.getData()).intValue());
			}
		});

		setButtonLayoutData(button);
		return button;
	}
	
	// ----------- package name validation ----------

	private IStatus validatePackageName(String text) {
		IJavaProject project= getJavaProject();
		if (project == null || !project.exists()) {
			return JavaConventions.validatePackageName(text, JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);
		}
		return JavaConventionsUtil.validatePackageName(text, project);
	}
	
	/**
	 * Returns the Java project of the currently selected package fragment root or <code>null</code>
	 * if no package fragment root is configured. 
	 *
	 * @return The current Java project or <code>null</code>.
	 * @see org.eclipse.jdt.ui.wizards.NewPackageWizardPage#getJavaProject
	 */
	public IJavaProject getJavaProject() {
		//IPackageFragmentRoot root= getPackageFragmentRoot();
		if (pkgroot != null) {
			return pkgroot.getJavaProject();
		}
		return null;
	}
	
	
	/**
	 * Validates the package name and returns the status of the validation.
	 * 
	 * @param packName the package name
	 * 
	 * @return the status of the validation
	 * @see org.eclipse.jdt.ui.wizards.NewPackageWizardPage#getPackageStatus
	 */
	private IStatus getPackageStatus(String packName) {
		StatusInfo status= new StatusInfo();
		if (packName.length() > 0) {
			IStatus val= validatePackageName(packName);
			if (val.getSeverity() == IStatus.ERROR) {
				status.setError(Messages.format(NewWizardMessages.NewPackageWizardPage_error_InvalidPackageName, val.getMessage()));
				return status;
			} else if (val.getSeverity() == IStatus.WARNING) {
				status.setWarning(Messages.format(NewWizardMessages.NewPackageWizardPage_warning_DiscouragedPackageName, val.getMessage()));
			}
		} else {
			status.setError(NewWizardMessages.NewPackageWizardPage_error_EnterName);
			return status;
		}

		// removed below logic
		
		return status;
	}
	
	
	private void updateStatus(IStatus status){
		
		String message= status.getMessage();
		if (message != null && message.length() == 0) {
			message= null;
		}
		switch (status.getSeverity()) {
			case IStatus.OK:
				setMessage(message, IMessageProvider.NONE);
				setErrorMessage(null);
				break;
			case IStatus.WARNING:
				setMessage(message, IMessageProvider.WARNING);
				setErrorMessage(null);
				break;
			case IStatus.INFO:
				setMessage(message, IMessageProvider.INFORMATION);
				setErrorMessage(null);
				break;
			default:
				setMessage(null);
				setErrorMessage(message);
				break;
		}
		
	}
	
	
	private String getPackageRootPath(IPackageFragmentRoot pkgroot){
		return pkgroot.getPath().makeRelative().toString();
	}
	
}
