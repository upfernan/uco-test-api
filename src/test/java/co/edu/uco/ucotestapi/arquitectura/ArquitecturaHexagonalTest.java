package co.edu.uco.ucotestapi.arquitectura;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import co.edu.uco.ucotestapi.servicios.input.ports.CasoUsoConRetorno;
import co.edu.uco.ucotestapi.servicios.input.ports.CasoUsoSinRetorno;
import jakarta.persistence.Entity;

@AnalyzeClasses(packages = "co.edu.uco.ucotestapi", importOptions = ImportOption.DoNotIncludeTests.class)
class ArquitecturaHexagonalTest {

	private static final String DOMINIO = "..dominio..";
	private static final String SERVICIOS = "..servicios..";
	private static final String INFRAESTRUCTURA = "..infraestructura..";
	private static final String TRANSVERSAL = "..transversal..";

	// Capas

	@ArchTest
	static final ArchRule las_capas_respetan_sus_dependencias = layeredArchitecture()
			.consideringOnlyDependenciesInLayers()
			.layer("Dominio").definedBy(DOMINIO)
			.layer("Servicios").definedBy(SERVICIOS)
			.layer("Infraestructura").definedBy(INFRAESTRUCTURA)
			.layer("Transversal").definedBy(TRANSVERSAL)
			.whereLayer("Infraestructura").mayNotBeAccessedByAnyLayer()
			.whereLayer("Servicios").mayOnlyBeAccessedByLayers("Dominio", "Infraestructura", "Transversal")
			.whereLayer("Dominio").mayOnlyBeAccessedByLayers("Servicios", "Infraestructura", "Transversal");

	@ArchTest
	static final ArchRule el_dominio_no_depende_de_frameworks = noClasses()
			.that().resideInAPackage(DOMINIO)
			.should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "jakarta.persistence..");

	@ArchTest
	static final ArchRule los_utilitarios_no_dependen_de_otras_capas = noClasses()
			.that().resideInAPackage("..transversal.utilitario..")
			.should().dependOnClassesThat().resideInAnyPackage(DOMINIO, SERVICIOS, INFRAESTRUCTURA);

	// Dominio

	@ArchTest
	static final ArchRule los_objetos_de_dominio_estan_en_el_dominio = classes()
			.that().haveSimpleNameEndingWith("Dominio")
			.should().resideInAPackage("..dominio..");

	// Puertos de entrada y casos de uso

	@ArchTest
	static final ArchRule los_puertos_de_entrada_son_interfaces = classes()
			.that().resideInAPackage("..servicios.input.ports..")
			.should().beInterfaces();

	@ArchTest
	static final ArchRule los_casos_de_uso_implementan_un_puerto_de_entrada = classes()
			.that().haveSimpleNameEndingWith("CasoUsoImpl")
			.should().resideInAPackage("..servicios.casosuso..")
			.andShould().beAssignableTo(CasoUsoConRetorno.class)
			.orShould().beAssignableTo(CasoUsoSinRetorno.class);

	// Puertos de salida y adaptadores

	@ArchTest
	static final ArchRule los_puertos_de_salida_son_interfaces = classes()
			.that().resideInAPackage("..servicios.output.ports..")
			.and().haveSimpleNameEndingWith("Repositorio")
			.or().haveSimpleNameEndingWith("Servicio")
			.should().beInterfaces();

	@ArchTest
	static final ArchRule los_adaptadores_implementan_un_puerto_de_salida = classes()
			.that().haveSimpleNameEndingWith("Adapter")
			.should().resideInAPackage("..infraestructura.output..")
			.andShould().implement(resideInAPackage("..servicios.output.ports.."));

	@ArchTest
	static final ArchRule los_repositorios_jpa_estan_en_su_paquete = classes()
			.that().haveSimpleNameEndingWith("JPARepositorio")
			.should().resideInAPackage("..infraestructura.output.repositorios.estructurados.jpa.repositorios..");

	@ArchTest
	static final ArchRule las_entidades_jpa_estan_en_su_paquete = classes()
			.that().areAnnotatedWith(Entity.class)
			.should().resideInAPackage("..infraestructura.output.repositorios.estructurados.jpa.entidades..");

	@ArchTest
	static final ArchRule los_mappers_estan_en_su_paquete = classes()
			.that().haveSimpleNameEndingWith("EntidadMapper")
			.should().resideInAPackage("..infraestructura.output.repositorios.estructurados.mapper..");

	// Entrada (REST)

	@ArchTest
	static final ArchRule los_controladores_estan_en_su_paquete = classes()
			.that().areAnnotatedWith(RestController.class)
			.should().resideInAPackage("..infraestructura.input.controladores..");

	@ArchTest
	static final ArchRule los_controladores_solo_usan_puertos_de_entrada = noClasses()
			.that().resideInAPackage("..infraestructura.input..")
			.should().dependOnClassesThat().resideInAnyPackage("..infraestructura.output..", "..servicios.output..",
					"..servicios.casosuso..");

	// Buenas prácticas generales

	@ArchTest
	static final ArchRule sin_inyeccion_por_atributo = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

	@ArchTest
	static final ArchRule sin_system_out = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

	@ArchTest
	static final ArchRule sin_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

}
