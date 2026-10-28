package co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.registrarnotaestudiante;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante.DocenteDominio;
import co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante.EstudianteDominio;
import co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante.EstudianteGrupoDominio;
import co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante.GrupoDominio;
import co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante.NotaDominio;
import co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante.NotaEstudianteDominio;
import co.edu.uco.ucotestapi.servicios.input.dto.request.RegistrarNotaEstudianteDTO;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoEstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaEstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEstudianteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CatalogoMensajesServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class RegistrarNotaEstudianteCasoUsoImpl implements RegistrarNotaEstudianteCasoUso {
	private static final RegistrarNotaEstudianteDTO DATOS_VACIOS = new RegistrarNotaEstudianteDTO(UtilUUID.UUID_DEFECTO,
			UtilUUID.UUID_DEFECTO, UtilUUID.UUID_DEFECTO, UtilUUID.UUID_DEFECTO, BigDecimal.ZERO);

	private final DocenteRepositorio docenteRepositorio;
	private final EstudianteRepositorio estudianteRepositorio;
	private final GrupoRepositorio grupoRepositorio;
	private final GrupoEstudianteRepositorio grupoEstudianteRepositorio;
	private final NotaRepositorio notaRepositorio;
	private final NotaEstudianteRepositorio notaEstudianteRepositorio;
	private final CatalogoMensajesServicio catalogoMensajes;

	public RegistrarNotaEstudianteCasoUsoImpl(DocenteRepositorio docenteRepositorio,
			EstudianteRepositorio estudianteRepositorio, GrupoRepositorio grupoRepositorio,
			GrupoEstudianteRepositorio grupoEstudianteRepositorio, NotaRepositorio notaRepositorio,
			NotaEstudianteRepositorio notaEstudianteRepositorio, CatalogoMensajesServicio catalogoMensajes) {
		this.docenteRepositorio = docenteRepositorio;
		this.estudianteRepositorio = estudianteRepositorio;
		this.grupoRepositorio = grupoRepositorio;
		this.grupoEstudianteRepositorio = grupoEstudianteRepositorio;
		this.notaRepositorio = notaRepositorio;
		this.notaEstudianteRepositorio = notaEstudianteRepositorio;
		this.catalogoMensajes = catalogoMensajes;
	}

	@Override
	@Transactional
	public void ejecutar(RegistrarNotaEstudianteDTO datos) {
		try {
			var datosValidos = UtilObjeto.obtenerValorDefecto(datos, DATOS_VACIOS);
			var valorEscalado = validarReglas(datosValidos);
			registrarNota(datosValidos, valorEscalado);
		} catch (ReglaNegocioException excepcion) {
			throw UcoTestApiExcepcion.crear(excepcion,
					mensaje(excepcion.getCodigo(), excepcion.getParametros()),
					mensaje(CodigoMensaje.REGLA_NEGOCIO_INCUMPLIDA_TECNICO, excepcion.getMessage()));
		} catch (UcoTestApiExcepcion excepcion) {
			throw excepcion;
		} catch (Exception excepcion) {
			throw UcoTestApiExcepcion.crear(excepcion,
					mensaje(CodigoMensaje.REGISTRAR_NOTA_ERROR_INESPERADO_USUARIO),
					mensaje(CodigoMensaje.REGISTRAR_NOTA_ERROR_INESPERADO_TECNICO, excepcion.getMessage()));
		}
	}

	private String mensaje(CodigoMensaje codigo, Object... parametros) {
		return catalogoMensajes.consultarMensaje(codigo, parametros).contenido();
	}

	private int validarReglas(RegistrarNotaEstudianteDTO datos) {
		var grupo = new GrupoDominio(datos.idGrupo(), grupoRepositorio);
		grupo.validarGrupoExista();
		grupo.validarGrupoVigente();

		var docente = new DocenteDominio(datos.idDocente(), docenteRepositorio);
		docente.validarDocenteExista();
		docente.validarDocenteActivo();
		grupo.validarDocenteAsignado(datos.idDocente());

		new EstudianteDominio(datos.idEstudiante(), estudianteRepositorio).validarEstudianteExista();
		new EstudianteGrupoDominio(datos.idEstudiante(), datos.idGrupo(), grupoEstudianteRepositorio)
				.validarEstudianteExistaEnGrupo();

		var nota = new NotaDominio(datos.idNota(), notaRepositorio);
		nota.validarNotaExista();
		nota.validarNotaActiva();
		nota.validarNotaPertenezcaAlGrupo(datos.idGrupo());
		nota.validarFechaRegistroVigente();

		var valorEscalado = NotaEstudianteDominio.escalarValor(datos.valor());
		var notaEstudiante = new NotaEstudianteDominio(datos.idEstudiante(), datos.idNota(), valorEscalado,
				notaEstudianteRepositorio);
		notaEstudiante.validarValorEnRango();
		notaEstudiante.validarNotaNoRegistrada();
		return valorEscalado;
	}

	private void registrarNota(RegistrarNotaEstudianteDTO datos, int valorEscalado) {
		var estudianteGrupo = grupoEstudianteRepositorio
				.consultarPorEstudianteYGrupo(datos.idEstudiante(), datos.idGrupo())
				.orElseThrow(() -> new ReglaNegocioException(CodigoMensaje.ESTUDIANTE_NO_PERTENECE_AL_GRUPO,
						datos.idEstudiante(), datos.idGrupo()));
		var nota = notaRepositorio.consultarPorId(datos.idNota())
				.orElseThrow(() -> new ReglaNegocioException(CodigoMensaje.NOTA_NO_EXISTE, datos.idNota()));

		var notaEstudiante = new NotaEstudianteEntidad();
		notaEstudiante.setId(UtilUUID.generar());
		notaEstudiante.setEstudianteGrupo(estudianteGrupo);
		notaEstudiante.setNota(nota);
		notaEstudiante.setValor(valorEscalado);
		notaEstudianteRepositorio.crear(notaEstudiante);
	}

}