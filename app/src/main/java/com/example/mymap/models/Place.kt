package com.example.mymap.models

import java.io.Serializable

data class Place(
    val title: String, val desscription: String, val latitude: Double, val longitude: Double
):Serializable
