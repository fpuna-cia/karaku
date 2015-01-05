function equalColumnWidth() {
	/* seleccionar las tablas */
	var tbody = $('.rf-edt-b .rf-edt-tbl .rf-edt-tbl').first(); /* cuerpo */
	var theader = $('.rf-edt-hdr .rf-edt-cnt .rf-edt-tbl').first(); /* cabecera */

	/* layout fixed para que las celdas no se expandan de acuerdo al contenido */
	theader.attr('style', 'table-layout: fixed;');
	tbody.attr('style', 'table-layout: fixed;');

	/* para las celdas del body */
	var cells = tbody.find('td');
	/* se calcula el % para la longitud de cada columna */
	var width = 100 / cells.length;

	cells.each(function(index) {
		/* se quitan de los td las clases que dan problemas */
		$(this).removeClass();
		/* se agrega la longitud */
		$(this).attr('style', 'width:' + width + '%');
		/*
		 * a uno de los divs internos se le quita la clase que le da un ancho
		 * fijo y bordes, y se le agrega otra para que se expanda dentro del td
		 */
		$(this).find('.rf-edt-c').removeClass('rf-edt-c').addClass('cell-div');
		/*
		 * al td se le agrega una clase para que se dibujen los bordes de la
		 * celda
		 */
		$(this).addClass('cell-border');
		/*
		 * al otro div interno también se le quita el ancho fijo y se le agrega
		 * propiedades para que se expanda a su contenedor
		 */
		$(this).find('.rf-edt-c-cnt').each(function(index) {
			$(this).removeProp('style');
			$(this).attr('style', 'width:100%;height:100%');
		});
	});

	/* para las celdas del header */
	cells = theader.find('td');
	width = 100 / cells.length;

	cells.each(function(index) {
		/* se quitan las clases problemáticas */
		$(this).removeClass();
		/* se le asigna el ancho en % */
		$(this).attr('style', 'width:' + width + '%');
		/*
		 * a uno de los divs internos se le quita el ancho fijo y se le agregan
		 * las propiedades para que se expanda
		 */
		$(this).find('.rf-edt-c-cnt').each(function(index) {
			$(this).removeProp('style');
			$(this).attr('style', 'width:100%;height:100%;');
		});
	});

	/* se esconde el scroll que deforma el body de la tabla */
	$('.rf-edt-scrl').hide();
}