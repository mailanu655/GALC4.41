package com.honda.galc.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import bsh.EvalError;
import bsh.Interpreter;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.TagNames;

/**
 * 
 * <h3>ScriptInterpreter</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BeanShellInterpreter is a wrapper class for BeanShell </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Apr 11, 2011
 *
 */

public class BeanShellInterpreter implements IScriptInterpreter{
	
	public static final String COLLECTOR = "collector";
	public static final String PRODUCT = "product";
	public static final String PRODUCTSPEC = "productSpec";
	public static final String INTERPRETER = "interpreter";
	public static final String DAO = "dao";
	public static final String LOGGER = "logger";
	public static final String HELPER = "helper";
	public static final String TASKS = "tasks";
	public static final String CONTEXT = "context";

	public static final String STATEMENT_DELIMITER = ";";
	private static final String WHITESPACE = " \t\r\n";
	private static final String EQUAL = "=";
	public static final String PARSE_DELIMITER = WHITESPACE + EQUAL + STATEMENT_DELIMITER;
	
	protected Interpreter interpreter;
	protected Map<Object, Object> context;
	protected Logger logger;
	
	private String script;
	private Map<String, String> scripts;
	

	public BeanShellInterpreter(Logger logger) {
		this.logger = logger;
	}
	
	@SuppressWarnings("unchecked")
	public BeanShellInterpreter(Map context, Logger logger) {
		this.context = context;
		this.logger = logger;
	}

	public void process(Map<String, String> scripts) throws Exception{

		try {
			this.scripts = scripts;
			this.interpreter = getInterpreter();
			
			beforeProcess();
			processScripts();
			
		} catch (Exception e) {
			logger.error(e, "exception to process statement:" + script);
			throw e;
		}

	}
	
	public void process(String script) throws Exception{

		try {
			this.script = script;
			this.interpreter = getInterpreter();
			
			beforeProcess();
			
			interpreter.eval(script);
			afterEvaluate();
			
			//afterProcess();
		} catch (Exception e) {
			logger.error(e, "exception to process statement:" + script);
			throw e;
		}

	}

	
	public void processScripts() throws EvalError {
		
		if(scripts == null) return;
		
		Map<String, String> scriptsCopy = new HashMap<String, String>();
		//validate the script key are all valid to avoid infinite loop
		for(Object obj : scripts.keySet()){
			if(!StringUtils.isNumeric(obj.toString().trim()) && 
					!StringUtils.isEmpty(obj.toString()) &&
					Integer.valueOf(obj.toString()) > 0){
				logger.error("Invalid script:" + obj);
				throw new TaskException("Invalid script:" + obj);
				
			}
		}
		
		scriptsCopy.putAll(scripts);
		
		for(int i = 0; scriptsCopy.size() > 0 && i <= Integer.MAX_VALUE; i++){
			try {
				script = scriptsCopy.remove(String.valueOf(i + 1));
				logger.info("evaluate script:", script);
				if (!StringUtils.isEmpty(script)) {
					interpreter.eval(script);
					afterEvaluate();
				}
			} catch (Exception e) { //Keep processing if there is a exception???
				logger.error(e, "Exception on evaluate script:" + script);
				setResultsToContext(TagNames.EXCEPTION.name(), e.getCause());
			}
		}
		
	}
	
	
	public void source(String fileName) throws Exception {
		try {
			interpreter.source(fileName);
		} catch (Exception e) {
			logger.error(e, "exception to source file:" + fileName);
			throw e;
		}
		
	}
	
	
	/**
	 * preparation before the script evaluation
	 * 
	 * @throws EvalError
	 */
	protected void beforeProcess() throws EvalError {
		
		//set data container data
		logger.debug("beforeProcess set data container data:");
		for(Object key : context.keySet()){
			Object object = context.get(key);
			// - is not valid variable name in bean shell, so strip off here
			interpreter.set(key.toString().replaceAll("-", ""), object);
			logger.debug(key.toString() + " : " + object);
		}
	}
	

	/**
	 * post processing after the script evaluation
	 * @throws EvalError 
	 * 
	 */
	protected void afterEvaluate() throws EvalError {
		List<String> varList = new ArrayList<String>();
		//Retrieve all variables assigned by the script and save into data container
		List<String> tokens = DataUtil.parseAndRemoveWhitespace(script, PARSE_DELIMITER, true);
		if(tokens == null) return;

		for(int i = 1; i < tokens.size(); i++){

			if(!tokens.get(i).equals(AssignmentOperator.AS.getOperator())) continue;

			if(tokens.get(i).equals(AssignmentOperator.AS.getOperator()) && 
					tokens.get(i+1).equals(AssignmentOperator.AS.getOperator())){
				i++;
				continue;
			} else {
				if(isAssigmentOpr(tokens.get(i-1))){
					varList.add(tokens.get(i-2));
				} else {
					varList.add(tokens.get(i-1));
				}
			}	
		}
		
		setScriptResults(varList);
	}

	protected void setScriptResults(List<String> varList) throws EvalError {
		for(String var : varList){
			Object value = interpreter.get(var);
			if(value != null){
				setResultsToContext(var, value);
			}
			logger.debug("script var:", var, " value:", (value == null ? "null" : value.toString()));
		}
	}

	protected void setResultsToContext(String var, Object value) {
		context.put(var, value);
	}
	
	public static boolean isAssigmentOpr(String str) {
		
		for(AssignmentOperator opr : AssignmentOperator.values()){
			if(opr.getOperator().contains(str)) return true;
		}
		
		return false;
	}

	
	public Map<Object, Object> getContext() {
		return context;
	}

	public void setContext(Map<Object, Object> context) {
		this.context = context;
	}

	public void initInterpreter(Map<Object,Object> context){
		try {
			interpreter = getInterpreter();
			this.context = context;
			beforeProcess();
			
		} catch (Exception e) {
			logger.error(e, "Exception to init interpreter.");
		}
	}
	
	public Object eval(String script){
		try {
			return interpreter.eval(script);
		} catch (Exception e) {
			logger.error(e, "Exception to evaluate :" + script);
			return null;
		}
	}

	public void set(String name, Object value) {
		try {
			getInterpreter().set(name, value);
		} catch (Exception e) {
			logger.error(e, "Exception to set Object:" + name);
		}
	}

	private Interpreter getInterpreter() {
		if(interpreter == null){
			createInterpreter();
		}
		return interpreter;
	}

	protected void createInterpreter() {
		interpreter = new Interpreter();
		interpreter.getNameSpace().importCommands("com.honda.galc.script.commands");
	}

	public void setContext(String name, Object value) {
		try {
			context.put(name, value);
		} catch (Exception e) {
			logger.error(e, "Exception to set context Object:" + name);
		}
	}

	public void setScripts(Map<String, String> scripts) {
		this.scripts = scripts;
	}
	
	public void replace(StringBuffer sb, String org, String rep){
		int start = sb.indexOf(org);
		sb.replace(start, start + org.length(), rep);
	
	}

	public Object get(String arg) {
		try {
			return interpreter.get(arg);
		} catch (Exception e) {
			return null;
		}
	}
	
}
