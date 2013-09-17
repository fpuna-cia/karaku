var alert = true;

$(window).bind('beforeunload', function() {
	if (alert && $("form[id$='base_abm_form']").data("changed")) {
		return 'Se perderan los cambios, esta seguro?';
	}
});

$(window).bind('load', function() {
	console.log($("#j_idt39:base_abm_form :input"));
	$("form[id$='base_abm_form'] :input").change(function() {
		$("form[id$='base_abm_form']").data("changed", true);
	});
});

function no_alert() {
	alert = false;
}
