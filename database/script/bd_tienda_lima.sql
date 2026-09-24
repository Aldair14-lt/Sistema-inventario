-- =================================================================
-- SISTEMA DE INVENTARIO - BODEGA "LIMA"
-- Motor: PostgreSQL 14+
-- Base de Datos: bd_tienda_lima
-- =================================================================
 
-- 1. TABLA: ROLES
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);
 
-- 2. TABLA: USUARIOS
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(15),
    estado BOOLEAN DEFAULT TRUE,
    rol_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
);
 
-- 3. TABLA: CATEGORIAS
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    estado BOOLEAN DEFAULT TRUE
);
 
-- 4. TABLA: PROVEEDORES
CREATE TABLE proveedores (
    id SERIAL PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    razon_social VARCHAR(150) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(120),
    direccion VARCHAR(200),
    estado BOOLEAN DEFAULT TRUE
);
 
-- 5. TABLA: PRODUCTOS
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    codigo_barras VARCHAR(50) UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio_compra NUMERIC(10, 2) NOT NULL CHECK (precio_compra >= 0),
    precio_venta NUMERIC(10, 2) NOT NULL CHECK (precio_venta >= 0),
    stock_actual INT NOT NULL DEFAULT 0 CHECK (stock_actual >= 0),
    stock_minimo INT NOT NULL DEFAULT 5 CHECK (stock_minimo >= 0),
    categoria_id INT NOT NULL,
    proveedor_id INT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_producto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
);
 
-- 6. TABLA: CLIENTES
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    num_documento VARCHAR(15) UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(120),
    direccion VARCHAR(200)
);
 
-- 7. TABLA: VENTAS
CREATE TABLE ventas (
    id SERIAL PRIMARY KEY,
    serie VARCHAR(10) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subtotal NUMERIC(10, 2) NOT NULL CHECK (subtotal >= 0),
    igv NUMERIC(10, 2) NOT NULL CHECK (igv >= 0),
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    usuario_id INT NOT NULL,
    cliente_id INT,
    estado VARCHAR(20) DEFAULT 'COMPLETADA', -- 'COMPLETADA', 'ANULADA'
    CONSTRAINT fk_venta_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);
 
-- 8. TABLA: DETALLE_VENTAS
CREATE TABLE detalle_ventas (
    id SERIAL PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(10, 2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);
 
-- 9. TABLA: COMPRAS
CREATE TABLE compras (
    id SERIAL PRIMARY KEY,
    serie VARCHAR(10) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    proveedor_id INT NOT NULL,
    usuario_id INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'COMPLETADA',
    CONSTRAINT fk_compra_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id),
    CONSTRAINT fk_compra_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
 
-- 10. TABLA: DETALLE_COMPRAS
CREATE TABLE detalle_compras (
    id SERIAL PRIMARY KEY,
    compra_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(10, 2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id) REFERENCES compras(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_compra_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);
 
-- 11. TABLA: MOVIMIENTOS_INVENTARIO
CREATE TABLE movimientos_inventario (
    id SERIAL PRIMARY KEY,
    producto_id INT NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL, -- 'ENTRADA', 'SALIDA', 'AJUSTE'
    cantidad INT NOT NULL,
    motivo VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT NOT NULL,
    CONSTRAINT fk_movimiento_producto FOREIGN KEY (producto_id) REFERENCES productos(id),
    CONSTRAINT fk_movimiento_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
 
-- INSERCIÓN DE DATOS INICIALES
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador total del sistema'),
('CAJERO', 'Encargado de registro de ventas y caja'),
('ALMACENERO', 'Encargado de inventario y recepción de productos');
 
INSERT INTO usuarios (nombre, apellido, email, password, telefono, rol_id) VALUES
('Admin', 'Lima', 'admin@tiendalima.com', '$2a$10$e844S3zP3.g4I5d68846O.EwO1l1i2wA.sN4', '966000111', 1);
 
INSERT INTO categorias (nombre, descripcion) VALUES
('Abarrotes Básicos', 'Arroz, azúcar, fideos, aceites y legumbres'),
('Lácteos y Huevos', 'Leche, yogures, quesos y mantequillas'),
('Bebidas y Gaseosas', 'Agua, refrescos, cervezas y jugos'),
('Limpieza del Hogar', 'Detergentes, desinfectantes y jabones');
 
INSERT INTO proveedores (ruc, razon_social, telefono, email, direccion) VALUES
('20100012341', 'Distribuidora Central S.A.C.', '014567890', 'ventas@central.com', 'Av. Argentina 1230, Lima'),
('20555666771', 'Lácteos del Valle E.I.R.L.', '019876543', 'contacto@lacteosvalle.pe', 'Av. Industrial 450, Lima');