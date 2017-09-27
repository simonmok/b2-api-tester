<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="/bbNG" prefix="bbNG" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.beenet.java.Config" %>

<bbNG:learningSystemPage>	
	<bbNG:pageHeader instructions="This page allows you to configure Java Developer properties.">
	    <bbNG:pageTitleBar title="Java Developer Settings"/>	  
	</bbNG:pageHeader>
	
	<script src="js/jquery-2.0.0.min.js" type="text/javascript"></script>
	<script src="js/jquery.tokeninput.min.js" type="text/javascript"></script>
	<script src="js/tokeninput.js" type="text/javascript"></script>
	<link href="css/token-input.css" rel="stylesheet" type="text/css"/>	
	
	<c:if test="${not empty successMessage}">
		<div id="success-message" class="receipt good" style="width: 100%; margin-bottom: 40px;">
			<span>${successMessage}</span>
		    <a class="close" href="#" title="Close" style="top: 5px" onclick="Element.remove($('success-message')); return false;"><img alt="Close" src="/images/ci/ng/close_mini.gif"/></a>
		</div>
	</c:if>
	
	<% final Config configuration = new Config(); %>
	
	<form action="config" method="post">
		<bbNG:dataCollection>
			<bbNG:step title="Code Generation">
				<bbNG:dataElement label="Default Import">
					<input name="imports" id="imports" type="text" />				
				</bbNG:dataElement>
			</bbNG:step>
			<bbNG:stepSubmit cancelUrl="/webapps/plugins/execute/plugInController"/>
		</bbNG:dataCollection>
	</form>
	<script type="text/javascript">
    	var imports = [
   			<% int count = 0; %>
   			<% for (final String item : configuration.getImportsAsList()) { %>
   				<% if (count++ > 0) { %>,<% } %>{id: <%=count%>, name: '<%= item %>'}
   			<% } %>
   		];
    	jQuery(document).ready(function() {
    		initTokenInput('#imports', imports);
    	});
	</script>
</bbNG:learningSystemPage>