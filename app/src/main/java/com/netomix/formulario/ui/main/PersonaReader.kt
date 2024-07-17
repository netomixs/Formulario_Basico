package com.netomix.formulario.ui.main

import android.provider.BaseColumns

object PersonaReader {
    // Table contents are grouped together in an anonymous object.
    object Persona : BaseColumns {
        const val TABLE_NAME = "Persona"
        const val NOMBRE = "nombre"
        const val APELLIDO_P = "apelidoP"
        const val APELLIDO_M = "apelidoM"
        const val FECHA_NACIMINETO="nacimiento"
        const val COLOR = "color"
        const val IMAGEN = "image"
    }
}