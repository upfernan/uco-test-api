package co.edu.uco.ucotestapi.arquitectura;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import co.edu.uco.ucotestapi.servicios.casosuso.CasoUsoConRetorno;
import co.edu.uco.ucotestapi.servicios.casosuso.CasoUsoSinRetorno;
import jakarta.persistence.Entity;

@AnalyzeClasses(packages = "co.edu.uco.ucotestapi", importOptions = ImportOption.DoNotIncludeTests.class)
class ArquitecturaHexagonalTest {

	private static final String DOMINIO = "..dominio..";
	private static final String DOMINIO_NOTA_ESTUDIANTE = "..dominionotaestudiante..";
	private static final String SERVICIOS = "..servicios..";
	private static final String INFRAESTRUCTURA = "..infraestructura..";
	private static final String TRANSVERSAL = "..transversal..";

	@ArchTest
	static final ArchRule el_dominio_no_depende_de_la_infraestructura = noClasses()
			.that().resideInAnyPackage(DOMINIO, DOMINIO_NOTA_ESTUDIANTE)
			.should().dependOnClassesThat().resideInAPackage(INFRAESTRUCTURA);

	@ArchTest
	static final ArchRule el_dominio_no_depende_de_frameworks = noClasses()
			.that().resideInAnyPackage(DOMINIO, DOMINIO_NOTA_ESTUDIANTE)
			.should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "jakarta.persistence..");

	@ArchTest
	static final ArchRule los_servicios_no_dependen_de_la_infraestructura = noClasses()
			.that().resideInAPackage(SERVICIOS)
			.should().dependOnClassesThat().resideInAPackage(INFRAESTRUCTURA);

	@ArchTest
	static final ArchRule lo_transversal_no_depende_de_la_infraestructura = noClasses()
			.that().resideInAPackage(TRANSVERSAL)
			.should().dependOnClassesThat().resideInAPackage(INFRAESTRUCTURA);

	@ArchTest
	static final ArchRule los_utilitarios_no_dependen_de_otras_capas = noClasses()
			.that().resideInAPackage("..transversal.utilitario..")
			.should().dependOnClassesThat().resideInAnyPackage(DOMINIO, DOMINIO_NOTA_ESTUDIANTE, SERVICIOS, INFRAESTRUCTURA);

	@ArchTest
	static final ArchRule los_controladores_solo_usan_casos_de_uso = noClasses()
			.that().resideInAPackage("..infraestructura.input..")
			.should().dependOnClassesThat().resideInAnyPackage("..infraestructura.output..", "..servicios.output..");

	@ArchTest
	static final ArchRule los_adaptadores_estan_en_la_infraestructura_de_salida = classes()
			.that().haveSimpleNameEndingWith("Adapter")
			.should().resideInAPackage("..infraestructura.output..");

	@ArchTest
	static final ArchRule los_casos_de_uso_implementan_un_contrato = classes()
			.that().haveSimpleNameEndingWith("CasoUsoImpl")
			.should().resideInAPackage("..servicios.casosuso..")
			.andShould().beAssignableTo(CasoUsoConRetorno.class)
			.orShould().beAssignableTo(CasoUsoSinRetorno.class);

	@ArchTest
	static final ArchRule las_entidades_jpa_estan_en_su_paquete = classes()
			.that().areAnnotatedWith(Entity.class)
			.should().resideInAPackage("..infraestructura.output.repositorios.estructurados.jpa..");

	@ArchTest
	static final ArchRule sin_inyeccion_por_atributo = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

	@ArchTest
	static final ArchRule sin_system_out = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

	@ArchTest
	static final ArchRule sin_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

}
