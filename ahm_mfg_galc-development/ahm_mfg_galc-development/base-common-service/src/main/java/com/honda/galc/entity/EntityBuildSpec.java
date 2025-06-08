package com.honda.galc.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.TagNames;
/**
 * 
 * <h3>EntityBuildSpec</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntityBuildSpec description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Feb 10, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 10, 2015
 */
public class EntityBuildSpec {
	enum BuildType {ONE_TO_ONE, ONE_TO_MANY};
	enum Spec_Keys{
		uniq /* will not create the same entity again if it already created */, 
		size, /* how many entities need to be built.*/
		useSubIds, /*use sub ids to build multiple entities, optional: default to true*/
		
	};
	
	String property;
	String name;
	BuildType type;
	HashMap<String, Object> args;
	List<FileFieldDef> fieldDefs = null;
	
	public EntityBuildSpec(String property) {
		super();
		this.property = property;
		
		init();
	}

	private void init() {
		if(StringUtils.isEmpty(property)) {
			name = BuildType.ONE_TO_ONE.name();
			type = BuildType.ONE_TO_ONE;//default to one_to_one
		} else {
			name = property.contains("(") ? property.substring(0, property.indexOf("(")).trim() : property;
			args = EntityBuilderHelper.parseArgs(property);
		}
	}

	
	
	// -------------- getters & setters -----------------	
	public BuildType getType() {
		if(type == null){
			try {
				return BuildType.valueOf(name);
			} catch (Exception e) {
				//OK;
			}
		}
		return type;
		
	}

	public HashMap<String, Object> getArgs() {
		return args;
	}

	public boolean isUniq() {
		String uniq = (String)getArgs().get(Spec_Keys.uniq.name());
		return StringUtils.isEmpty(uniq) ? false : Boolean.parseBoolean(uniq);
	}
	
	public boolean isUseSubIds() {
		String useSubIds = (String)getArgs().get(Spec_Keys.useSubIds.name());
		return StringUtils.isEmpty(useSubIds) ? true : Boolean.parseBoolean(useSubIds);
	}
	
	public String getSize() {
		return (String)getArgs().get(Spec_Keys.size.name());
	}
	
	public String getName(){
		return StringUtils.trim(this.name);
	}

	public List<FileFieldDef> getFieldDefs(){
		if(fieldDefs == null) {
			fieldDefs = new ArrayList<FileFieldDef>();
			
			if(args == null) return fieldDefs;
			for(String argKey : args.keySet()){
				if(argKey.startsWith(TagNames.FIELD_DEFS.name())){
					String fieldKey = argKey.substring((argKey.indexOf("{") +1), argKey.indexOf("}"));
					try {
						fieldDefs.add(new FileFieldDef(fieldKey, args.get(argKey).toString()));
					} catch (Exception e) {
						Logger.getLogger().warn(e, " Exception to extract fields from build spec arguments");
					}
				}
			}
		}
		return fieldDefs;
	}
		
}
