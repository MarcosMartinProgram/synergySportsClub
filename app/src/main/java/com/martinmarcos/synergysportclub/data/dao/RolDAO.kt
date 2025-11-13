// Archivo: data/dao/RolDAO.kt (VERSIÓN CORREGIDA PARA LEER DE 'roles')
package com.martinmarcos.synergysportclub.data.dao

import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.Rol

class RolDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun getAllRoles(): List<Rol> {
        val db = dbHelper.readableDatabase
        val roles = mutableListOf<Rol>()

        // --- ¡AQUÍ ESTÁ LA CORRECCIÓN CLAVE! ---
        // Le decimos que busque en la tabla "roles" (plural), como está en tu DBHelper.
        // También excluimos 'Administrador' de la lista, porque no se debe inscribir desde aquí.
        val query = "SELECT * FROM roles WHERE nombreRol != 'Administrador'"

        try {
            db.rawQuery(query, null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val rol = Rol(
                            idRol = cursor.getInt(cursor.getColumnIndexOrThrow("idRol")),
                            nombreRol = cursor.getString(cursor.getColumnIndexOrThrow("nombreRol"))
                        )
                        roles.add(rol)
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            Log.e("RolDAO", "Error al obtener los roles de la tabla 'roles'", e)
        }

        Log.i("RolDAO", "Se encontraron ${roles.size} roles para el spinner.")
        return roles
    }
}
