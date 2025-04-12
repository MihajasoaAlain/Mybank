package com.example.mybank.models

enum class CategorieClient {
    INSUFFISANT , MOYEN,ELEVE
}
data class Client(
    val numCompte : String,
    val nom : String,
    var solde :Double
){
    fun getCategorie(): CategorieClient{
        val min = 1000;
        val  avg= 5000;
        return when {
            solde < min -> CategorieClient.INSUFFISANT
            solde <= avg -> CategorieClient.MOYEN
            else -> CategorieClient.ELEVE
        }
    }
}