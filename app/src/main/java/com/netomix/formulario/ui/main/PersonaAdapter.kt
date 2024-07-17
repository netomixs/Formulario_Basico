package com.netomix.formulario.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.netomix.formulario.MainActivity
import com.netomix.formulario.R
import java.io.File
import java.text.SimpleDateFormat


class PersonaAdapter(private val itemList: MutableList<PersonaItem>, val context: Context) :
    RecyclerView.Adapter<PersonaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val apellidoPTxtView: TextView = itemView.findViewById(R.id.textViewApeliidoP)
        val apellidoMTxtView: TextView = itemView.findViewById(R.id.textViewApelldioM)
        val nacimineto: TextView = itemView.findViewById(R.id.textViewNacimiento)
        val btnActualizar: Button = itemView.findViewById(R.id.buttonUpdate)
        val btnEliminar: Button = itemView.findViewById(R.id.buttonDelete)
        val fondo: ConstraintLayout = itemView.findViewById(R.id.fondo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carview_item, parent, false)
        return ViewHolder(view)
    }

    fun eliminarElemento(index: Int) {
        val dbHelper = PersonaDbHelper(context)
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("${itemList[index].ID}")
        val deletedRows = db.delete(PersonaReader.Persona.TABLE_NAME, selection, selectionArgs)
        Log.d("Datos", deletedRows.toString())
        notifyItemRemoved(index)
        itemList.removeIf { it.ID == itemList[index].ID }
        db.close()
    }

    fun actualizarDatos(index: Int) {
        try {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("UPDATE", "UPDATE")
            intent.putExtra("ID", "${itemList[index].ID}")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        try {
            val formato = SimpleDateFormat("dd/MM/yyyy")
            val fechaFormateada = formato.format(item.Nacimiento)
            holder.nombre.text = item.nombre
            holder.apellidoPTxtView.text = item.apellido_p
            holder.apellidoMTxtView.text = item.apellido_m
            holder.nacimineto.text = fechaFormateada
            val drawable = ColorDrawable(Color.parseColor("#" + item.color))
            holder.fondo.background = drawable
            holder.btnEliminar.setOnClickListener {
                eliminarElemento(position)
            }
            holder.btnActualizar.setOnClickListener {
                actualizarDatos(position)
            }

        } catch (e: Exception) {
            println(Uri.parse(item.imagen))
            println(e.toString())
        }
    }

    override fun getItemCount() = itemList.size
}
