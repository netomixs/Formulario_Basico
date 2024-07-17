package com.netomix.formulario.ui.main

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.netomix.formulario.ui.main.PersonaReader.Persona
class PersonaDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "datos.db"
        private const val  SQL_CREATE_ENTRIES =
            "CREATE TABLE ${Persona.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${Persona.NOMBRE} TEXT," +
                    "${Persona.APELLIDO_P} TEXT,"+
                    "${Persona.APELLIDO_M} TEXT,"+
                    "${Persona.COLOR} TEXT,"+
                    "${Persona.IMAGEN} TEXT,"+
                    "${Persona.FECHA_NACIMINETO} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${Persona.TABLE_NAME}"
    }
}