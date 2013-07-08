jsf.ajax.addOnEvent(function(data) {
	if (typeof jsf !== 'undefined') {
	    if (data.status == "success") {
	        var viewState = getViewState(data.responseXML);
	
	        if (viewState) {
	            for (var i = 0; i < document.forms.length; i++) {
	                var form = document.forms[i];
	
	                if (!hasViewState(form)) {
	                    createViewState(form, viewState);
	                }
	            }
	        }
	    }
	}
});

function getViewState(responseXML) {
    var updates = responseXML.getElementsByTagName("update");

    for (var i = 0; i < updates.length; i++) {
        var update = updates[i];

        if (update.getAttribute("id") == "javax.faces.ViewState") {
            return update.firstChild.nodeValue;
        }
    }

    return null;
}

function hasViewState(form) {
    for (var i = 0; i < form.elements.length; i++) {
        if (form.elements[i].name == "javax.faces.ViewState") {
            return true;
        }
    }

    return false;
}

function createViewState(form, viewState) {
    var hidden;

    try {
        hidden = document.createElement("<input name='javax.faces.ViewState'>"); // IE6-8.
    } catch(e) {
        hidden = document.createElement("input");
        hidden.setAttribute("name", "javax.faces.ViewState");
    }

    hidden.setAttribute("type", "hidden");
    hidden.setAttribute("value", viewState);
    hidden.setAttribute("autocomplete", "off");
    form.appendChild(hidden);
}