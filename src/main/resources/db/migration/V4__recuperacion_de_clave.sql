-- Codigos para recuperar la contrasena. El codigo se guarda cifrado (BCrypt), vence y tiene intentos limitados.

CREATE TABLE recuperacion_clave (
    id          UUID         PRIMARY KEY,
    id_usuario  UUID         NOT NULL REFERENCES usuario (id),
    codigo_hash VARCHAR(100) NOT NULL,
    expira_en   TIMESTAMPTZ  NOT NULL,
    intentos    INTEGER      NOT NULL DEFAULT 0 CHECK (intentos >= 0),
    usado       BOOLEAN      NOT NULL DEFAULT FALSE,
    creado_en   TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_recuperacion_clave_usuario ON recuperacion_clave (id_usuario, usado);
