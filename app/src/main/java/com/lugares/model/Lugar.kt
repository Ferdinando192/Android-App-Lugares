package com.lugares.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lugar(
    var id: String,//En Firestore los IDs deben ser strings.
    val nombre: String,
    val correo: String?,
    val telefono: String?,
    val web: String?,
    val latitud: Double?,
    val longitud: Double?,
    val altura: Double?,
    val rutaAudio: String?,
    val rutaImagen: String?

) : Parcelable{
    constructor():
            this("","","","","",0.0,0.0,0.0,"","")
}


