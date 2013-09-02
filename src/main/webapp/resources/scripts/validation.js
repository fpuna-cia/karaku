/**
 * Función que se encarga de validar lo que se ingresa en un input.
 * 
 * <br />
 * 
 * @param regexString
 *            cadena (proveniente de {@literal @}Pattern) que se utiliza para
 *            validar.
 */
function validateInput(regexString) {
	var theEvent = window.event || event;
	var key = theEvent.keyCode || theEvent.which;
	if (key >= 46) {
		key = String.fromCharCode(key);
		var regex = new RegExp("^" + regexString + "$");
		if (!regex.test(key)) {
			theEvent.returnValue = false;
			if (theEvent.preventDefault) {
				theEvent.preventDefault();
			}
		}
	}
}