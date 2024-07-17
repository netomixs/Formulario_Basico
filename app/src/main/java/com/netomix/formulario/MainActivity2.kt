package com.netomix.formulario

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.BaseColumns
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netomix.formulario.ui.main.PersonaAdapter
import com.netomix.formulario.ui.main.PersonaDbHelper
import com.netomix.formulario.ui.main.PersonaItem
import com.netomix.formulario.ui.main.PersonaReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity2 : AppCompatActivity() {

    private val listaPersonas = mutableListOf<PersonaItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        cargarDatos()
    }

    @SuppressLint("Range")
    fun cargarDatos(){
        try{
            val dbHelper = PersonaDbHelper( applicationContext)
            val db = dbHelper.readableDatabase
            val projection = arrayOf(
                BaseColumns._ID, PersonaReader.Persona.NOMBRE,
                PersonaReader.Persona.APELLIDO_P, PersonaReader.Persona.APELLIDO_M,
                PersonaReader.Persona.COLOR, PersonaReader.Persona.FECHA_NACIMINETO,
                PersonaReader.Persona.IMAGEN
            )
            val cursor = db.query(
                PersonaReader.Persona.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null
            )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val formatoFecha = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                    val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                    val nombre = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.NOMBRE))
                    val apellidoP = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.APELLIDO_P))
                    val apellidoM = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.APELLIDO_M))
                    val color = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.COLOR))
                    val fechaNacimiento = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.FECHA_NACIMINETO))
                    val iamgen = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.IMAGEN))
                    val fecha: Date = formatoFecha.parse(fechaNacimiento)
                    listaPersonas.add(  PersonaItem(nombre,id.toInt(), apellidoP, apellidoM,color,fecha,iamgen))
                    Log.d("Datos", "ID: $id, Nombre: $nombre, Apellido P: $apellidoP, Apellido M: $apellidoM, Color: $color, Fecha de Nacimiento: $fechaNacimiento,Imagen $iamgen")
                }
                cursor.close()
            }
            val recicledView:RecyclerView=findViewById(R.id.lista)
            recicledView.layoutManager=LinearLayoutManager(this)
            val adapter = PersonaAdapter(listaPersonas,applicationContext)
            recicledView.adapter = adapter
            adapter
        }catch (e:Exception){
            print(e.toString())

        }
    }

}