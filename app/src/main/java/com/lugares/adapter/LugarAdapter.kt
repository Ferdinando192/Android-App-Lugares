package com.lugares.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.LugarFilaBinding

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding) :
            RecyclerView.ViewHolder(itemBinding.root){

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}