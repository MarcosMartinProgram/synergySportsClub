package com.martinmarcos.synergysportclub.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "SynergySportClub.db", null, 2) {

    companion object {
        private const val CREATE_TABLE_PERSONA = """
            CREATE TABLE persona (
                idPersona INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                apellido TEXT,
                dni TEXT,
                fechaNacimiento TEXT,
                domicilio TEXT,
                telefono TEXT,
                fichaMedica TEXT
            );"""

        private const val CREATE_TABLE_ROLES = """
            CREATE TABLE roles (
                RolUsu INTEGER PRIMARY KEY AUTOINCREMENT,
                NomRol TEXT
            );"""

        private const val CREATE_TABLE_USUARIO = """
            CREATE TABLE usuario (
                idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,  -- <<< AÑADIDO: Campo para el nombre de usuario
                mail TEXT,
                pass TEXT,
                idPersona INTEGER,
                RolUsu INTEGER,
                FOREIGN KEY (idPersona) REFERENCES persona(idPersona),
                FOREIGN KEY (RolUsu) REFERENCES roles(RolUsu)
            );"""

        private const val CREATE_TABLE_ACTIVIDADES = """
            CREATE TABLE actividades (
                idActividad INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                horarios TEXT,
                dias TEXT,
                cupo INTEGER
            );"""

        private const val CREATE_TABLE_SOCIO = """
            CREATE TABLE socio (
                idSocio INTEGER PRIMARY KEY AUTOINCREMENT,
                idPersona INTEGER,
                fechaAlta TEXT,
                carnet TEXT,
                precioCuota REAL,
                FOREIGN KEY(idPersona) REFERENCES persona(idPersona)
            );"""

        private const val CREATE_TABLE_NOSOCIO = """
            CREATE TABLE nosocio (
                idNoSocio INTEGER PRIMARY KEY AUTOINCREMENT,
                idPersona INTEGER,
                fechaActividad TEXT,
                habilitado INTEGER,
                idActividad INTEGER,
                FOREIGN KEY(idPersona) REFERENCES persona(idPersona),
                FOREIGN KEY(idActividad) REFERENCES actividades(idActividad)
            );"""

        private const val CREATE_TABLE_PAGOSOCIO = """
            CREATE TABLE pagosocio (
                idPagoSocio INTEGER PRIMARY KEY AUTOINCREMENT,
                idSocio INTEGER,
                fechaPago TEXT,
                fechaVencimiento TEXT,
                tipoPago TEXT,
                cantidadCuotas INTEGER,
                monto REAL,
                estado TEXT,
                FOREIGN KEY(idSocio) REFERENCES socio(idSocio)
            );"""

        // OJO: Tu diagrama tiene 'pagonosocios' y 'pago_actividad' que parecen muy similares.
        // Las he creado ambas tal como están en la imagen.
        private const val CREATE_TABLE_PAGONOSOCIOS = """
            CREATE TABLE pagonosocios (
                idPagoNoSocio INTEGER PRIMARY KEY AUTOINCREMENT,
                fechaPago TEXT,
                precio REAL,
                idNoSocio INTEGER,
                idActividad INTEGER,
                FOREIGN KEY(idNoSocio) REFERENCES nosocio(idNoSocio),
                FOREIGN KEY(idActividad) REFERENCES actividades(idActividad)
            );"""

        private const val CREATE_TABLE_PAGO_ACTIVIDAD = """
            CREATE TABLE pago_actividad (
                idPagoActividad INTEGER PRIMARY KEY AUTOINCREMENT,
                idNoSocio INTEGER,
                idActividad INTEGER,
                fecha TEXT,
                monto REAL,
                tipoPago TEXT,
                FOREIGN KEY(idNoSocio) REFERENCES nosocio(idNoSocio),
                FOREIGN KEY(idActividad) REFERENCES actividades(idActividad)
            );"""

        private const val CREATE_TABLE_VALOR_ACTIVIDAD = """
            CREATE TABLE valor_actividad (
                idValorActividad INTEGER PRIMARY KEY AUTOINCREMENT,
                idActividad INTEGER,
                monto REAL,
                fechaDesde TEXT,
                FOREIGN KEY(idActividad) REFERENCES actividades(idActividad)
            );"""

        private const val CREATE_TABLE_VALOR_CUOTA = """
            CREATE TABLE valor_cuota (
                idValorCuota INTEGER PRIMARY KEY AUTOINCREMENT,
                monto REAL,
                fechaDesde TEXT
            );"""

        // Lista de todas las sentencias de creación
        private val ALL_CREATE_TABLE_STATEMENTS = arrayOf(
            CREATE_TABLE_PERSONA,
            CREATE_TABLE_ROLES,
            CREATE_TABLE_USUARIO,
            CREATE_TABLE_ACTIVIDADES,
            CREATE_TABLE_SOCIO,
            CREATE_TABLE_NOSOCIO,
            CREATE_TABLE_PAGOSOCIO,
            CREATE_TABLE_PAGONOSOCIOS,
            CREATE_TABLE_PAGO_ACTIVIDAD,
            CREATE_TABLE_VALOR_ACTIVIDAD,
            CREATE_TABLE_VALOR_CUOTA
        )

        // Lista de todos los nombres de tablas
        private val ALL_TABLE_NAMES = arrayOf(
            "persona",
            "roles",
            "usuario",
            "actividades",
            "socio",
            "nosocio",
            "pagosocio",
            "pagonosocios",
            "pago_actividad",
            "valor_actividad",
            "valor_cuota"
        )
    }
    /**
     * Se llama cuando la base de datos se crea por primera vez.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        // Usamos db? (safe call) porque el parámetro puede ser nulo
        // Ejecutamos todas las sentencias de creación
        ALL_CREATE_TABLE_STATEMENTS.forEach { statement ->
            db?.execSQL(statement)
        }
        // Creamos un ContentValues para insertar los roles de forma segura
        val rolesValues = ContentValues()

        // Insertar Rol 1: Administrador
        rolesValues.put("RolUsu", 1)
        rolesValues.put("NomRol", "Administrador")
        db?.insert("roles", null, rolesValues)

        // Insertar Rol 2: Socio/Cliente
        rolesValues.clear() // Limpiamos para reutilizar el objeto
        rolesValues.put("RolUsu", 2)
        rolesValues.put("NomRol", "Socio")
        db?.insert("roles", null, rolesValues)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Esta lógica maneja la actualización de la versión 1 a la 2
        if (oldVersion < 2) {
            // Si la versión antigua es la 1, añade la columna 'username' a la tabla 'usuario'.
            db?.execSQL("ALTER TABLE usuario ADD COLUMN username TEXT")
        }


    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        db?.execSQL("PRAGMA foreign_keys=ON;")
    }


}