var KarakuExtendedDataTableFix = {

	headerTable : '.rf-edt-hdr .rf-edt-cnt .rf-edt-tbl',
	bodyTable : '.rf-edt-b .rf-edt-tbl .rf-edt-tbl',

	selectHeader : function() {
		return $(this.headerTable).first();
	},

	selectBody : function() {
		return $(this.bodyTable).first();
	},

	setPercentWidth : function(selector, width) {
		$(selector).attr('style', 'width:' + width + '%');
	},

	setTableLayoutFixed : function(table) {
		table.attr('style', 'table-layout: fixed;');
	},

	applyExpandedStyle : function(div) {
		$(div).removeProp('style');
		$(div).attr('style', 'width:100%;height:100%');
	},

	getCells : function(table) {
		return table.find('td');
	},

	hideScroll : function() {
		$('.rf-edt-scrl').hide();
	},

	fixCellBorders : function(cell) {
		$(cell).find('.rf-edt-c').removeClass('rf-edt-c').addClass('cell-div');
		$(cell).addClass('cell-border');
	},

	fixColumns : function() {

		var theader = this.selectHeader(); // cabecera
		var tbody = this.selectBody(); // cuerpo

		// layout fixed para que las celdas no se expandan de acuerdo al
		// contenido

		this.setTableLayoutFixed(theader);
		this.setTableLayoutFixed(tbody);

		// para las celdas del body

		var cells = this.getCells(tbody);

		// se calcula el % para la longitud de cada columna
		var width = 100 / cells.length;

		cells.each(function(index) {

			// se quitan de los td las clases que dan problemas
			$(this).removeClass();

			// se agrega la longitud
			KarakuExtendedDataTableFix.setPercentWidth(this, width);

			// se corrige el borde de la celda, sacando el estilo del div
			// interno y aplicando otro estilo a la celda
			KarakuExtendedDataTableFix.fixCellBorders(this);

			// al otro div interno también se le quita el ancho fijo y se le
			// agrega propiedades para que se expanda a su contenedor

			$(this).find('.rf-edt-c-cnt').each(function(index) {
				KarakuExtendedDataTableFix.applyExpandedStyle(this);
			});
		});

		// para las celdas del header

		cells = this.getCells(theader);
		width = 100 / cells.length;

		cells.each(function(index) {

			// se quitan las clases problemáticas
			$(this).removeClass();

			// se le asigna el ancho en %
			KarakuExtendedDataTableFix.setPercentWidth(this, width);

			// a uno de los divs internos se le quita el ancho fijo y se le
			// agregan las propiedades para que se expanda

			$(this).find('.rf-edt-hdr-c-cnt').each(function(index) {
				KarakuExtendedDataTableFix.applyExpandedStyle(this);
			});
		});

		this.hideScroll();
	}
};
