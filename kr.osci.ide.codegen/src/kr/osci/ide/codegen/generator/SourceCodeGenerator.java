package kr.osci.ide.codegen.generator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.corext.codemanipulation.GetterSetterUtil;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import kr.osci.ide.codegen.utils.PluginUtil;
import kr.osci.ide.codegen.utils.StringUtil;
import kr.osci.ide.codegen.utils.XMLUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class SourceCodeGenerator {

	public static final int RESULT_NOTHING = 0;
	public static final int RESULT_CREATED = 1;
	public static final int RESULT_MODIFIED = 2;
	
	private static SourceCodeGenerator INSTANCE ;
	public static final String GENERICCRITERIA = "GenericCriteria";
	public static final String XPATH_TARGET_XML = "//bean[@id='target.xml']/property/list";
	
	/*
	public static final String SUB_PACKAGE_SQL_MAPPER = "";// ex ".dao.mapper"
	public static final String SUB_PACKAGE_DTO = ""; // ex ".dto"
	public static final String SUB_PACKAGE_DAO = ""; // ex ".dao"
	public static final String SUB_PACKAGE_SERVICE = ""; // ex ".service"
	public static final String SUB_PACKAGE_CONTROLLER = ""; // ex ".controller"
	
	public static final String SUFFIX_MAPPER_FILE = "Mapper.xsql";
	public static final String SUFFIX_DTO = "Dto";
	*/
	
	public static final String PREFER_PROP_DTO_PARENT = "dtoParent";
	public static final String PREFER_PROP_SUFFIX_MAPPER = "suffixMapper";
	public static final String PREFER_PROP_SUFFIX_DTO = "suffixDto";
	public static final String PREFER_PROP_SUFFIX_DAO = "suffixDao";
	
	public static final String PREFER_PROP_SUB_PACKAGE_MAPPER = "subPackageMapper";
	public static final String PREFER_PROP_SUB_PACKAGE_DTO = "subPackageDto";
	public static final String PREFER_PROP_SUB_PACKAGE_DAO = "subPackageDao";
	public static final String PREFER_PROP_SUB_PACKAGE_SERVICE = "subPackageService";
	public static final String PREFER_PROP_SUB_PACKAGE_CONTROLLER = "subPackageController";
	
	private Configuration cfg;
	
	private SourceCodeGenerator() {
		
	}
	
	private void init() throws IOException{
		cfg = new Configuration();

        //cfg.setDirectoryForTemplateLoading(new File("/where/you/store/templates"));
		cfg.setTemplateLoader(new ClassTemplateLoader(SourceCodeGenerator.class, "templates"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        //cfg.setIncompatibleImprovements(new Version(2, 3, 20));
	}
	
	public static SourceCodeGenerator getInstance(){
		
		if(INSTANCE == null){
			INSTANCE = new SourceCodeGenerator();
			
			try{
				INSTANCE.init();
			}catch(IOException e){
				PluginUtil.logError(e);
			}
		}
		
		return INSTANCE;
	}
	
	public boolean generate(String templateFileName, Object dataModel, Writer out) throws IOException{
		
		/* Get the template */
        Template temp = cfg.getTemplate(templateFileName);

        try{
        	temp.process(dataModel, out);
        	
        	return true;
        }catch(TemplateException e){
        	PluginUtil.logError(e);
        	return false;
        }
	}
	
	
	public static IFile createDomainClass(IPackageFragmentRoot pkgFragmentRoot, String packageName, String domainClassName, String comment) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("packageName", packageName);
        dataModel.put("domainClass", domainClassName);
        dataModel.put("comment", formatComment(comment));
        
        IFile iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ ".domain", domainClassName + ".java");
        
        generateSourceFile(dataModel, iFile, "DomainTemplate.ftl", null);
        
        return iFile;
        
	}
	
	public static void createDomainList(IPackageFragmentRoot pkgFragmentRoot, String packageName, String domainClassName, String comment) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("packageName", packageName);
        dataModel.put("domainClass", domainClassName);
        dataModel.put("domainClassLower", domainClassName.toLowerCase());
        dataModel.put("comment", formatComment(comment));
        
        IFile iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ ".domain", domainClassName + "List.java");
        
        generateSourceFile(dataModel, iFile, "DomainListTemplate.ftl", null);
        
	}
	
	public static int createDomainMppingFile(IPackageFragmentRoot pkgFragmentRoot, String userCode, final String packageName, final String domainClassName) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("userCode", userCode);
        dataModel.put("packageName", packageName);
        dataModel.put("domainClass", domainClassName);
        
        IFile iFile = PluginUtil.getUserDomainMappingFile(pkgFragmentRoot, userCode );
        
        return generateSourceFile(dataModel, iFile, "DomainMappingTemplate.ftl", new ExistFileHandler(){

			@Override
			public int process(Map dataModel, IFile iFile) {
				return addDominMapping(iFile, packageName + ".domain." + domainClassName, true);
			}
        });
	}
	
	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * @param pkgFragmentRoot
	 * @param springServletFile
	 * @param userCode
	 * @return 추가여부
	 * @throws Exception
	 */
	public static boolean addUserMappingId(IPackageFragmentRoot pkgFragmentRoot, String springServletFile, String userCode)throws Exception{
		
		String prjName = pkgFragmentRoot.getJavaProject().getElementName();
		boolean isExist = false;
		
		Path path = new Path(prjName + "/src/main/webapp" + springServletFile);
	    IFile spingFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	    
	    Document doc = XMLUtil.parse(spingFile.getContents(), "EUC-KR");
	    
	    // xpath 생성
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        Element el = null;
		try{
			el = (Element)xpath.evaluate(XPATH_TARGET_XML, doc, XPathConstants.NODE);
			
			NodeList refList = el.getElementsByTagName("ref");
			for (int i = 0; i < refList.getLength(); i++) {
				//System.out.println(((Element)refList.item(i)).getAttribute("bean"));
				
				if(((Element)refList.item(i)).getAttribute("bean").startsWith(userCode)){
					isExist = true;
					break;
				}
			}
			
			if(!isExist){
				Element refEl = doc.createElement("ref");
				refEl.setAttribute("bean", userCode + ".mapping");
				el.appendChild(refEl);
				
				// save doc.
				XMLUtil.serialize(doc, spingFile, "EUC-KR");
			}
			
			return !isExist;
			
		}catch(XPathExpressionException e){
			throw new RuntimeException(e);
		}

	    //System.out.println( file.getRawLocation().toOSString() );
	}
	
	public static IFile createSqlMapperFile(IPackageFragmentRoot pkgFragmentRoot, String packageName, String domainClassName) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("namespace", domainClassName);
        
        IFile iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ ".dao.mapper", domainClassName + "Mapper.xml");
        
        generateSourceFile(dataModel, iFile, "MyBatisMapperTemplate.ftl", null);
        
        return iFile;
	}
	
	public static IFile createSqlMapperFile(IPackageFragmentRoot pkgFragmentRoot, String packageName, CodeModel model, IProgressMonitor monitor) throws IOException, CoreException{
		
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		monitor.beginTask("SQL Mapper 생성.", 1);
		IFile iFile = null;
		try{
			
			String subPackageMapper = PluginUtil.getPreferenceProperty(PREFER_PROP_SUB_PACKAGE_MAPPER);
			String suffixMapper = PluginUtil.getPreferenceProperty(PREFER_PROP_SUFFIX_MAPPER);
			
			/* Create a data-model */
	        Map<String, Object> dataModel = new HashMap<String, Object>();
	        dataModel.put("model", model);
	        
	        iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName + subPackageMapper, model.getDomainName() + suffixMapper + ".xml");
	        
	        generateSourceFile(dataModel, iFile, "MysqlOscMapperTemplate.ftl", null);
	        monitor.worked(1);
	        
		}finally{
			monitor.done();
		}
        return iFile;
	}
	
	public static IFile createDao(IPackageFragmentRoot pkgFragmentRoot, String packageName, CodeModel model, IProgressMonitor monitor) throws IOException, CoreException{
		
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		monitor.beginTask("Dao 생성.", 1);
		IFile iFile = null;
		try{
			String subPackageDao = PluginUtil.getPreferenceProperty(PREFER_PROP_SUB_PACKAGE_DAO);
			String suffixDao = PluginUtil.getPreferenceProperty(PREFER_PROP_SUFFIX_DAO);
			
			/* Create a data-model */
			model.setClsSuffix(suffixDao);
	        Map<String, CodeModel> dataModel = new HashMap<String, CodeModel>();
	        dataModel.put("model", model);
	        
	        iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ subPackageDao, model.getDomainName() + suffixDao + ".java");
	        
	        generateSourceFile(dataModel, iFile, "OSCDaoTemplate.ftl", null);
        
		}finally{
			monitor.done();
		}
        return iFile;
	}
	
	public static IFile createService(IPackageFragmentRoot pkgFragmentRoot, String packageName, CodeModel model, IProgressMonitor monitor) throws IOException, CoreException{
		
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		monitor.beginTask("Service 생성.", 1);
		IFile iFile = null;
		try{
			
			String subPackageService = PluginUtil.getPreferenceProperty(PREFER_PROP_SUB_PACKAGE_SERVICE);
			
			/* Create a data-model */
	        Map<String, CodeModel> dataModel = new HashMap<String, CodeModel>();
	        dataModel.put("model", model);
	        
	        iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ subPackageService, model.getDomainName() + "Service.java");
	        
	        generateSourceFile(dataModel, iFile, "OSCServiceTemplate.ftl", null);
        
		}finally{
			monitor.done();
		}
        return iFile;
	}
	
	public static IFile createController(IPackageFragmentRoot pkgFragmentRoot, String packageName, CodeModel model, IProgressMonitor monitor) throws IOException, CoreException{
		
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		monitor.beginTask("Controller 생성.", 1);
		IFile iFile = null;
		try{
			String subPackageController = PluginUtil.getPreferenceProperty(PREFER_PROP_SUB_PACKAGE_CONTROLLER);
			
			/* Create a data-model */
	        Map<String, CodeModel> dataModel = new HashMap<String, CodeModel>();
	        dataModel.put("model", model);
	        
	        iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ subPackageController, model.getDomainName() + "Controller.java");
	        
	        generateSourceFile(dataModel, iFile, "OSCControllerTemplate.ftl", null);
        
		}finally{
			monitor.done();
		}
        return iFile;
	}
	
	public static IFile createServiceInterface(IPackageFragmentRoot pkgFragmentRoot, String packageName, String domainClassName, String comment) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("packageName", packageName);
        dataModel.put("domainClass", domainClassName);
        dataModel.put("comment", formatComment(comment));
        
        IFile iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ ".service", domainClassName + "Service.java");
        
        generateSourceFile(dataModel, iFile, "ServiceTemplate.ftl", null);
        
        return iFile;
	}
	
	public static IFile createServiceImpl(IPackageFragmentRoot pkgFragmentRoot, String packageName, String domainClassName, String comment) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("packageName", packageName);
        dataModel.put("domainClass", domainClassName);
        dataModel.put("comment", formatComment(comment));
        
        IFile iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ ".service", domainClassName + "ServiceImpl.java");
        
        generateSourceFile(dataModel, iFile, "ServiceImplTemplate.ftl", null);
        
        return iFile;
	}
	
	public static IFile createControllerClass(IPackageFragmentRoot pkgFragmentRoot, String packageName, String domainClassName, String comment) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("packageName", packageName);
        dataModel.put("domainClass", domainClassName);
        dataModel.put("comment", formatComment(comment));
        
        IFile iFile = PluginUtil.getIFile(pkgFragmentRoot, packageName+ ".web", domainClassName + "Controller.java");
        
        generateSourceFile(dataModel, iFile, "ControllerTemplate.ftl", null);
        
        return iFile;
	}
	
	public static IFile createDataSetDomain(GenerateModel gModel, String dataSetClass, List<JavaField> fields) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("packageName", gModel.getPackageName());
        dataModel.put("domainClass", dataSetClass);
        dataModel.put("fields", fields);
        dataModel.put("comment", formatComment(gModel.getComment()));
        
        IFile iFile = PluginUtil.getIFile(gModel.getPkgFragmentRoot(), gModel.getPackageName()+ ".domain", dataSetClass +".java");
        
        generateSourceFile(dataModel, iFile, "DataSetDomainTemplate.ftl", null);
        
        // add DomainMapping
        IFile domainMappingConfig = PluginUtil.getUserDomainMappingFile(gModel.getResPkgFragmentRoot(), gModel.getUserCode());
        SourceCodeGenerator.addDominMapping(domainMappingConfig, gModel.getPackageName() + ".domain." + dataSetClass, false);
        
        return iFile;
	}
	
	public static IFile createDataSetDao(GenerateModel gModel, String dataSetClass, SqlStatement[] statements) throws IOException, CoreException{
		
		/* Create a data-model */
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("packageName", gModel.getPackageName());
        dataModel.put("domainClass", gModel.getDomainClassName());
        dataModel.put("dataSetClass", dataSetClass);
        dataModel.put("statements", statements);
        dataModel.put("comment", formatComment(gModel.getComment()));
        
        IFile iFile = PluginUtil.getIFile(gModel.getPkgFragmentRoot(), gModel.getPackageName()+ ".dao", dataSetClass +"Dao.java");
        
        generateSourceFile(dataModel, iFile, "DataSetDaoTemplate.ftl", null);
        
        return iFile;
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dataModel
	 * @param iFile
	 * @param templateFileName
	 * @param exHandler
	 * @return 0: nothing, 1:create, 2:modify
	 * @throws IOException
	 * @throws CoreException
	 */
	private static int generateSourceFile(Map dataModel, IFile iFile, String templateFileName, ExistFileHandler exHandler) throws IOException, CoreException{
		
		if(!iFile.exists()){
        	Writer out = new StringWriter();
    		SourceCodeGenerator.getInstance().generate(templateFileName, dataModel, out);
    		
    		InputStream source = new ByteArrayInputStream(out.toString().getBytes("UTF-8"));
    		
    		iFile.create(source, true, null);
    		
    		return RESULT_CREATED;
        }else if(exHandler != null){
        	return exHandler.process(dataModel, iFile);
        }
		
		return RESULT_NOTHING;//nothing generate
	}
	

	public static void generateFieldWithMethod(IType type, String fieldName)throws CoreException{
		
		try{
			// create field
			IField field = type.createField("private String "+fieldName+";", null, false, null);
			
			
			String getterName = GetterSetterUtil.getGetterName(field, null);
			String setterName = GetterSetterUtil.getSetterName(field, null);
			
			// create getter
			String methodCode = formatJavaCode(type, GetterSetterUtil.getGetterStub(field, getterName, false, Flags.AccPublic));
			
			type.createMethod(methodCode, null, false, null);
			
			// create setter
			methodCode = formatJavaCode(type, GetterSetterUtil.getSetterStub(field, setterName, false, Flags.AccPublic));
			
			type.createMethod(methodCode, null, false, null);
			
		}catch(JavaModelException e){
			//ignore (allready exist field).
		}
		
	}
	
	public static void generateServiceDataSetCode(GenerateModel genModel, String dataSetClass, IProgressMonitor monitor)throws JavaModelException, PartInitException{
		
		String dataSet = genModel.getPackageName()+ ".domain." + dataSetClass;
		String dataSetFileName = StringUtil.convertFieldName(dataSetClass);
		
		//------------------ Service
		IFile ifile = PluginUtil.getIFile(genModel.getPkgFragmentRoot(), genModel.getPackageName()+ ".service", genModel.getDomainClassName() +"Service.java");
		CompilationUnit unit = (CompilationUnit)JavaCore.createCompilationUnitFrom(ifile);
		
		// add import
		unit.createImport(dataSet, null, monitor);
		
		// add method
		IType serviceType = unit.getType(genModel.getDomainClassName() +"Service");
		serviceType.createMethod("/**\n * @param param\n */\nvoid update"+dataSetClass+"("+dataSetClass+" param);", null, true, monitor);
		
		// file open
		PluginUtil.openEditor(ifile, monitor);
		
		//------------------ ServiceImpl
		ifile = PluginUtil.getIFile(genModel.getPkgFragmentRoot(), genModel.getPackageName()+ ".service", genModel.getDomainClassName() +"ServiceImpl.java");
		unit = (CompilationUnit)JavaCore.createCompilationUnitFrom(ifile);
		
		// add import
		unit.createImport(dataSet, null, monitor);
		unit.createImport(genModel.getPackageName()+ ".dao." + dataSetClass + "Dao", null, monitor);
		
		IType serviceImplType = unit.getType(genModel.getDomainClassName() +"ServiceImpl");
		
		// add dao field
		serviceImplType.createField("@Autowired\nprivate "+dataSetClass+"Dao "+dataSetFileName+"Dao;", null, true, monitor);
		
		// add method
		String methodStr = formatJavaCode(serviceImplType, "public void update"+dataSetClass+"("+dataSetClass+" param) { "+dataSetFileName+"Dao.update"+dataSetClass+"( param); }");
		serviceImplType.createMethod(methodStr, null, true, monitor);
		
		// file open
		PluginUtil.openEditor(ifile, monitor);
		
	}
	
	public static IFile generateControllerDataSetCode(GenerateModel genModel, String dataSetClass, IProgressMonitor monitor)throws JavaModelException, IOException{
		
		String dataSet = genModel.getPackageName()+ ".domain." + dataSetClass;
		
		IFile ifile = PluginUtil.getIFile(genModel.getPkgFragmentRoot(), genModel.getPackageName()+ ".web", genModel.getDomainClassName() +"Controller.java");
		
		CompilationUnit unit = (CompilationUnit)JavaCore.createCompilationUnitFrom(ifile);
		
		// add import
		unit.createImport(dataSet, null, monitor);
		
		
		IType controllerType = unit.getType(genModel.getDomainClassName() +"Controller");
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("dataSetClass", dataSetClass);
        dataModel.put("dataSetLower", dataSetClass.toLowerCase());
		
		Writer out = new StringWriter();
		SourceCodeGenerator.getInstance().generate("ControllerDataSetTemplate.ftl", dataModel, out);
		
		// add method
		controllerType.createMethod(out.toString(), null, true, monitor);
		
		return ifile;
	}
	
	
	
	private static String formatJavaCode(IType type, String contents){
		String delimiter= StubUtility.getLineDelimiterUsed(type);
		
		return CodeFormatterUtil.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, contents, 0, delimiter, type.getJavaProject());
	}
	
	/**
	 * <pre>
	 * *-mapping.xml 파일에 클래스를 등록한다.
	 * 이미 등록되어 있으면 생략.
	 * </pre>
	 * @param iFile
	 * @param domainClassName 패키지 포함 클래스명
	 * @param addList
	 */
	public static int addDominMapping(IFile iFile, String domainClassName, boolean addList){
		
		
		try{
			InputStream in = null;
			Document doc = null;
			try{
				in = iFile.getContents(true);
				
				doc = XMLUtil.parse( in );
			}finally{
				if(in != null){
					try{ in.close(); }catch(IOException e){}
				}
			}
			if(XMLUtil.hasValueByText(doc, domainClassName)){
				// 이미 등록되어 있는 도메인 클래스는 추가 생략..
				return RESULT_NOTHING;
			}
			
			//--------------- 추가..
			NodeList nodeList = doc.getElementsByTagName("util:list");
			
			Element el = doc.createElement("value");
			el.setTextContent(domainClassName);
			
			nodeList.item(0).appendChild(el);
			
			if(addList){
				el = doc.createElement("value");
				el.setTextContent(domainClassName + "List");
				
				nodeList.item(0).appendChild(el);
			}
			
			XMLUtil.serialize(doc, iFile, "EUC-KR");
			
			return RESULT_MODIFIED;
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	private static String formatComment(String comment){
		return comment.replaceAll("\r\n", "\r\n * ");
	}


}
