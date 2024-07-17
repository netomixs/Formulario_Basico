package com.netomix.formulario

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.netomix.formulario.ui.main.PersonaDbHelper
import com.netomix.formulario.ui.main.PersonaItem
import com.netomix.formulario.ui.main.PersonaReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var dbHelper: PersonaDbHelper
    lateinit var nombre: EditText
    lateinit var apellidoP: EditText
    lateinit var apellidoM: EditText
    lateinit var day: NumberPicker
    lateinit var mes: Spinner
    lateinit var year: NumberPicker
    lateinit var color: EditText
    lateinit var colorR: SeekBar
    lateinit var colorG: SeekBar
    lateinit var colorB: SeekBar
    lateinit var buttonActualizar: Button
    lateinit var buttonAgregar: Button
    lateinit var IVPreviewImage: ImageView
    lateinit var btnColorPreview: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = PersonaDbHelper(applicationContext)
        val intent: Intent = intent




        day = findViewById<NumberPicker>(R.id.dayPicker)
        day.minValue = 1
        day.maxValue = 30
        mes = findViewById(R.id.spinnerMes)
        year = findViewById<NumberPicker>(R.id.yearPikers)
        year.minValue = 1900
        year.maxValue = 2023
        buttonActualizar = findViewById(R.id.buttonActualizar)
        buttonAgregar = findViewById(R.id.button)
        nombre = findViewById(R.id.editTextName)
        apellidoP = findViewById(R.id.editTextApellidoP)
        apellidoM = findViewById(R.id.editTextApellidoM)
        colorR = findViewById<SeekBar>(R.id.colorR)
        colorG = findViewById<SeekBar>(R.id.colorG)
        colorB = findViewById<SeekBar>(R.id.colorB)
        btnColorPreview = findViewById<View>(R.id.btnColorPreview)
        color = findViewById<EditText>(R.id.strColor)
        if (intent.hasExtra("UPDATE")) {
            Toast.makeText(applicationContext, "Actualizar datos", Toast.LENGTH_SHORT).show()
            var datosExtra = intent.getStringExtra("UPDATE")
            var ID = intent.getStringExtra("ID")
            if (ID != null) {
                setDatos(ID.toInt())
            }
            buttonActualizar.visibility = View.VISIBLE
            buttonAgregar.visibility = View.GONE
            buttonActualizar.setOnClickListener {
                if (ID != null) {
                    actualizarDatos(ID!!.toInt())
                    ID = ""
                    datosExtra = ""
                }
            }
        } else {
            buttonActualizar.visibility = View.GONE
            buttonAgregar.visibility = View.VISIBLE

            Toast.makeText(applicationContext, "Ingrese datos", Toast.LENGTH_SHORT).show()

        };
        color.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 6) {
                    colorR.progress = Integer.parseInt(s.substring(0..1), 16)
                    colorG.progress = Integer.parseInt(s.substring(2..3), 16)
                    colorB.progress = Integer.parseInt(s.substring(4..5), 16)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })
        colorR.max = 255
        colorR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                val colorStr = getColorString()
                color.setText(colorStr.replace("#", "").toUpperCase())
                try {
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                    Log.e("Error Color", color.text.toString())
                }
            }
        })
        colorG.max = 255
        colorG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                val colorStr = getColorString()
                color.setText(colorStr.replace("#", "").toUpperCase())
                try {
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                    Log.e("Error Color", color.text.toString())
                }
            }
        })
        colorB.max = 255
        colorB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                val colorStr = getColorString()
                color.setText(colorStr.replace("#", "").toUpperCase())
                try {
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                    Log.e("Error Color", color.text.toString())
                }

            }
        })
    }

    fun getColorString(): String {
        var r = Integer.toHexString(((255 * colorR.progress) / colorR.max))
        if (r.length == 1) r = "0" + r
        var g = Integer.toHexString(((255 * colorG.progress) / colorG.max))
        if (g.length == 1) g = "0" + g
        var b = Integer.toHexString(((255 * colorB.progress) / colorB.max))
        if (b.length == 1) b = "0" + b
        return "#" + r + g + b
    }

    fun actualizarDatos(id: Int) {
        try {
            if (nombre.text.toString().isNotEmpty() &&
                apellidoP.text.toString().isNotEmpty() &&
                apellidoM.text.toString().isNotEmpty() &&
                color.text.toString().isNotEmpty() &&
                day.value != 0 &&
                year.value != 0 &&
                mes.selectedItem.toString().isNotEmpty()
            ) {
                val db = dbHelper.writableDatabase
                val values = getDatos()
                val selection = "${BaseColumns._ID} = ?"
                val selectionArgs = arrayOf("${id}")
                val newRowId =
                    db?.update(PersonaReader.Persona.TABLE_NAME, values, selection, selectionArgs)
                Toast.makeText(
                    applicationContext,
                    "Datos actualizados correctamente  ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(applicationContext, "Completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            println("Ërror" + e.toString())
        }
    }

    fun guardarDatos(view: View) {
        try {
            if (nombre.text.toString().isNotEmpty() &&
                apellidoP.text.toString().isNotEmpty() &&
                apellidoM.text.toString().isNotEmpty() &&
                color.text.toString().isNotEmpty() &&
                day.value != 0 &&
                year.value != 0 &&
                mes.selectedItem.toString().isNotEmpty()
            ) {
                val db = dbHelper.writableDatabase
                val values = getDatos()
                val newRowId = db?.insert(PersonaReader.Persona.TABLE_NAME, null, values)
                Toast.makeText(
                    applicationContext,
                    "Datos registrados + ${newRowId}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(applicationContext, "Completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            println("Ërror" + e.toString())
        }
    }

    fun getDatos(): ContentValues {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year.value);
        calendar.set(
            Calendar.MONTH,
            mes.selectedItemPosition
        );
        calendar.set(Calendar.DAY_OF_MONTH, day.value);
        val date = calendar.time
        val values = ContentValues().apply {
            put(PersonaReader.Persona.NOMBRE, nombre.text.toString())
            put(PersonaReader.Persona.APELLIDO_P, apellidoP.text.toString())
            put(PersonaReader.Persona.APELLIDO_M, apellidoM.text.toString())
            put(PersonaReader.Persona.FECHA_NACIMINETO, date.toString())
            put(PersonaReader.Persona.COLOR, color.text.toString())
            put(PersonaReader.Persona.IMAGEN, "")

        }
        return values
    }

    fun cambiar(view: View) {
        try {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            print(e.toString())
        }

    }

    fun setDatos(id: Int) {
        var persona: PersonaItem? = null
        persona = obtenerPersonaPorId(id)

        if (persona != null) {
            nombre.setText(persona.nombre)
            apellidoP.setText(persona.apellido_p)
            apellidoM.setText(persona.apellido_m)
            day.value = persona.Nacimiento.day + 1
            mes.setSelection(persona.Nacimiento.month)
            val indiceValorDeseado = persona.Nacimiento.year - 1900 - 14
            year.value = (indiceValorDeseado)
            var s = persona.color
            btnColorPreview.setBackgroundColor(Color.parseColor("#" + persona.color))
            color.setText(persona.color)
            var color = Color.parseColor("#" + persona.color)


        }
    }

    @SuppressLint("Range")
    fun obtenerPersonaPorId(id: Int): PersonaItem? {
        val formatoFecha = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val dbHelper = PersonaDbHelper(applicationContext)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID, PersonaReader.Persona.NOMBRE,
            PersonaReader.Persona.APELLIDO_P, PersonaReader.Persona.APELLIDO_M,
            PersonaReader.Persona.COLOR, PersonaReader.Persona.FECHA_NACIMINETO,
            PersonaReader.Persona.IMAGEN
        )

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(
            PersonaReader.Persona.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var persona: PersonaItem? = null

        if (cursor != null && cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.NOMBRE))
            val apellidoP =
                cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.APELLIDO_P))
            val apellidoM =
                cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.APELLIDO_M))
            val color = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.COLOR))
            val fechaNacimiento =
                cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.FECHA_NACIMINETO))
            val imagen = cursor.getString(cursor.getColumnIndex(PersonaReader.Persona.IMAGEN))
            val fecha: Date = formatoFecha.parse(fechaNacimiento)

            persona = PersonaItem(nombre, id.toInt(), apellidoP, apellidoM, color, fecha, imagen)

            cursor.close()
        }

        return persona
    }
}