jQuery(document).ready(function() {
	var datacontainer = $('.rf-edt-c');

	console.log(datacontainer.width());
	$('.rf-edt-hdr-c-cnt').width(datacontainer.width());
	$('.rf-edt-c-cnt').width(datacontainer.width());
});