<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*, java.sql.*, blackboard.db.*" %>
<%@ page import="com.beenet.java.Config" %>
<%@ taglib uri="/bbNG" prefix="bbNG" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<bbNG:genericPage>

	<bbNG:pageHeader instructions="This admin tool allows you to test Java codes.">
	    <bbNG:pageTitleBar title="Java Developer"/>
	    <bbNG:breadcrumbBar environment="SYS_ADMIN">
	    	<bbNG:breadcrumb href="/webapps/portal/execute/tabs/tabAction?tabType=admin" title="Administrator Panel"/>
	        <bbNG:breadcrumb href="query.jsp" title="Java Developer"/>
	    </bbNG:breadcrumbBar>
	</bbNG:pageHeader>
	
	<link href="css/bootstrap-combined.min.css" rel="stylesheet"/>
	<script src="js/jquery-2.0.0.min.js" type="text/javascript"></script>
	<script src="js/ace.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/developer.js?v=<%=UUID.randomUUID().toString()%>" type="text/javascript"></script> <%-- TODO remove in production --%>
	<link href="css/developer.css?v=<%=UUID.randomUUID().toString()%>" rel="stylesheet" type="text/css"/> <%-- TODO remove in production --%>
	
	<c:if test="${not empty successMessage}">
		<div id="success-message" class="receipt good" style="width: 97%; margin-bottom: 40px; display: none;">
			<span>${successMessage}<c:if test="${not empty message}"><br/>${message}</c:if></span>
			<div id="execution-results"></div>
		    <a class="close" href="#" title="Close" style="top: 5px" onclick="Element.remove($('success-message')); return false;"><img alt="Close" src="/images/ci/ng/close_mini.gif"/></a>
		</div>
		<input type="hidden" id="execution-link" value="${link}"/>
	</c:if>
	
	<c:if test="${not empty errorMessage}">
		<div id="error-message" class="receipt bad" style="width: 97%; margin-bottom: 40px;">
			<span>${errorMessage}<c:if test="${not empty message}"><br/>${message}</c:if></span>
		    <a class="close" href="#" title="Close" style="top: 5px" onclick="Element.remove($('error-message')); return false;"><img alt="Close" src="/images/ci/ng/close_mini.gif"/></a>
		</div>
	</c:if>
	
	<bbNG:form action="execute" method="post" onSubmit="return execute()" id="codeForm">
		<input type="hidden" name="uuid" value="${uuid}"/>
		<textarea id="codes" name="codes" style="display: none"><c:if test="${not empty codes}">${codes}</c:if></textarea>
		<textarea id="methods" name="methods" style="display: none"><c:if test="${not empty methods}">${methods}</c:if></textarea>
		<bbNG:dataCollection>
			<bbNG:step title="Main Program">
				<bbNG:dataElement>
					<bbNG:elementInstructions text="Enter the codes of the main program. Click <a href='javascript: void(0)' onclick='samples()'>here</a> to view code examples."/>
					<div class="code">
						<div><br/><input id="add-import" class="button-2" type="button" value="Add Import"/><img src="images/download.png" onclick="upload()" class="download-icon flip-180" title="Upload Codes"/><img src="images/download.png" onclick="download()" class="download-icon" title="Download Codes"/></div>
						<input type="file" id="fileUpload" style="display: none" accept=".java"/>
						<div id="import-area" style="display: none; margin-top: 10px;">
							<textarea id="imports" name="imports" style="width: 95%" rows="5"><c:if test="${not empty imports}">${imports}</c:if><c:if test="${empty imports}"><%= new Config().getImportContents() %></c:if></textarea>
							<textarea id="last-imports" style="display: none"></textarea>
							<div style="width: 96.2%; text-align: right;">
								<input id="cancel-import" class="button-2" type="button" value="Cancel"/>
								<input id="confirm-import" class="button-1" type="button" value="OK"/>
							</div>
						</div>
						<div id="class-start">
							<span class="keyword">public class</span> MainClass {
						</div>
						<div id="psvm-start">
							<div>
								<input class="button-2" type="button" id="fieldMethods" value="Add class fields / methods"/>
								<div id="method-body" style="display: none;">
									<textarea name="methods" class="hide"></textarea>
									<div id="methods-text" class="ace_editor ace-xcode" style="margin-top: 10px; height: 70px;"><textarea class="ace_text-input" wrap="off" autocapitalize="off" spellcheck="false" style="opacity: 0; height: 14px; width: 6.59766px; right: 551.402px; bottom: 146px;"></textarea></div>
									<input type="button" id="removeMethods" value="Remove class fields / methods" class="button-2 remove-methods"/>
								</div>
							</div>
							<div class="pad-top">
								<span class="keyword">public static void</span> main(String[] args) <span class="keyword">throws</span> Exception {
							</div>
						</div>
						<div id="psvm-code">
							<textarea name="code" class="hide"></textarea>
							<div id="code" class="ace_editor ace-xcode" style="height: 70px;"><textarea class="ace_text-input" wrap="off" autocapitalize="off" spellcheck="false" style="opacity: 0; height: 14px; width: 6.59766px; right: 551.402px; bottom: 146px;"></textarea></div>
						</div>
						<div id="psvm-end">}</div>
						<div id="class-end">}</div>
						<div class="arguments">
							<div class="pad-top"><label for="arguments">Input Arguments:</label></div>
							<div class="pad-top">
								<input id="arguments" type="text" name="arguments" <c:if test="${not empty arguments}">value="${arguments}"</c:if>/>
							</div>
						</div>
					</div>
				</bbNG:dataElement>
			</bbNG:step>
			<bbNG:stepSubmit cancelUrl="/webapps/portal/execute/tabs/tabAction?tabType=admin"/>
		</bbNG:dataCollection>
	</bbNG:form>
</bbNG:genericPage>