/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package py.una.pol.karaku.util;

import java.io.ByteArrayInputStream;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.asistencial.domain.ArchivoAdjunto;

/**
 * Componente que provee funcionalidades para el manejo de archivos adjuntos
 * 
 * @author Carlos Gómez
 * @since 1.0
 * @version 1.0 04/02/2015
 * 
 */

@Component
public class FileHelper {

	private static final Logger LOG = LoggerFactory
			.getLogger(FormatProvider.class);

	public enum TipoArchivo {
		PNG("image/png", ".png"), JPEG("image/jpeg", ".jpeg"), GIF("image/gif",
				".gif"), PDF("application/pdf", ".pdf"), ZIP("application/zip",
				".zip"), TEXTO_PLANO("text/plain", ".txt"), DOC(
				"application/msword", ".doc"), DOCX(
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
				".docx"), XLS("application/vnd.ms-excel", ".xls"), XLSX(
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				".xlsx"), PPT("application/vnd.ms-powerpoint", ".ppt"), PPTX(
				"application/vnd.openxmlformats-officedocument.presentationml.presentation",
				".pptx"), ODS("vnd.oasis.opendocument.spreadsheet", ".ods"), ODT(
				"vnd.oasis.opendocument.text", ".odt"), ODP(
				"vnd.oasis.opendocument.presentation", ".odp");

		private final String tipoMIME;
		private final String extension;

		private TipoArchivo(String tm, String e) {

			tipoMIME = tm;
			extension = e;
		}

		public String getTipoMIME() {

			return tipoMIME;
		}

		public String getExtension() {

			return extension;
		}

		public String extensionByMime(String mime) {

			for (TipoArchivo ta : TipoArchivo.values()) {
				if (ta.getTipoMIME().equals(mime)) {
					return ta.getExtension();
				}
			}
			return null;
		}
	}

	@Autowired
	private DateProvider dp;

	/**
	 * Función que permite crear una entidad {@link ArchivoAdjunto} a partir del
	 * archivo obtenido del componente {@link UploadedFile} de PrimeFaces
	 * 
	 * @param file
	 *            : Archivo que se desea guardar en la BD
	 * @param descripción
	 *            : Texto descriptivo del archivo a guardar en la BD
	 * @param authId
	 *            : Usuario proveedor del archivo
	 * @return Un objeto {@link ArchivoAdjunto} con los datos del archivo
	 * @return null en caso de que no se pueda crear el {@link ArchivoAdjunto}
	 **/

	// TODO: Verificar mecanismo de validación para los tipos de datos
	// permitidos
	public ArchivoAdjunto fileToArchivoAdjunto(UploadedFile file,
			String descripcion, String authId) {

		if (file != null && authId != null || authId.compareTo("") == 0) {
			ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
			archivoAdjunto.setArchivo(file.getContents());
			archivoAdjunto.setDescripcion(descripcion);
			archivoAdjunto.setNombre(file.getFileName());
			archivoAdjunto.setFechaActualización(dp.getNow());
			archivoAdjunto.setAuthId(authId);
			archivoAdjunto.setMime(file.getContentType());
			return archivoAdjunto;
		}
		LOG.warn("Datos recibidos incorrectos");
		return null;
	}

	/**
	 * Función que permite obtener el archivo original a partir del
	 * ArchivoAdjunto persistido en BD
	 * 
	 * @param archivo
	 *            : {@link ArchivoAdjunto} con los datos persistidos en la BD
	 * @return el archivo en su formato original utilizando el modelo
	 *         {@link DefaultStreamedContent} de PrimeFaces y listo para su
	 *         descaga
	 **/
	public DefaultStreamedContent archivoAdjuntoToFile(ArchivoAdjunto archivo) {

		if (archivo.getArchivo() != null) {
			ByteArrayInputStream img = new ByteArrayInputStream(
					archivo.getArchivo());
			return new DefaultStreamedContent(img, archivo.getMime(),
					archivo.getNombre());
		}
		LOG.warn("Imposible convertir el archivo");
		return null;
	}

}
