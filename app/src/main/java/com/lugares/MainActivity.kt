package com.lugares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Declarar variables.

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        binding.btLogin.setOnClickListener { hacerLogin() }
        binding.btRegister.setOnClickListener { hacerRegistro() }

        //Esto es un comentario del video.

    } //Fin del metodo onCreate.

    private fun hacerRegistro() {

        //Declarar variables.

        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Se hace el registro en firebase.

        auth.createUserWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this){

                task -> if(task.isSuccessful){

                    Log.d("Autenticando","Creado Usuario")
                    val user = auth.currentUser

                    actualizar(user)

                    } else{

                    Log.d("Autenticando","Creando Fall")
                    actualizar(null)

                    } //Fin del else.

            } //Fin del CompleteListener.

    } //Fin del metodo.

    private fun hacerLogin() {

        //Declarar variables.

        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Se hace el registro en firebase.

        auth.signInWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this){

                task -> if(task.isSuccessful){

                    Log.d("Autenticando","Login Usuario")
                    val user = auth.currentUser

                    actualizar(user)

                } else{

                    Log.d("Autenticando","Login Falló")

                    actualizar(null)

                } //Fin del else.

            } //Fin del CompleteListener.

    } //Fin del metodo.

    private fun actualizar(user: FirebaseUser?) {

        if(user!=null){

            val intent = Intent(this , Principal::class.java)
            startActivity(intent)

        } //Fin del if.

    } //Fin de actualizar.

    public override fun onStart(){

        super.onStart()

        val usuario = auth.currentUser
        actualizar(usuario)

    } //Fin del onStart.

} //Fin de la clase.