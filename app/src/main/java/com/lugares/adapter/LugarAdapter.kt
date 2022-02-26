package com.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.LugarFilaBinding
import com.lugares.model.Lugar

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //Lista para almacenar la info de los lugares... registrados en el app
    private var listaLugares = emptyList<Lugar>()

    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding) :
            RecyclerView.ViewHolder(itemBinding.root){
                fun bind(lugar: Lugar){
                    itemBinding.tvNombre.text = lugar.nombre
                    itemBinding.tvTelefono.text = lugar.telefono
                    itemBinding.tvCorreo.text = lugar.correo
                    itemBinding.tvWeb.text = lugar.web
                }

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return LugarViewHolder((itemBinding))
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = listaLugares[position]
        holder.bind(lugar)
    }

    override fun getItemCount(): Int {
        return  listaLugares.size
    }

    fun setData(lugares : List<Lugar>){
        this.listaLugares = lugares
        notifyDataSetChanged()
    }

}