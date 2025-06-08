package com.honda.galc.script;

import java.util.Map;


public interface IScriptInterpreter {
	void process(Map<String, String> scripts) throws Exception;
	void process(String script) throws Exception;
}
