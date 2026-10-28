package co.edu.uco.ucotestapi.arquitectura;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import co.edu.uco.ucotestapi.aplicacion.casouso.CasoUso;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.Regla;
import co.edu.uco.ucotestapi.aplicacion.casouso.validador.Validador;
import co.edu.uco.ucotestapi.aplicacion.puertoentrada.PuertoEntrada;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;
import jakarta.persistence.Entity;

@AnalyzeClasses(packages = "co.edu.uco.ucotestapi", importOptions = ImportOption.DoNotIncludeTests.class)
class ArquitecturaHexagonalTest {

	private static final String APLICACION = "..ucotestapi.aplicacion..";
	private static final String FUNCIONALIDADES = "..ucotestapi.funcionalidades..";
	private static final String DOMINIO = "..ucotestapi.dominio..";
	private static final String INFRAESTRUCTURA = "..ucotestapi.infraestructura..";
	private static final String CONTROLADOR = "..infraestructura.controlador..";
	private static final String ADAPTADOR_REPOSITORIO = "..persistencia.repositorio.adaptador..";
	private static final String SQL_JPA = "..persistencia.repositorio.sql..";

	// Límites entre capas

	@ArchTest
	static final ArchRule la_aplicacion_no_conoce_implementaciones_de_infraestructura = noClasses()
			.that().resideInAnyPackage(APLICACION, FUNCIONALIDADES, DOMINIO)
			.should().dependOnClassesThat().resideInAnyPackage(CONTROLADOR, ADAPTADOR_REPOSITORIO, SQL_JPA,
					"..persistencia.configuracion..", "..serviciosexternos.identidad..",
					"..serviciosexternos.notificaciones..", "..serviciosexternos.configuracion..",
					"..catalogomensajes.propiedades..", "..ucotestapi.inicializador..")
			.because("los casos de uso solo usan puertos: interfaces de repositorio, entidades y el catálogo de mensajes");

	@ArchTest
	static final ArchRule la_aplicacion_no_depende_de_la_web_ni_de_jpa = noClasses()
			.that().resideInAnyPackage(APLICACION, FUNCIONALIDADES, DOMINIO)
			.should().dependOnClassesThat().resideInAnyPackage("org.springframework.web..", "jakarta.persistence..",
					"org.springframework.data..");

	@ArchTest
	static final ArchRule los_controladores_solo_usan_puertos_de_entrada = noClasses()
			.that().resideInAPackage(CONTROLADOR)
			.should().dependOnClassesThat().resideInAnyPackage("..aplicacion.casouso..", "..aplicacion.puertosalida..",
					"..persistencia..")
			.because("un controlador solo habla con el puerto de entrada de su funcionalidad");

	@ArchTest
	static final ArchRule nadie_fuera_de_persistencia_usa_jpa_directamente = noClasses()
			.that().resideOutsideOfPackages(ADAPTADOR_REPOSITORIO, SQL_JPA)
			.should().dependOnClassesThat().resideInAPackage(SQL_JPA);

	@ArchTest
	static final ArchRule las_funcionalidades_no_dependen_entre_si = slices()
			.matching("..funcionalidades.(*).(*)..")
			.should().notDependOnEachOther();

	@ArchTest
	static final ArchRule lo_transversal_no_depende_de_las_capas = noClasses()
			.that().resideInAnyPackage("..transversal.utilitario..", "..transversal.especificacion..",
					"..transversal.enumeracion..", "..transversal.parametros..", "..transversal.respuesta..")
			.should().dependOnClassesThat().resideInAnyPackage(APLICACION, FUNCIONALIDADES, DOMINIO, INFRAESTRUCTURA);

	// Puerto de entrada

	@ArchTest
	static final ArchRule los_puertos_de_entrada_son_interfaces = classes()
			.that().haveSimpleNameEndingWith("PuertoEntrada")
			.and().haveSimpleNameNotStartingWith("Mapeador")
			.and().haveSimpleNameNotContaining("MapeadorPuertoEntrada")
			.should().beInterfaces()
			.andShould().resideInAPackage("..aplicacion.puertoentrada")
			.andShould().beAssignableTo(PuertoEntrada.class);

	@ArchTest
	static final ArchRule los_interactores_implementan_el_puerto_de_entrada = classes()
			.that().haveSimpleNameEndingWith("Interactor")
			.should().resideInAPackage("..aplicacion.puertoentrada.impl")
			.andShould().beAssignableTo(PuertoEntrada.class);

	@ArchTest
	static final ArchRule los_dto_de_entrada_estan_en_su_paquete = classes()
			.that().resideInAPackage(FUNCIONALIDADES)
			.and().haveSimpleNameEndingWith("DTO")
			.should().resideInAnyPackage("..puertoentrada.dto", "..puertosalida");

	// Caso de uso

	@ArchTest
	static final ArchRule los_casos_de_uso_estan_en_su_paquete = classes()
			.that().haveSimpleNameEndingWith("CasoUsoImpl")
			.should().resideInAPackage("..aplicacion.casouso.impl")
			.andShould().beAssignableTo(CasoUso.class);

	@ArchTest
	static final ArchRule los_objetos_de_dominio_estan_en_el_caso_de_uso = classes()
			.that().haveSimpleNameEndingWith("Dominio")
			.and().haveSimpleNameNotStartingWith("Mapeador")
			.should().resideInAnyPackage("..aplicacion.casouso.dominio", DOMINIO);

	@ArchTest
	static final ArchRule los_validadores_estan_en_su_paquete = classes()
			.that().haveSimpleNameStartingWith("Validar")
			.should().resideInAnyPackage("..casouso.dominio.validador..", "..casouso.validador..");

	@ArchTest
	static final ArchRule los_validadores_generales_implementan_validador = classes()
			.that().resideInAPackage("..aplicacion.casouso.validador.generales..")
			.should().implement(Validador.class);

	@ArchTest
	static final ArchRule las_reglas_estan_en_su_paquete = classes()
			.that().implement(Regla.class)
			.should().resideInAPackage("..aplicacion.casouso.regla..")
			.andShould().haveSimpleNameEndingWith("Regla");

	@ArchTest
	static final ArchRule las_especificaciones_estan_en_su_paquete = classes()
			.that().areAssignableTo(Especificacion.class)
			.should().resideInAnyPackage("..aplicacion.casouso.especificacion..", "..transversal.especificacion.base..")
			.andShould().haveSimpleNameEndingWith("Especificacion");

	// Puertos de salida y adaptadores

	@ArchTest
	static final ArchRule los_puertos_de_salida_son_interfaces = classes()
			.that().haveSimpleNameEndingWith("PuertoSalida")
			.should().beInterfaces()
			.andShould().resideInAPackage("..aplicacion.puertosalida..");

	@ArchTest
	static final ArchRule los_repositorios_son_interfaces = classes()
			.that().resideInAPackage("..persistencia.repositorio")
			.should().beInterfaces()
			.andShould().haveSimpleNameEndingWith("Repositorio");

	@ArchTest
	static final ArchRule los_adaptadores_de_repositorio_estan_en_su_paquete = classes()
			.that().haveSimpleNameEndingWith("RepositorioAdaptador")
			.should().resideInAPackage("..persistencia.repositorio.adaptador.sql.jpa");

	@ArchTest
	static final ArchRule los_adaptadores_viven_en_infraestructura = classes()
			.that().haveSimpleNameEndingWith("Adaptador")
			.should().resideInAPackage(INFRAESTRUCTURA);

	@ArchTest
	static final ArchRule los_repositorios_jpa_estan_en_su_paquete = classes()
			.that().haveSimpleNameEndingWith("JpaRepositorio")
			.should().resideInAPackage("..persistencia.repositorio.sql.jpa");

	@ArchTest
	static final ArchRule las_entidades_jpa_estan_en_su_paquete = classes()
			.that().areAnnotatedWith(Entity.class)
			.should().resideInAPackage("..persistencia.repositorio.sql.jpa.entidad")
			.andShould().haveSimpleNameEndingWith("EntidadJpa");

	@ArchTest
	static final ArchRule los_mapeadores_jpa_estan_en_su_paquete = classes()
			.that().haveSimpleNameEndingWith("MapeadorJpa")
			.and().areNotInterfaces()
			.should().resideInAPackage("..persistencia.repositorio.adaptador.sql.jpa.mapeador");

	// Controladores

	@ArchTest
	static final ArchRule los_controladores_estan_en_su_paquete = classes()
			.that().areAnnotatedWith(RestController.class)
			.should().resideInAPackage("..infraestructura.controlador")
			.andShould().haveSimpleNameEndingWith("Controlador");

	// Buenas prácticas generales

	@ArchTest
	static final ArchRule sin_inyeccion_por_atributo = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

	@ArchTest
	static final ArchRule sin_system_out = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

	@ArchTest
	static final ArchRule sin_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

}
