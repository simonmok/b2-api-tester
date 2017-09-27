jQuery.noConflict();

function initTokenInput(box, data) {
	jQuery(box).tokenInput([], {
        theme: 'facebook',
		allowFreeTagging: true,
		tokenValue: 'name',
		searchingText: '',
		noResultsText: '',
		hintText: '',
		allowCreation: true,
		prePopulate: data
	});
}