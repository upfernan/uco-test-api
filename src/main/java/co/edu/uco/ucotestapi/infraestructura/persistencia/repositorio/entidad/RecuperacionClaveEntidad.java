package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad;

import java.time.Instant;
import java.util.UUID;

public class RecuperacionClaveEntidad {
	private UUID id;
	private UUID idUsuario;
	private String codigoHash;
	private Instant expiraEn;
	private int intentos;
	private boolean usado;
	private Instant creadoEn;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(UUID idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCodigoHash() {
		return codigoHash;
	}

	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}

	public Instant getExpiraEn() {
		return expiraEn;
	}

	public void setExpiraEn(Instant expiraEn) {
		this.expiraEn = expiraEn;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

	public Instant getCreadoEn() {
		return creadoEn;
	}

	public void setCreadoEn(Instant creadoEn) {
		this.creadoEn = creadoEn;
	}

}
