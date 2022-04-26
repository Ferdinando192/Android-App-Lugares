package com.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares.adapter.LugarAdapter.*

import com.lugares.databinding.LugarFilaBinding
import com.lugares.model.Lugar
import com.lugares.ui.lugar.LugarFragmentDirections


class LugarAdapter: RecyclerView.Adapter<LugarViewHolder>(){

    //Lista para almacenar la info de los lugares registrados en el app, se guardan en fragment_lugar.xml
    private var listaLugares = emptyList<Lugar>() //arrayList

    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(lugar: Lugar) {
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvWeb.text = lugar.web

            //Dibujar la imagen en el RecyclerView
            Glide.with(itemBinding.root.context)
                .load(lugar.rutaImagen) //libreria para trabajar imagenes
                .circleCrop() //que le ponga circulo
                .into(itemBinding.imagen) //y lo guarde aquí

            //Para actualizar un record seleccionado en la lista que despliega los carViews
            itemBinding.vistaFila.setOnClickListener{
                val action = LugarFragmentDirections.
                actionNavLugarToUpdateLugarFragment(lugar)
                itemView.findNavController().navigate(action)
            }
        }
    }
    //Un view holder se usa cuando usamos CardViews para mostrar la info que pusimos en el XML del fragment lugar_fila.xml, y luego pasarlo al Recycler View en fragment_lugar.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        return LugarViewHolder(itemBinding)
    }

    //Este metodo constuye el cardview en memoria usando lugar_fila.xml para luego pasarlo a fragment_lugar.xml
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = listaLugares[position]
        holder.bind(lugar)
    }
    //Saber cuantos elementos tiene el array list y poder iterar en ellos
    override fun getItemCount(): Int {
        return listaLugares.size
    }


    fun setData(lugares : List<Lugar>){
        this.listaLugares=lugares
        notifyDataSetChanged() //Para decirle al adapter que los datos cambiaron, entonces para que se redibujen, osea que llame un nuevo elemento.
    }
}