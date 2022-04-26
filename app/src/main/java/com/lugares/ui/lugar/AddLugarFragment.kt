package com.lugares.ui.lugar

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.lugares.utiles.AudioUtiles
import com.lugares.utiles.ImagenUtiles
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class AddLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentAddLugarBinding? = null //Var = es una variable
    private val binding get() = _binding!! //Val= no es modificable, osea es una constante

    private lateinit var audioUtiles: AudioUtiles //Llama a la clase AudioUtiles para Grabar nota de audio
    private lateinit var imagenUtiles: ImagenUtiles //Llama a la clase ImagenUtiles para Tomar foto
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>//Activar camara del celular

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentAddLugarBinding.inflate(inflater,container,false)


        binding.btAgregar.setOnClickListener {
        //Además, hacer que esto suba imagen y audio.
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.text = getString(R.string.msg_subiendo_audio)
            binding.msgMensaje.visibility = TextView.VISIBLE
            subeAudioNube()

        }
        //Grabar nota de audio
        audioUtiles = AudioUtiles(requireActivity(),
        requireContext(),
        binding.btAccion, binding.btPlay, binding.btDelete,
        getString(R.string.msg_graba_audio),
        getString(R.string.msg_detener_audio),
            )

        //Tomar Foto
        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                imagenUtiles.actualizaFoto()
            }

        }

        imagenUtiles = ImagenUtiles(
            requireContext(),
            binding.btPhoto,
            binding.btRotaL,
            binding.btRotaR,
            binding.imagen,
            tomarFotoActivity
        )

        //GPS
        ubicaGPS()
        return binding.root
    }

    //Sube Audio e Imagen
    private fun subeAudioNube() {
    val audioFile = audioUtiles.audioFile
        if (audioFile.exists() && audioFile.isFile && audioFile.canRead()){ //pregunto si el audio existe, si es un archivo y si lo puedo leer
            val ruta = Uri.fromFile(audioFile)
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"//la ruta donde quiero guardar en Firebase, la estoy creando aquí.
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaAudio = it.toString()
                            subeImagenNube(rutaAudio)
                        }
                }
                .addOnFailureListener{subeImagenNube("")}
        }else{
            subeImagenNube("")
        }
    }

    private fun subeImagenNube(rutaAudio: String) {
        val imagenFile = imagenUtiles.imagenFile
        if (imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()){ //pregunto si la imagen existe, si es un archivo y si la puedo leer
            val ruta = Uri.fromFile(imagenFile)
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/imagenes/${imagenFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaImagen = it.toString()
                            agregarLugar(rutaAudio, rutaImagen)
                        }
                }
                .addOnFailureListener{agregarLugar(rutaAudio,"")}
        }else{
            agregarLugar(rutaAudio,"")
        }
    }

    // variable para saber si ya se tienen los permisos
    private var conPermisos:Boolean=true
    private fun ubicaGPS() {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        //Se valida si se requieren pedir los permisos...
        if( ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),105)

        }

        //Se recuperan las coordenadas GPS
        if(conPermisos) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null){
                    binding.tvLatitud.text = "${location.latitude}"
                    binding.tvLongitud.text = "${location.longitude}"
                    binding.tvAltura.text = "${location.altitude}"
                } else {
                    binding.tvLatitud.text = getString(R.string.error)
                    binding.tvLongitud.text = getString(R.string.error)
                    binding.tvAltura.text = getString(R.string.error)
                }
            }
        }
    }

    private fun agregarLugar(rutaAudio: String, rutaImagen: String) {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        val altura = binding.tvAltura.text.toString().toDouble()

        val lugar = Lugar("",nombre,correo,telefono,web,latitud,longitud,altura,rutaAudio, rutaImagen)
        lugarViewModel.addLugar(lugar)
        Toast.makeText(requireContext(),getString(R.string.msg_agregar),Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}