-- Insertar opciones raiz (sin padre)
INSERT INTO opciones (nombre, ruta, icono, orden, opcion_padre_id, activo)
VALUES
  ('Dashboard', '/dashboard', 'dashboard', 1, NULL, true),
  ('Productos', '/productos', 'product', 2, NULL, true),
  ('Transacciones', '/transacciones', 'transaction', 3, NULL, true),
  ('Administración', '/admin', 'admin', 4, NULL, true),
  ('Reportes', '/reportes', 'report', 5, NULL, true);

-- Insertar sub-opciones (con padre)
INSERT INTO opciones (nombre, ruta, icono, orden, opcion_padre_id, activo)
VALUES
  ('Crear Producto', '/productos/crear', 'add', 1, 2, true),
  ('Listar Productos', '/productos/lista', 'list', 2, 2, true),
  ('Depositar', '/transacciones/depositar', 'deposit', 1, 3, true),
  ('Retirar', '/transacciones/retirar', 'withdraw', 2, 3, true),
  ('Transferencia', '/transacciones/transferir', 'transfer', 3, 3, true),
  ('Usuarios', '/admin/usuarios', 'user', 1, 4, true),
  ('Configuración', '/admin/config', 'settings', 2, 4, true);
