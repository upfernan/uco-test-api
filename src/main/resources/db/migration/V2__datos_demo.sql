-- Datos para la demo: el docente del grupo de Matematicas registra la nota 3.5 a un estudiante.
-- Los IDs son fijos para poder usarlos en las pruebas de Bruno.

INSERT INTO docente (id, nombre, estado) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Docente Demo', 'ACTIVO'),
    ('11111111-1111-1111-1111-222222222222', 'Docente Inactivo', 'INACTIVO');

INSERT INTO estudiante (id, nombre) VALUES
    ('22222222-2222-2222-2222-111111111111', 'Estudiante Demo'),
    ('22222222-2222-2222-2222-222222222222', 'Estudiante Sin Grupo');

INSERT INTO grupo (id, nombre, id_docente, estado, fecha_inicio, fecha_fin) VALUES
    ('33333333-3333-3333-3333-111111111111', 'Matematicas', '11111111-1111-1111-1111-111111111111', 'ACTIVO', '2026-01-01', '2030-12-31');

INSERT INTO estudiante_grupo (id, id_estudiante, id_grupo) VALUES
    ('44444444-4444-4444-4444-111111111111', '22222222-2222-2222-2222-111111111111', '33333333-3333-3333-3333-111111111111');

INSERT INTO nota (id, nombre, id_grupo, fecha_registro_desde, fecha_registro_hasta, estado) VALUES
    ('55555555-5555-5555-5555-111111111111', 'Parcial 1', '33333333-3333-3333-3333-111111111111', '2026-01-01T00:00:00Z', '2030-12-31T23:59:59Z', 'ACTIVO'),
    ('55555555-5555-5555-5555-222222222222', 'Quiz cerrado', '33333333-3333-3333-3333-111111111111', '2026-01-01T00:00:00Z', '2026-02-01T00:00:00Z', 'ACTIVO'),
    ('55555555-5555-5555-5555-333333333333', 'Taller anulado', '33333333-3333-3333-3333-111111111111', '2026-01-01T00:00:00Z', '2030-12-31T23:59:59Z', 'INACTIVO');