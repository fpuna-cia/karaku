var alert = true;

$(window).bind('beforeunload', function() {
	if (alert && $("form[id$='base_abm_form']").data("changed")) {
		return 'Se perderan los cambios, esta seguro?';
	}
	loadingPopup.show();
});

$(window).bind('load', function() {
	$("form[id$='base_abm_form'] :input").change(function() {
		$("form[id$='base_abm_form']").data("changed", true);
	});
});

function no_alert() {
	alert = false;
}


function handleDisableButton(data) {
    if (data.source.type != "submit") {
        return;
    }

    switch (data.status) {
        case "begin":
        	loadingPopup.show();
            break;
        case "complete":
        	loadingPopup.hide();
            break;
    }    
}

jsf.ajax.addOnEvent(handleDisableButton);
