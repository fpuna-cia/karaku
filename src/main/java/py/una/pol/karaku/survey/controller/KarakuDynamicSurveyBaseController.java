/*
 * @KarakuDynamicSurveyBaseController.java 1.0 03/06/13. Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.survey.business.IEncuestaDetalleLogic;
import py.una.pol.karaku.survey.business.IEncuestaLogic;
import py.una.pol.karaku.survey.business.IEncuestaPlantillaBloqueLogic;
import py.una.pol.karaku.survey.business.IEncuestaPlantillaPreguntaLogic;
import py.una.pol.karaku.survey.components.DynamicSurveyBlock;
import py.una.pol.karaku.survey.components.DynamicSurveyDataTable;
import py.una.pol.karaku.survey.components.DynamicSurveyField;
import py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField;
import py.una.pol.karaku.survey.components.DynamicSurveyFieldOption;
import py.una.pol.karaku.survey.components.DynamicSurveyFields;
import py.una.pol.karaku.survey.components.DynamicSurveyRow;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.domain.EncuestaDetalle;
import py.una.pol.karaku.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.pol.karaku.survey.domain.EncuestaPlantilla;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.ListHelper;
import py.una.pol.karaku.util.StringUtils;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 03/06/2013
 * 
 */
public abstract class KarakuDynamicSurveyBaseController implements
		IKarakuDynamicSurveyBaseController {

	public static enum Mode {
		NEW, EDIT, VIEW, DELETE
	}

	private Mode mode = Mode.VIEW;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Encuesta bean;

	// Lista de bloques dentro del formulario
	private List<DynamicSurveyBlock> blocks;
	@Autowired
	private IEncuestaLogic encuestaLogic;
	@Autowired
	private IEncuestaPlantillaPreguntaLogic templateQuestionLogic;
	@Autowired
	private IEncuestaPlantillaBloqueLogic templateBlockLogic;
	@Autowired
	private IEncuestaDetalleLogic encuestaDetalleLogic;

	public IKarakuBaseLogic<Encuesta, Long> getBaseLogic() {

		return encuestaLogic;
	}

	/**
	 * Construye los detalles de la encuesta en base al formulario dinámico.
	 * 
	 * <p>
	 * Esto es por cada respuesta de cada pregunta del bloque genera un
	 * {@link EncuestaDetalle}.
	 */
	protected void buildSurveyFromForm() {

		List<EncuestaDetalle> detallesEncuesta = new ArrayList<EncuestaDetalle>();
		for (DynamicSurveyBlock bloque : blocks) {
			if (bloque instanceof DynamicSurveyFields) {
				// Si el bloque es del tipo fields, cada field es un
				// detalle.
				detallesEncuesta.addAll(buildDetallesField(bloque));
			} else {
				if (bloque instanceof DynamicSurveyDataTable) {

					// Si el bloque es del tipo grilla, cada celda es un
					// detalle.
					detallesEncuesta.addAll(buildDetallesDataTable(bloque));
				}
			}

		}
		getBean().setDetalles(detallesEncuesta);
	}

	@Override
	public void buildBlocksFromSurvey() {

		for (EncuestaPlantillaBloque bloque : getBlocksByTemplate()) {

			addBlock(createBlock(getQuestionsByBlock(bloque), bloque));
		}

	}

	/**
	 * Crea un bloque de acuerdo al tipo y obtiene las respuestas
	 * correspondientes.
	 * 
	 * @param preguntas
	 *            Preguntas del bloque
	 * @param bloque
	 *            Bloque que se desea construir
	 * @return Bloque construido y cargado con las respuestas(si aplica)
	 */
	private DynamicSurveyBlock createBlock(
			List<EncuestaPlantillaPregunta> preguntas,
			EncuestaPlantillaBloque bloque) {

		/*
		 * Bloque del tipo GRILLA
		 */
		if ("GRILLA".equals(bloque.getTipoBloque().getDescripcion())) {
			return buildResponseGrill(getResponseBlock(bloque),
					new DynamicSurveyDataTable(preguntas, blocks.size()));
		} else {
			/*
			 * Bloque del tipo FIELDS
			 */
			return buildResponseFields(getResponseBlock(bloque),
					new DynamicSurveyFields(preguntas, blocks.size()));

		}

	}

	/**
	 * Agrega un bloque en particular en la estructura dinámica.
	 * 
	 * @param bloque
	 *            Bloque que se desea agregar
	 * @return
	 */
	@Override
	public List<DynamicSurveyBlock> addBlock(DynamicSurveyBlock bloque) {

		blocks.add(bloque);
		return blocks;
	}

	/**
	 * Inicializa la estructura dinámica.
	 * 
	 * <p>
	 * Es decir los bloques que conforman el formulario dinámico, teniendo en
	 * cuenta el tipo de cada bloque.
	 */
	private void initBlocks() {

		this.blocks = new ArrayList<DynamicSurveyBlock>();
		buildBlocksFromSurvey();
	}

	@Override
	public List<DynamicSurveyBlock> getBlocks() {

		if (blocks == null) {
			initBlocks();
		}
		return blocks;
	}

	public void setBlocks(List<DynamicSurveyBlock> blocks) {

		this.blocks = blocks;
	}

	/**
	 * Obtiene los bloques que se encuentran asociados a una plantilla.
	 * 
	 * <p>
	 * Los bloques definen la estructura dinámica del formulario.
	 * 
	 * @return lista de bloques contenidos en la plantilla
	 */
	public List<EncuestaPlantillaBloque> getBlocksByTemplate() {

		return templateBlockLogic.getBlocksByTemplate(getTemplate());
	}

	/**
	 * Obtiene las preguntas que se encuentran en un bloque especifico.
	 * 
	 * @param bloque
	 *            bloque del cual se desea obtener la lista de preguntas
	 * @return Lista de preguntas del bloque
	 */
	public List<EncuestaPlantillaPregunta> getQuestionsByBlock(
			EncuestaPlantillaBloque bloque) {

		return templateQuestionLogic.getQuestionsByBlock(bloque);
	}

	/**
	 * Carga las respuestas de un bloque en particular del tipo
	 * {@link DynamicSurveyDataTable}.
	 * 
	 * @param responseBlock
	 * @param bloque
	 *            bloque que almacenara las respuestas y las representara en el
	 *            dataTable
	 * @return
	 */
	public DynamicSurveyDataTable buildResponseGrill(
			List<EncuestaDetalle> responseBlock, DynamicSurveyDataTable bloque) {

		List<DynamicSurveyRow> registros = new ArrayList<DynamicSurveyRow>();

		int columnsNumber = bloque.getColumnsNumber();
		int nroFila = 1;
		DynamicSurveyRow row = new DynamicSurveyRow(bloque.getId(), nroFila,
				columnsNumber);

		int numeroPregunta = 0;
		for (EncuestaDetalle detalle : responseBlock) {

			numeroPregunta = detalle.getNumeroPregunta();
			if (nroFila != detalle.getNumeroFila()) {
				registros.add(row);
				nroFila = detalle.getNumeroFila();
				row = new DynamicSurveyRow(bloque.getId(), nroFila,
						columnsNumber);

			}

			SurveyField cell = DynamicSurveyField.fieldFactory(bloque
					.getQuestion(numeroPregunta));

			cell.setIndex(numeroPregunta);
			if (detalle.getRespuesta() != null) {
				cell.setValue(detalle.getRespuesta());
			} else {
				cell.setValue("");
			}
			row.addCell(cell);

		}
		registros.add(row);

		return bloque.buildRows(registros);
	}

	/**
	 * Carga las respuestas asociadas a un bloque en particular del tipo
	 * {@link DynamicSurveyFields}.
	 * 
	 * @param responseBlock
	 * @param block
	 * @return
	 */
	public DynamicSurveyFields buildResponseFields(
			List<EncuestaDetalle> responseBlock, DynamicSurveyFields block) {

		if (responseBlock != null) {
			for (EncuestaDetalle detalle : responseBlock) {

				DynamicSurveyFieldOption field = new DynamicSurveyFieldOption(
						block.getTypeField(detalle.getNumeroPregunta() - 1));
				SurveyField cell = DynamicSurveyField.fieldFactory(block
						.getQuestion(detalle.getNumeroPregunta()));

				cell.setIndex(detalle.getNumeroPregunta());
				if (detalle.getRespuesta() != null) {
					cell.setValue(detalle.getRespuesta());
				}
				// Obtenemos las respuestas que estan asociadas a un/os
				// detalle/s
				List<OpcionRespuesta> listResponse = getResponseSelected(detalle);
				if (field.isCheck()) {
					field.setManyOptions(listResponse);

				} else {
					if (field.isRadio() || field.isCombo()) {
						field.setOneOption(listResponse.get(0));
					}
				}

				field.setField(cell);
				block.addField(field);
			}
		}

		return block.buildFields();
	}

	/**
	 * Obtiene las respuestas relacionadas a un bloque en particular.
	 * 
	 * <p>
	 * Esto es, verifica si la los datos del formulario estan en memoria y de lo
	 * contrario obtiene las respuestas de la base de datos.
	 * 
	 * @param bloque
	 *            Bloque del cual se desea obtener las respuestas.
	 * @return Lista de respuestas relacionadas al bloque.
	 */
	public List<EncuestaDetalle> getResponseBlock(EncuestaPlantillaBloque bloque) {

		if (getBean().getId() == null) {
			return getResponseBlockMemory(bloque);
		} else {

			return encuestaDetalleLogic.getRespuestas(getBean(), bloque);

		}
	}

	/**
	 * Obtiene las respuestas en memoria del formulario dinámico.
	 * 
	 * @param bloque
	 *            Bloque en memoria cuyas respuestas se desean obtener.
	 * @return Lista de respuestas del bloque.
	 */
	private List<EncuestaDetalle> getResponseBlockMemory(
			EncuestaPlantillaBloque bloque) {

		List<EncuestaDetalle> respuestas = new ArrayList<EncuestaDetalle>();

		if (getBean().getDetalles() == null) {
			return respuestas;
		}

		for (EncuestaDetalle detalle : getBean().getDetalles()) {
			if (detalle.getPregunta().getBloque().getId()
					.equals(bloque.getId())) {

				respuestas.add(detalle);
			}
		}
		return respuestas;
	}

	/**
	 * 
	 * @param respuesta
	 *            Respuesta de la cual se desean obtener las opciones
	 *            seleccionadas (Si aplica)
	 * @return
	 */
	private List<OpcionRespuesta> getResponseSelected(EncuestaDetalle respuesta) {

		/*
		 * Si la respuesta esta persistida, obtiene las opciones seleccionadas
		 * persistidas
		 */
		if (respuesta.getId() != null) {
			return encuestaDetalleLogic.getRespuestasSelected(respuesta);
		} else {

			for (EncuestaDetalle detalle : getBean().getDetalles()) {

				if (detalle.getNumeroPregunta().equals(
						respuesta.getNumeroPregunta())) {

					List<EncuestaDetalleOpcionRespuesta> listResponse = encuestaDetalleLogic
							.getDetailsRespuestasSelected(detalle);

					if (ListHelper.hasElements(listResponse)) {
						detalle.setOpcionRespuesta(listResponse);
						List<OpcionRespuesta> result = new ArrayList<OpcionRespuesta>();

						for (EncuestaDetalleOpcionRespuesta detalleOpcionRespuesta : detalle
								.getOpcionRespuesta()) {

							result.add(detalleOpcionRespuesta
									.getOpcionRespuesta());

						}
						return result;
					}

				}
			}
			return Collections.emptyList();
		}

	}

	/**
	 * Inicializa una encuesta con la plantilla y fecha del día.
	 * 
	 * @param template
	 * @return
	 */
	public Encuesta getNewInstance(EncuestaPlantilla template) {

		Encuesta instance = getBaseLogic().getNewInstance();
		instance.setFechaRealizacion(new Date());
		instance.setPlantilla(template);
		return instance;
	}

	/**
	 * Construye un {@link EncuestaDetalle} por cada respuesta valida en la
	 * encuesta.
	 * 
	 * @param bloque
	 *            Bloque del cual se obtendran los datos.
	 */
	protected List<EncuestaDetalle> buildDetallesField(DynamicSurveyBlock bloque) {

		List<EncuestaDetalle> detalles = new ArrayList<EncuestaDetalle>();
		DynamicSurveyFields fields = (DynamicSurveyFields) bloque;
		int index = 1;
		for (DynamicSurveyFieldOption field : fields.getFields()) {
			EncuestaDetalle detalle = null;

			if (isEdit()) {
				EncuestaDetalle example = new EncuestaDetalle();
				example.setEncuesta(getBean());
				example.setPregunta(bloque.getQuestion(index));
				detalle = encuestaDetalleLogic.getByExample(example);

			}
			if (detalle == null) {
				detalle = new EncuestaDetalle();
				detalle.setEncuesta(getBean());
				detalle.setPregunta(bloque.getQuestion(index));
			}
			detalle.setOpcionRespuesta(new ArrayList<EncuestaDetalleOpcionRespuesta>());
			detalle.setRespuesta(field.getField().getValue());

			// Si permite multiples respuestas, cada respuesta es un
			// detalle
			/*
			 * Si es un Check
			 */
			if (field.isManyOptionResponse()) {
				for (OpcionRespuesta opcion : field.getManyOptions()) {
					detalle.addDetalleOpcionRespuesta(buildOpcionRespuesta(
							opcion, detalle));
				}
			} else {
				/*
				 * Si es un Radio
				 */
				if (field.isOneOptionResponse()) {
					detalle.addDetalleOpcionRespuesta(buildOpcionRespuesta(
							field.getOneOption(), detalle));
				}
			}
			if (StringUtils.isValid(detalle.getRespuesta())
					|| ListHelper.hasElements(detalle.getOpcionRespuesta())) {
				detalles.add(detalle);
			}
			index++;
		}

		return detalles;

	}

	/**
	 * @param opcion
	 * @param detalle
	 * @param detallesOpcionRespuesta
	 * @return
	 */
	private EncuestaDetalleOpcionRespuesta buildOpcionRespuesta(
			OpcionRespuesta opcion, EncuestaDetalle detalle) {

		EncuestaDetalleOpcionRespuesta detalleOpcionRespuesta = new EncuestaDetalleOpcionRespuesta();
		detalleOpcionRespuesta.setOpcionRespuesta(opcion);
		detalleOpcionRespuesta.setEncuestaDetalle(detalle);

		return detalleOpcionRespuesta;
	}

	/**
	 * Construye las respuestas del bloque {@link DynamicSurveyDataTable}
	 * 
	 * @param bloque
	 *            Bloque a construir
	 */
	protected List<EncuestaDetalle> buildDetallesDataTable(
			DynamicSurveyBlock bloque) {

		List<EncuestaDetalle> detalles = new ArrayList<EncuestaDetalle>();
		DynamicSurveyDataTable fields = (DynamicSurveyDataTable) bloque;
		int numberRow = 1;
		for (DynamicSurveyRow row : fields.getRows()) {
			for (int i = 0; i < row.getCells().length; i++) {
				// Solo almaceno los valores ingresados
				if (StringUtils.isValid(row.getCell(i).getValue())) {
					EncuestaDetalle detalle = null;
					if (isEdit()) {
						EncuestaDetalle example = new EncuestaDetalle();
						example.setEncuesta(getBean());
						example.setPregunta(bloque.getQuestion(i + 1));
						example.setNumeroFila(numberRow);
						detalle = encuestaDetalleLogic.getByExample(example);
					}
					if (detalle == null) {
						detalle = new EncuestaDetalle();
						detalle.setEncuesta(getBean());
						detalle.setNumeroFila(numberRow);
						detalle.setPregunta(bloque.getQuestion(i + 1));

					}
					detalle.setOpcionRespuesta(new ArrayList<EncuestaDetalleOpcionRespuesta>());
					detalle.setRespuesta(row.getCell(i).getValue());
					detalles.add(detalle);
				}
			}
			numberRow++;
		}
		return detalles;

	}

	@Override
	public void preCreate(EncuestaPlantilla template) {

		setBean(getNewInstance(template));
		log.debug("preCreate llamado");

	}

	@Override
	public String goCreate(EncuestaPlantilla template) {

		preCreate(template);
		mode = Mode.NEW;
		return goUrlSurvey();
	}

	@Override
	public String doCreate() {

		log.debug("Creando la encuesta definida...");

		buildSurveyFromForm();
		create();
		return postCreate();
	}

	@Override
	public void create() {

		getBaseLogic().add(getBean());
	}

	public String postCreate() {

		log.debug("postCreate llamado");
		return goUrlBack();
	}

	@Override
	public void preEdit(Encuesta encuesta) {

		setBean(encuesta);
		log.debug("preEdit llamado");

	}

	@Override
	public String goEdit(Encuesta encuesta) {

		preEdit(encuesta);
		mode = Mode.EDIT;
		return goUrlSurvey();
	}

	@Override
	public String doEdit() {

		log.debug("Editando la encuesta definida...");
		buildSurveyFromForm();
		edit();
		return postEdit();

	}

	@Override
	public void edit() {

		if (bean.getId() != null) {
			getBaseLogic().update(bean);
		}
	}

	public String postEdit() {

		log.debug("postEdit llamado");
		clear();
		return goUrlBack();
	}

	@Override
	public String doCancel() {

		return postCancel();
	}

	public String postCancel() {

		log.debug("postCancel llamado..");
		clear();
		return goUrlBack();
	}

	@Override
	public void preView(Encuesta encuesta) {

		setBean(encuesta);
		log.debug("preView llamado");

	}

	@Override
	public String goView(Encuesta encuesta) {

		preView(encuesta);
		mode = Mode.VIEW;
		return goUrlSurvey();
	}

	@Override
	public void preDelete(Encuesta encuesta) {

		setBean(encuesta);
		log.debug("preDelete llamado");

	}

	@Override
	public String goDelete(Encuesta encuesta) {

		preDelete(encuesta);
		mode = Mode.DELETE;
		return goUrlSurvey();
	}

	@Override
	public String doDelete() {

		log.debug("Eliminando la encuesta seleccionada..");
		if (bean.getId() != null) {
			bean.setId(bean.getId());
			getBaseLogic().remove(bean);
		}
		return postDelete();
	}

	public String postDelete() {

		log.debug("postDelete llamado..");
		clear();
		return goUrlBack();
	}

	public void clear() {

		setBlocks(null);
		setBean(null);
	}

	/**
	 * Página a dirijir para acceder al formulario dinámico(encuesta).
	 * 
	 * @return
	 */
	public abstract String goUrlSurvey();

	/**
	 * Pagína orignal de donde fue invocado el formulario dinámico.
	 * 
	 * @return
	 */
	public abstract String goUrlBack();

	@Override
	public Encuesta getBean() {

		if (bean == null) {
			bean = getBaseLogic().getNewInstance();
		}
		return bean;
	}

	@Override
	public void setBean(Encuesta bean) {

		this.bean = bean;
	}

	public EncuestaPlantilla getTemplate() {

		return getBean().getPlantilla();
	}

	public Mode getMode() {

		return mode;
	}

	public void setMode(Mode mode) {

		this.mode = mode;
	}

	@Override
	public boolean isNew() {

		return mode == Mode.NEW;
	}

	@Override
	public boolean isEdit() {

		return mode == Mode.EDIT;
	}

	@Override
	public boolean isView() {

		return mode == Mode.VIEW;
	}

	@Override
	public boolean isDelete() {

		return mode == Mode.DELETE;
	}

	public String getValueButtonCancel() {

		if (mode == null) {
			mode = Mode.VIEW;
		}
		String value = "";
		if (isView()) {
			value = "ABM_SURVEY_BUTTON_BACK";
		} else {
			value = "ABM_SURVEY_BUTTON_CANCEL";
		}

		return I18nHelper.getMessage(value);
	}

	@Override
	public boolean isEditable() {

		if (mode == null) {
			mode = Mode.VIEW;
		}
		if (isView() || isDelete()) {
			return false;
		}
		return true;
	}

}
