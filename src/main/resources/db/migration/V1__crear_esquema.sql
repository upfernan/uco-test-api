CREATE TABLE docente (
    id     UUID         PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    estado VARCHAR(20)  NOT NULL CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE estudiante (
    id     UUID         PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE grupo (
    id           UUID         PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    id_docente   UUID         NOT NULL REFERENCES docente (id),
    estado       VARCHAR(20)  NOT NULL CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    fecha_inicio DATE         NOT NULL,
    fecha_fin    DATE         NOT NULL,
    CHECK (fecha_inicio <= fecha_fin)
);

CREATE TABLE estudiante_grupo (
    id            UUID PRIMARY KEY,
    id_estudiante UUID NOT NULL REFERENCES estudiante (id),
    id_grupo      UUID NOT NULL REFERENCES grupo (id),
    UNIQUE (id_estudiante, id_grupo)
);

CREATE TABLE nota (
    id                   UUID         PRIMARY KEY,
    nombre               VARCHAR(100) NOT NULL,
    id_grupo             UUID         NOT NULL REFERENCES grupo (id),
    fecha_registro_desde TIMESTAMPTZ  NOT NULL,
    fecha_registro_hasta TIMESTAMPTZ  NOT NULL,
    estado               VARCHAR(20)  NOT NULL CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    CHECK (fecha_registro_desde <= fecha_registro_hasta)
);

CREATE TABLE nota_estudiante (
    id                  UUID    PRIMARY KEY,
    id_estudiante_grupo UUID    NOT NULL REFERENCES estudiante_grupo (id),
    id_nota             UUID    NOT NULL REFERENCES nota (id),
    valor               INTEGER NOT NULL CHECK (valor BETWEEN 0 AND 500),
    UNIQUE (id_estudiante_grupo, id_nota)
);