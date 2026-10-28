package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recuperacion_clave")
public class RecuperacionClaveEntidadJpa {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "id_usuario")
	private UUID idUsuario;

	@Column(name = "codigo_hash")
	private String codigoHash;

	@Column(name = "expira_en")
	private Instant expiraEn;

	@Column(name = "intentos")
	private int intentos;

	@Column(name = "usado")
	private boolean usado;

	@Column(name = "creado_en")
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
