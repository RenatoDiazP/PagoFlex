package com.duocuc.pagoflex.data.local

import android.content.Context

class SesionPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    companion object {
        private const val CLAVE_ID_USUARIO = "id_usuario"
    }

    fun guarda(idUsuario: Int){
        prefs.edit().putInt("usuario_id", idUsuario).apply()
    }

    fun obtenerUsuarioId(): Int {
        return prefs.getInt("usuario_id", 0)
    }

    fun cerrarSesion(){
        prefs.edit().clear().apply()
    }
}