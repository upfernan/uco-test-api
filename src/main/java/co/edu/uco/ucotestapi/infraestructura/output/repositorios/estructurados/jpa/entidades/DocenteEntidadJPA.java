package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.EstadoDocente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Docente")
public class DocenteEntidadJPA {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "correo")
	private String correo;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	private EstadoDocente estado;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}


	public EstadoDocente getEstado() {
		return estado;
	}

	public void setEstado(EstadoDocente estado) {
		this.estado = estado;
	}

}
