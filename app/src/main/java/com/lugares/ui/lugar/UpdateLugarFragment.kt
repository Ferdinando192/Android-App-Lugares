package com.lugares.ui.lugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lugares.R
import com.lugares.databinding.FragmentUpdateBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class UpdateLugarFragment : Fragment() {
    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
     _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        binding.btAgregar.setOnClickListener {
          UpdateLugarFragment()
      }
        return binding.root
    }

    private fun updateLugar(){
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.toString()
        val web = binding.etWeb.toString()
        val lugar = Lugar(0, nombre, correo, telefono, web,0.0,0.0,0.0,"","")
        lugarViewModel.addLugar(lugar)
        Toast.makeText(requireContext(),getString(R.string.msg_agregado),Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}