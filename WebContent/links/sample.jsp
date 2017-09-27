<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<title>Code Examples</title>
		<link href="css/bootstrap-combined.min.css" rel="stylesheet"/>
		<script src="js/jquery-2.0.0.min.js" type="text/javascript"></script>
		<script src="js/ace.js" type="text/javascript" charset="utf-8"></script>
		<script src="js/developer.js" type="text/javascript"></script>
		<link href="css/developer.css" rel="stylesheet" type="text/css"/>	
		<link rel="SHORTCUT ICON" type="image/x-icon" href="/ui/bb-icon2.ico"/>
	</head>
	<body>
		<textarea id="codes" style="display: none">int i = Integer.parseInt(args[0]);
int j = Integer.parseInt(args[1]);

System.out.println("Sum is " + (i + j));</textarea>
		<textarea id="examples" style="display: none">User user = new User();
user.setUserName("test");
System.out.println(user.getUserName());</textarea>
		<div id="psvm-start">
			<div class="pad-top">
				<span class="keyword">public static void</span> main(String args[]) <span class="keyword">throws</span> Exception {
			</div>
		</div>
		<div id="psvm-code">
			<textarea name="code" class="hide"></textarea>
			<div id="code" class=" ace_editor ace-xcode" style="height: 70px;"><textarea class="ace_text-input" wrap="off" autocapitalize="off" spellcheck="false" style="opacity: 0; height: 14px; width: 6.59766px; right: 551.402px; bottom: 146px;"></textarea></div>
		</div>
		<div id="psvm-end">}</div>
		<input type="button" class="confirm add-code" value="Add Code" onclick="addCode('codes')"/>

		<div id="psvm-start">
			<div class="pad-top">
				<span class="keyword">public static void</span> main(String args[]) <span class="keyword">throws</span> Exception {
			</div>
		</div>
		<div id="psvm-code">
			<textarea name="example" class="hide"></textarea>
			<div id="example" class=" ace_editor ace-xcode" style="height: 70px;"><textarea class="ace_text-input" wrap="off" autocapitalize="off" spellcheck="false" style="opacity: 0; height: 14px; width: 6.59766px; right: 551.402px; bottom: 146px;"></textarea></div>
		</div>
		<div id="psvm-end">}</div>
		<input type="button" class="confirm add-code" value="Add Code" onclick="addCode('examples')"/>
	</body>
</html>