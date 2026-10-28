package co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.RegistrarNotaEstudianteCasoUso;
import co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante.DocenteDominio;
import co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante.EstudianteDominio;
import co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante.EstudianteGrupoDominio;
import co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante.GrupoDominio;
import co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante.NotaDominio;
import co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante.NotaEstudianteDominio;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.dominio.RegistrarNotaEstudianteDominio;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.impl.mapeador.RegistrarNotaEstudianteMapeadorCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionDTO;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteGrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.GrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaEstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

@Service
public class RegistrarNotaEstudianteCasoUsoImpl implements RegistrarNotaEstudianteCasoUso {
	private final DocenteRepositorio docenteRepositorio;
	private final EstudianteRepositorio estudianteRepositorio;
	private final GrupoRepositorio grupoRepositorio;
	private final EstudianteGrupoRepositorio estudianteGrupoRepositorio;
	private final NotaRepositorio notaRepositorio;
	private final NotaEstudianteRepositorio notaEstudianteRepositorio;
	private final NotificacionPuertoSalida notificacion;
	private final RegistrarNotaEstudianteMapeadorCasoUso mapeador;

	public RegistrarNotaEstudianteCasoUsoImpl(final DocenteRepositorio docenteRepositorio,
			final EstudianteRepositorio estudianteRepositorio, final GrupoRepositorio grupoRepositorio,
			final EstudianteGrupoRepositorio estudianteGrupoRepositorio, final NotaRepositorio notaRepositorio,
			final NotaEstudianteRepositorio notaEstudianteRepositorio, final NotificacionPuertoSalida notificacion,
			final RegistrarNotaEstudianteMapeadorCasoUso mapeador) {
		this.docenteRepositorio = docenteRepositorio;
		this.estudianteRepositorio = estudianteRepositorio;
		this.grupoRepositorio = grupoRepositorio;
		this.estudianteGrupoRepositorio = estudianteGrupoRepositorio;
		this.notaRepositorio = notaRepositorio;
		this.notaEstudianteRepositorio = notaEstudianteRepositorio;
		this.notificacion = notificacion;
		this.mapeador = mapeador;
	}

	@Override
	public Void ejecutar(final RegistrarNotaEstudianteDominio datos) {
		var valorEscalado = validarReglas(datos);
		var estudianteGrupo = estudianteGrupoRepositorio.consultarPorEstudianteYGrupo(datos.getIdEstudiante(), datos.getIdGrupo())
				.orElseThrow(() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.ESTUDIANTE_NO_PERTENECE_AL_GRUPO,
						datos.getIdEstudiante(), datos.getIdGrupo()));
		var nota = notaRepositorio.consultarPorId(datos.getIdNota())
				.orElseThrow(() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.NOTA_NO_EXISTE, datos.getIdNota()));
		notaEstudianteRepositorio.crear(mapeador.aEntidad(valorEscalado, estudianteGrupo, nota));
		notificarDespuesDeConfirmar(estudianteGrupo, nota.getNombre(), valorEscalado);
		return null;
	}

	private int validarReglas(final RegistrarNotaEstudianteDominio datos) {
		var grupo = new GrupoDominio(datos.getIdGrupo(), grupoRepositorio);
		grupo.validarGrupoExista();
		grupo.validarGrupoVigente();

		var docente = new DocenteDominio(datos.getIdDocente(), docenteRepositorio);
		docente.validarDocenteExista();
		docente.validarDocenteActivo();
		grupo.validarDocenteAsignado(datos.getIdDocente());

		new EstudianteDominio(datos.getIdEstudiante(), estudianteRepositorio).validarEstudianteExista();
		new EstudianteGrupoDominio(datos.getIdEstudiante(), datos.getIdGrupo(), estudianteGrupoRepositorio)
				.validarEstudianteExistaEnGrupo();

		var nota = new NotaDominio(datos.getIdNota(), notaRepositorio);
		nota.validarNotaExista();
		nota.validarNotaActiva();
		nota.validarNotaPertenezcaAlGrupo(datos.getIdGrupo());
		nota.validarFechaRegistroVigente();

		var valorEscalado = NotaEstudianteDominio.escalarValor(datos.getValor());
		var notaEstudiante = new NotaEstudianteDominio(datos.getIdEstudiante(), datos.getIdNota(), valorEscalado,
				notaEstudianteRepositorio);
		notaEstudiante.validarValorEnRango();
		notaEstudiante.validarNotaNoRegistrada();
		return valorEscalado;
	}

	private void notificarDespuesDeConfirmar(final EstudianteGrupoEntidad estudianteGrupo, final String nombreNota,
			final int valorEscalado) {
		var nombreEstudiante = estudianteGrupo.getEstudiante().getNombre();
		var aviso = new NotificacionDTO(nombreEstudiante, estudianteGrupo.getEstudiante().getCorreo(),
				CatalogoMensajesProveedor.contenido(CodigoMensaje.NOTIFICACION_NOTA_REGISTRADA_ASUNTO, nombreNota),
				CatalogoMensajesProveedor.contenido(CodigoMensaje.NOTIFICACION_NOTA_REGISTRADA_CONTENIDO, nombreEstudiante,
						nombreNota, BigDecimal.valueOf(valorEscalado, 2).toPlainString()));
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			notificacion.notificar(aviso);
			return;
		}
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				notificacion.notificar(aviso);
			}
		});
	}

}