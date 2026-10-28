-- Cuentas para que docentes y estudiantes se registren e inicien sesion.

ALTER TABLE docente ADD COLUMN correo VARCHAR(150) UNIQUE;
ALTER TABLE estudiante ADD COLUMN correo VARCHAR(150) UNIQUE;

CREATE TABLE usuario (
    id            UUID         PRIMARY KEY,
    correo        VARCHAR(150) NOT NULL UNIQUE,
    clave_hash    VARCHAR(100) NOT NULL,
    rol           VARCHAR(20)  NOT NULL CHECK (rol IN ('DOCENTE', 'ESTUDIANTE')),
    id_docente    UUID         UNIQUE REFERENCES docente (id),
    id_estudiante UUID         UNIQUE REFERENCES estudiante (id),
    CHECK ((rol = 'DOCENTE' AND id_docente IS NOT NULL AND id_estudiante IS NULL)
        OR (rol = 'ESTUDIANTE' AND id_estudiante IS NOT NULL AND id_docente IS NULL))
);

-- Correos de los datos de demo: al registrarse con ellos, la cuenta se asocia al registro existente.
UPDATE docente SET correo = 'docente.demo@uco-test-api.test' WHERE id = '11111111-1111-1111-1111-111111111111';
UPDATE docente SET correo = 'docente.inactivo@uco-test-api.test' WHERE id = '11111111-1111-1111-1111-222222222222';
UPDATE estudiante SET correo = 'estudiante.demo@uco-test-api.test' WHERE id = '22222222-2222-2222-2222-111111111111';
UPDATE estudiante SET correo = 'estudiante.singrupo@uco-test-api.test' WHERE id = '22222222-2222-2222-2222-222222222222';
