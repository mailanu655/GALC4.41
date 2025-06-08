<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<% Boolean disableEdit =  (Boolean)request.getAttribute("disableEdit"); %>
  <table border="0" width="100%">
  <TBODY>
     <tr>
       <td colspan="2">
       <table border="0">
       <tbody>
         <tr>
           <th class="settingstext" align="left">ProcessPointAt: </th> 
           <td align="left">
           <html:text styleId="broadcastppid"  property="applicationID" readonly="true" styleClass="readonly" size="16" maxlength="16"/></td>
         </tr>
       </tbody>
       </table>
       </td>
     </tr>
     <tr>
      <td>
        <table border="1" width="100%">
          <THEAD>
            <tr>
              <th>Seq</th>
              <th>Destination Type</th>
              <th>Destination ID</th>
              <th>Request ID</th>
              <th>Argument</th>
              <th>Auto Enabled</th>
              <th>Check Point</th>
              <th>Condition</th>
              <th>Condition Type</th>
            </tr>
          </THEAD>
          <TBODY id="broadcasttablebody">
          <c:set var="bdseq" value="0"/>
          <c:forEach var="destination" items="${broadcastDestinationList}">
          	<c:set var="bdseq" value="${bdseq + 1}"/>
          	<c:set var="bdcheckedrb" value="checked"/>
          	<c:if test="${bdseq == 1}">
          		<c:set var="bdcheckedrb" value="checked"/>
          	</c:if>
          	<c:set var="displayRequestID" value="${destination.requestId}"/>
          	<c:if test="${empty displayRequestID}">
          		<c:set var="displayRequestID" value="&nbsp;"/>
          	</c:if>
          	<c:set var="displayArg" value="${destination.argument}"/>
          	<c:if test="${empty displayArg}">
          		<c:set var="displayArg" value="&nbsp;"/>
          	</c:if>
          	<c:set var="displayAutoEnabled" value="${destination.autoEnabled}"/>
          	<c:if test="${empty displayAutoEnabled}">
          		<c:set var="displayAutoEnabled" value="&nbsp;"/>
          	</c:if>
          	
          	<c:set var="displayCheckPoint" value="${destination.checkPoint}"/>
            <c:if test="${empty displayCheckPoint}">
                <c:set var="displayCheckPoint" value="&nbsp;"/>
            </c:if>
            <c:set var="displayCondition" value="${destination.condition}"/>
            <c:if test="${empty displayCondition}">
                <c:set var="displayCondition" value="&nbsp;"/>
            </c:if>
            <c:set var="displayConditionType" value="${destination.conditionType}"/>
            <c:if test="${empty displayConditionType}">
                <c:set var="displayConditionType" value="&nbsp;"/>
            </c:if>
          	
          	
           <tr id="<c:out value='trbd${bdseq}'/>">                                       
             <td id="td_bdsequenceNo<c:out value='${bdseq}'/>" ><INPUT type="radio" name="broadcastdatasequence" value="<c:out value='${bdseq}'/>" <c:out value='${bdcheckedrb}'/> ><c:out value='${destination.sequenceNumber}'/></td>
             <td id="td_bddestinationType<c:out value='${bdseq}'/>"><c:out value='${destination.destinationTypeName}'/><input type="hidden" name="bddestinationType<c:out value='${bdseq}'/>" value="<c:out value='${destination.destinationTypeId}'/>" /></td>
             <td id="td_bddestinationID<c:out value='${bdseq}'/>"><c:out value='${destination.destinationId}'/><input type="hidden" name="bddestinationID<c:out value='${bdseq}' />" value="<c:out value='${destination.destinationId}'/>" /></td>
             <td id="td_bdrequestID<c:out value='${bdseq}'/>"><c:out escapeXml='false' value='${displayRequestID}'/><input type="hidden" name="bdrequestID<c:out value='${bdseq}' />" value="<c:out value='${destination.requestId}'/>" /></td>
             <td id="td_bdargument<c:out value='${bdseq}'/>"><c:out escapeXml='false' value='${displayArg}'/><input type="hidden" name="bdargument<c:out value='${bdseq}' />" value="<c:out value='${destination.argument}'/>" /></td>
             <td id="td_bdAutoEnabled<c:out value='${bdseq}'/>"><c:out escapeXml='false' value='${displayAutoEnabled}'/><input type="hidden" name="bdAutoEnabled<c:out value='${bdseq}' />" value="<c:out value='${destination.autoEnabled}'/>" /></td>
             <td id="td_bdCheckPoint<c:out value='${bdseq}'/>"><c:out escapeXml='false' value='${displayCheckPoint}'/><input type="hidden" name="bdCheckPoint<c:out value='${bdseq}' />" value="<c:out value='${destination.checkPoint}'/>" /></td>
             <td id="td_bdCondition<c:out value='${bdseq}'/>"><c:out escapeXml='false' value='${displayCondition}'/><input type="hidden" name="bdCondition<c:out value='${bdseq}' />" value="<c:out value='${destination.condition}'/>" /></td>
             <td id="td_bdConditionType<c:out value='${bdseq}'/>"><c:out escapeXml='false' value='${displayConditionType}'/><input type="hidden" name="bdConditionType<c:out value='${bdseq}' />" value="<c:out value='${destination.conditionType}'/>" /></td>
           </tr>           
          </c:forEach>
          </TBODY>
        </table>
      </td>
      <td align="right" width="90" valign="top">
			           <table border="0">
			              <tbody>
			              <tr>
			                 <td align="right"><button class="broadcastdestbutton" onclick="doAddBroadcastDestination();return false;" >Add</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="broadcastdestbutton" onclick="removeBroadcast(); return false;"  >Delete</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="broadcastdestbutton" onclick="doModifyBroadcastDestination();return false;" >Modify</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="broadcastdestbutton" onclick="upBroadcast(); return false;">Up</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="broadcastdestbutton" onclick="downBroadcast(); return false;">Down</button></td>			                 
			              </tr>
			             
			              </tbody>
			           </table>
		</td>
     </tr>
     <tr>
			<td colspan="3" id="broadcastmsgcol">
				<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE"><bean:write name="msg"/></html:messages>

				<html:errors/>			
			</td>
	 </tr>
	 <TR>
		<TD colspan="2"><html:submit property="apply" value="Apply" disabled="<%= disableEdit.booleanValue() %>" />&nbsp;&nbsp;&nbsp;
		<html:submit property="cancel" value="Cancel" /></TD>	
	 </TR>
</TBODY>
</table>
