package com.example.mybank.repository

import ClientBancaire
import com.example.mybank.api.RetrofitInstance
import com.example.mybank.data.dto.StatsResponse
import com.example.mybank.service.ClientApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RemoteClientRepository(private val api: ClientApiService = RetrofitInstance.api) {

    // État pour suivre les erreurs
    private val _erreurConnexion = MutableStateFlow<String?>(null)
    val erreurConnexion: StateFlow<String?> = _erreurConnexion.asStateFlow()

    // Fonction pour réinitialiser l'état d'erreur
    fun reinitialiserErreur() {
        _erreurConnexion.value = null
    }

    suspend fun getClients(): List<ClientBancaire> {
        return try {
            api.getClients()
        } catch (e: Exception) {
            _erreurConnexion.value = "Erreur de connexion au serveur: ${e.localizedMessage}"
            print(_erreurConnexion)
            emptyList() // Retourne une liste vide en cas d'erreur
        }
    }

    suspend fun ajouterClient(client: ClientBancaire): Boolean {
        return try {
            api.ajouterClient(client)
            true
        } catch (e: Exception) {
            _erreurConnexion.value = "Impossible d'ajouter le client: ${e.localizedMessage}"
            print(_erreurConnexion)
            false
        }
    }

    suspend fun modifierClient(id: Int, client: ClientBancaire): Boolean {
        return try {
            api.modifierClient(id, client)
            true
        } catch (e: Exception) {
            _erreurConnexion.value = "Impossible de modifier le client: ${e.localizedMessage}"
            print(_erreurConnexion)
            false
        }
    }

    suspend fun supprimerClient(id: Int): Boolean {
        return try {
            api.supprimerClient(id)
            true
        } catch (e: Exception) {
            _erreurConnexion.value = "Impossible de supprimer le client: ${e.localizedMessage}"
            print(_erreurConnexion)
            false
        }
    }
    suspend fun getStats(): StatsResponse? {
        return try {
            api.getStats()
        } catch (e: Exception) {
            _erreurConnexion.value = "Impossible de récupérer les statistiques: ${e.localizedMessage}"
            null
        }
    }
}