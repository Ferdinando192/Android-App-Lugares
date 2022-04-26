package com.lugares.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.lugares.model.Lugar

class LugarDao {

    private val coleccion1 = "lugaresAPP"
    private val coleccion2 = "misLugares"
    private var codigoUsuario: String
    private var firestore: FirebaseFirestore

    init {
        val usuario = Firebase.auth.currentUser?.email
        codigoUsuario = "$usuario"
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //Obtener toda la lista de los documentos tipo lugar...
    fun getLugares(): MutableLiveData<List<Lugar>> {
        val listaLugares = MutableLiveData<List<Lugar>>()

        //obtener los documentos
        firestore
            .collection(coleccion1) //lugaresApp
            .document(codigoUsuario) //el usuario (correo o id)
            .collection(coleccion2) //mis lugares
            .addSnapshotListener { snapshot, e ->
                if (e != null) { //Si de da alguna excepcion al tomar la instantanea entra acá
                    return@addSnapshotListener
                }
                if (snapshot != null) { //Recupero los documentos lugar de la instancia
                    val lista = ArrayList<Lugar>()
                    val lugares = snapshot.documents //Recupera la lista de documentos
                    lugares.forEach {
                        val lugar =
                            it.toObject(Lugar::class.java) //Transformando un documento a un objeto Lugar
                        if (lugar != null) {
                            lista.add(lugar) //Si se pudo transforma el lugar....se  pasar a la lista
                        }
                    }
                    listaLugares.value = lista
                }
            }
        return listaLugares
    }


    suspend fun saveLugar(lugar: Lugar) {
        val document: DocumentReference //El lugar a actualizar o crear como nuevo.
        if (lugar.id.isEmpty()) {//si el id del lugar no está definido...entonces es un lugar nuevo...
            document = firestore.
                collection(coleccion1)
                .document(codigoUsuario)
                .collection(coleccion2)
                .document()
            lugar.id = document.id
        } else { //Si el lugar tiene un id...entonces es actualizar un lugar...
            document = firestore
                .collection(coleccion1)
                .document(codigoUsuario)
                .collection(coleccion2)
                .document(lugar.id)
        }
        val set =
            document.set(lugar) //Acá efectivamente se actualiza firebase con un nuevo lugar o modifica el que existe
        set.addOnSuccessListener { Log.d("saveLugar", "Lugar agregado/modificado") }
            .addOnCanceledListener { Log.e("saveLugar", "ERROR: Lugar NO agregado/modificado") }
    }

    //suspend fun updateLugar(lugar: Lugar){}

    suspend fun deleteLugar(lugar: Lugar) {
        if (lugar.id.isNotEmpty()) { //Si tiene un valor...en teoría existe en la colección y lo puedo borrar
            firestore
                .collection(coleccion1)
                .document(codigoUsuario)
                .collection(coleccion2)
                .document(lugar.id)
                .delete()
                .addOnSuccessListener { Log.d("deleteLugar", "Lugar eliminado") }
                .addOnCanceledListener { Log.e("deleteLugar", "ERROR: Lugar NO eliminado") }
        }
    }
}