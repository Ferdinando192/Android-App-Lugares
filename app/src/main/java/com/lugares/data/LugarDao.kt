package com.lugares.data

import android.service.autofill.FillResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.lugares.model.Lugar

class LugarDao {

    private val coleccion1 = "lugaresAPP"
    private val coleccion2 = "missLugares"
    private var codigoUsuario:String
    private var firestore: FirebaseFirestore

    init{
        val usuario = Firebase.auth.currentUser?.email
        codigoUsuario = "$usuario"
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //Obtener toda la lista de documentos tipo lugar
    fun getLugares(): MutableLiveData<List<Lugar>> {
        val listaLugares =  MutableLiveData<List<Lugar>>()

        //Obtener los documentos
        firestore.collection(coleccion1)
            .document(codigoUsuario)
            .collection(coleccion2)
            .addSnapshotListener{snapshot, e->
                if (e != null){ //Si se da alguna exepcion al tomar la instantanea, entra aca
                    return@addSnapshotListener
                }
                if(snapshot != null){ //Recupero los doocuemntos lugar de la instantanea
                    val lista = ArrayList<Lugar>()
                    val lugares = snapshot.documents //Recupera la lista de documentos
                    lugares.forEach(){
                        val lugar = it.toObject(Lugar::class.java)
                        if (lugar != null){
                            lista.add(lugar)
                        }
                    }
                    listaLugares.value = lista
                }
            }

        return listaLugares
    }

    suspend fun  saveLugar(lugar: Lugar) {
        val document: DocumentReference //Actualizar o crear
        if(lugar.id.isEmpty()){
            document = firestore.collection(coleccion1).document(codigoUsuario).collection(coleccion2).document()
            lugar.id = document.id
        }else{
            document = firestore.collection(coleccion1).document(codigoUsuario).collection(coleccion2).document(lugar.id)
        }
        val set = document.set(lugar)
        set.addOnSuccessListener { Log.d("saveLugar", "Lugar agregado/modificado") }
            .addOnCanceledListener { Log.d("saveLugar", "ERROR: Lugar NO agregado/modificado") }
    }

    suspend fun deleteLugar(lugar: Lugar)
    {
        if (lugar.id.isEmpty())
        {
            firestore
                .collection(coleccion1)
                .document(codigoUsuario)
                .collection(coleccion2)
                .document(lugar.id)
                .delete()
                .addOnSuccessListener { Log.d("deleteLugar", "Lugar eliminado") }
                .addOnCanceledListener { Log.d("deleteLugar", "ERROR: Lugar NO eliminado") }

        }
    }
} //Fin de la interfaze