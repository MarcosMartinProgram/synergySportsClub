// Archivo: data/DBHelper.kt (VERSIÓN CON BUG CORREGIDO Y MÁS LOGS)
package com.martinmarcos.synergysportclub.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // ... (companion object y onCreate no cambian, pero te los pongo para que reemplaces todo sin miedo)
    companion object {
        private const val DATABASE_VERSION = 8
        private const val DATABASE_NAME = "synergy_sport_club.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.i("DBHelper", "-> -> -> INICIANDO onCreate: Creando base de datos desde CERO <- <- <-")

        val CREATE_TABLE_ROL = """
            CREATE TABLE roles (
                idRol INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreRol TEXT NOT NULL UNIQUE
            );"""

        val CREATE_TABLE_PERSONA = """
            CREATE TABLE persona (
                idPersona INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                dni TEXT UNIQUE NOT NULL,
                idRol INTEGER NOT NULL,
                username TEXT UNIQUE,
                password TEXT,
                activo INTEGER NOT NULL DEFAULT 1,
                fechaAlta TEXT,
                telefono TEXT,
                email TEXT,
                domicilio TEXT,
                fechaNacimiento TEXT,
                fichaMedica INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(idRol) REFERENCES roles(idRol)
            );"""
        val CREATE_TABLE_PAGO_CUOTAS = """
        CREATE TABLE pago_cuotas (
            idPagoCuota INTEGER PRIMARY KEY AUTOINCREMENT,
            idPersona INTEGER NOT NULL,            -- Quién pagó (FK a la tabla 'socios')
            fecha_pago TEXT NOT NULL,          -- Cuándo pagó (Formato 'YYYY-MM-DD')
            meses_pagados INTEGER NOT NULL,    -- Cuántos meses pagó (1, 3, 6)
            metodo_pago TEXT NOT NULL,         -- 'Efectivo', 'Tarjeta', etc.
            monto_total REAL NOT NULL,           -- El monto total abonado
            FOREIGN KEY(idPersona) REFERENCES persona(idPersona)
        );"""

        // El resto de tus tablas...
        val CREATE_TABLE_ACTIVIDADES = "CREATE TABLE actividades (idActividad INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL UNIQUE, horarios TEXT NOT NULL, dias TEXT NOT NULL, cupo INTEGER NOT NULL);"
        val CREATE_TABLE_VALOR_CUOTA = "CREATE TABLE valor_cuota (idValorCuota INTEGER PRIMARY KEY AUTOINCREMENT, monto REAL NOT NULL, fechaDesde TEXT NOT NULL, descripcion TEXT);"
        val CREATE_TABLE_VALOR_ACTIVIDAD = "CREATE TABLE valor_actividad (idValorActividad INTEGER PRIMARY KEY AUTOINCREMENT, idActividad INTEGER NOT NULL, monto REAL NOT NULL, fechaDesde TEXT NOT NULL, FOREIGN KEY(idActividad) REFERENCES actividades(idActividad));"
        val CREATE_TABLE_PAGOS = "CREATE TABLE pagos (idPago INTEGER PRIMARY KEY AUTOINCREMENT, idPersona INTEGER NOT NULL, fechaPago TEXT NOT NULL, montoPagado REAL NOT NULL, concepto TEXT NOT NULL, idReferencia INTEGER, FOREIGN KEY(idPersona) REFERENCES persona(idPersona));"
        val CREATE_TABLE_PAGO_SOCIO = """
        CREATE TABLE pagosocio (
            idPago INTEGER PRIMARY KEY AUTOINCREMENT,
            idPersona INTEGER NOT NULL,
            fechaPago TEXT NOT NULL,
            monto REAL NOT NULL,
            fechaVencimiento TEXT NOT NULL,
            FOREIGN KEY(idPersona) REFERENCES persona(idPersona)
        );
    """

        try {
            db.execSQL("PRAGMA foreign_keys=ON;")
            db.execSQL(CREATE_TABLE_ROL)
            Log.i("DBHelper", "Tabla 'roles' creada.")
            db.execSQL(CREATE_TABLE_PERSONA)
            Log.i("DBHelper", "Tabla 'persona' creada.")
            db.execSQL(CREATE_TABLE_ACTIVIDADES)
            db.execSQL(CREATE_TABLE_VALOR_CUOTA)
            db.execSQL(CREATE_TABLE_VALOR_ACTIVIDAD)
            db.execSQL(CREATE_TABLE_PAGOS)
            db.execSQL(CREATE_TABLE_PAGO_SOCIO)
            Log.i("DBHelper", "Resto de tablas creadas.")

            insertarRolesIniciales(db)
            Log.i("DBHelper", "-> -> -> onCreate FINALIZADO con éxito. <- <- <-")
        } catch (e: Exception) {
            Log.e("DBHelper", "FATAL: Error al crear las tablas de la base de datos.", e)
        }
    }

    // --- ¡AQUÍ ESTÁ LA CORRECCIÓN DEL BUG! ---
    private fun insertarRolesIniciales(db: SQLiteDatabase) {
        val roles = listOf("Administrador", "Socio", "No Socio", "Profesor", "Nutricionista", "Entrenador")
        Log.i("DBHelper", "Insertando ${roles.size} roles iniciales...")
        try {
            roles.forEach { nombreRol ->
                // Creamos un NUEVO ContentValues en CADA iteración. Esto es crucial.
                val initialValues = ContentValues().apply {
                    put("nombreRol", nombreRol)
                }
                val id = db.insert("roles", null, initialValues)
                Log.i("DBHelper", "Rol insertado: '$nombreRol' con ID: $id")
            }
        } catch (e: Exception) {
            Log.e("DBHelper", "Error insertando roles iniciales", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("DBHelper", "Actualizando base de datos. Se borrarán todos los datos.")
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS valor_actividad")
        db.execSQL("DROP TABLE IF EXISTS valor_cuota")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS persona")
        db.execSQL("DROP TABLE IF EXISTS roles")
        db.execSQL("DROP TABLE IF EXISTS pago_cuotas")
        db.execSQL("DROP TABLE IF EXISTS pagosocio")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
    }
}
