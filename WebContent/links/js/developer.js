jQuery.noConflict();

jQuery(document).ready(function () {
	configAceEditor();
	
	var imports = jQuery('#imports'), last = jQuery('#last-imports'), area = jQuery('#import-area'), methodBody = jQuery('#method-body'), fieldMethods = jQuery('#fieldMethods'); 
	
	jQuery('#add-import').click(function () {
		last.val(imports.val());
		area.fadeIn();
	});
	
	jQuery('#confirm-import').click(function () {
		area.fadeOut();
	});
	
	jQuery('#cancel-import').click(function () {
		area.fadeOut(function () {
			imports.val(last.val());
		});
	});
	
	fieldMethods.click(function () {
		fieldMethods.hide();
		ace.edit('methods-text').focus();
		methodBody.fadeIn();
	});
	
	jQuery('#removeMethods').click(function () {
		ace.edit('methods-text').setValue('');
		methodBody.fadeOut(function () {
			fieldMethods.show();
		});
	});

	jQuery('#fileUpload').change(function (event) {
		importCodes(event);
	});

	setFont();
	executeCodes();
});

var heightChangeFunction = function (id) {

	var editor = ace.edit(id), newHeight = editor.getSession().getScreenLength() * editor.renderer.lineHeight + editor.renderer.scrollBar.getWidth();
	jQuery('#' + id).height(newHeight.toString() + 'px');
	jQuery('#' + id + '-section').height(newHeight.toString() + 'px');

	ace.edit(id).resize();
};

var samples = function () {
	window.open('sample.jsp', 'sample', 'height=600, width=880, resizable=0, scrollbars=1');
}

var initEditor = function (id) {

    ace.require('ace/ext/language_tools');
    var editor = ace.edit(id);
	editor.setTheme('ace/theme/xcode');
	editor.getSession().setMode('ace/mode/java');
	editor.renderer.setShowGutter(true);
	editor.renderer.setAnimatedScroll(true);
	editor.renderer.setShowPrintMargin(false);
	return editor;
}

var resetAll = function () {

    ace.edit('code').getSession().setValue('');
    ace.edit('methods-text').getSession().setValue('');
    jQuery('#arg').val('');
}

var populateForm = function () {
	var codes = ace.edit('code').getValue();
	jQuery('#codes').text(codes);
	if (jQuery('#method-body').is(':visible')) {
		jQuery('#methods').text(ace.edit('methods-text').getValue());
	}
	return codes;
}

var execute = function () {
	var codes = populateForm();
	return codes.length > 0;
}

var download = function () {
	populateForm();
	window.location.href = 'download?' + jQuery('#codeForm').serialize();
}

var configAceEditor = function () {

	var editor = initEditor('code');
	var heightChangeCode = function () {
		heightChangeFunction('code');
	}
	var heightChangeMethods = function () {
		heightChangeFunction('methods-text');
	}

	editor.getSession().on('change', heightChangeCode);

	var codes = jQuery('#codes').text();
	var example = jQuery('#example');
	if (example.length > 0) {
		var exampleEditor = initEditor('example');
		exampleEditor.setValue(jQuery('#examples').text(), -1);
		exampleEditor.setReadOnly(true);
		exampleEditor.renderer.$cursorLayer.element.style.display = 'none';
		editor.setValue(codes, -1);
		editor.setReadOnly(true);
		editor.renderer.$cursorLayer.element.style.display = 'none';
	} else {
		var editorMethod = initEditor('methods-text');
		editorMethod.getSession().on('change', heightChangeMethods);
		if (codes.length > 0) {
			editor.setValue(codes, -1);
			var methods = jQuery('#methods').text();
			if (methods.length > 0) {
				editorMethod.setValue(methods, -1);
				jQuery('#method-body').show();
				jQuery('#fieldMethods').hide();
			}
		}
		
		heightChangeCode();
		heightChangeMethods();
	}
}

function addCode(id) {
	var editor = opener.ace.edit('code');
	editor.setValue(jQuery('#' + id).text(), -1);
	if (id === 'codes') {
		opener.document.getElementById('arguments').value = '4 5';
	}
	editor.focus();
	window.close();
}

function executeCodes() {	
	var link = jQuery('#execution-link'), results = jQuery('#execution-results');
	if (link.length > 0) {
		var url = link.remove().val();
		jQuery.ajax({
		    url: url,
			success: function (data) {
		        var parts = data.split('\n');
				jQuery.each(parts, function (index, line) {
		             results.append(jQuery('<div>').text(line));
				});
				results.parent().show();
			},
			error: function () {
				results.prev().html('Error in program execution. See <a target="_blank" href="' + url + '">here</a> to see the error message.');
				results.parent().removeClass('good').addClass('bad').show();
			}
		});
	}
}

function setFont() {
	setInterval(function () {
		jQuery('#methods-text *, #code *').css('font-family', 'Consolas');
	}, 500);
}

function upload() {
	jQuery('#fileUpload').click();
}

function importCodes(event) {

	var input = event.target, reader = new FileReader();
	reader.onload = function () {
		var text = reader.result, lines = text.split('\n'), imports = [], methods = [], body = [], count = 0;
		for (count = 0; count < lines.length; count++) {
			var line = lines[count].trim();
			if (line.startsWith('import ') && line.endsWith(';')) {
				var parts = line.split(/[\s;]+/);
				for (var index = 0; index < parts.length; index++) {
					var part = parts[index];
					if (part !== 'import' && part.length > 0) {
						imports.push(part);
					}
				}
			} else {
				var parts = line.split(/\s+/), classIndex = parts.indexOf('class'), mainClassIndex = parts.indexOf('MainClass'), startClassIndex = parts.indexOf('{');
				if (classIndex >= 0 && mainClassIndex > classIndex && startClassIndex + 1 === parts.length) {
					break;
				}
			}
		}

		for (count = count + 1; count < lines.length; count++) {
			var line = lines[count].trim(), parts = line.split(/[\s()\[\]]+/), publicIndex = parts.indexOf('public'), staticIndex = parts.indexOf('static'), voidIndex = parts.indexOf('void'), mainIndex = parts.indexOf('main'), stringIndex = parts.indexOf('String'), argsIndex = parts.indexOf('args'), startMethodIndex = parts.indexOf('{');
			if (publicIndex >= 0 && staticIndex > publicIndex && voidIndex > staticIndex && mainIndex > voidIndex && stringIndex > mainIndex && argsIndex > stringIndex && startMethodIndex + 1 === parts.length) {
				break;
			} else {
				if (line.length > 0) {
					methods.push(line);
				}
			}
		}

		var endCount;
		var closeCount = 0;

		for (endCount = lines.length - 1; endCount >= 0; endCount--) {
			var line = lines[endCount].trim();
			if (line === '}' && ++closeCount == 2) {
				break;
			}
		}

		for (count = count + 1; count < endCount; count++) {
			var line = lines[count].trim();
			if (body.length > 0 || line.length > 0) {
				body.push(line);
			}
		}

		if (endCount >= 0) {
			ace.edit('code').setValue(body.join('\n'), -1);
			ace.edit('methods-text').setValue(methods.join('\n'), -1);
			jQuery('#imports').val(imports.join('\n'));
			jQuery('#arguments').val('');
			var methodBody = jQuery('#method-body'), fieldMethods = jQuery('#fieldMethods');
			if (methods.length > 0) {
				methodBody.fadeIn();
				fieldMethods.fadeOut();
			} else {
				methodBody.fadeOut();
				fieldMethods.fadeIn();
			}
		} else {
			alert('Invalid Java source file');
		}
		jQuery('#fileUpload').val('');
	};

	reader.readAsText(input.files[0]);
}