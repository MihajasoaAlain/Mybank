// Modèle de données pour le client bancaire
data class ClientBancaire(
    val numCompte: String,
    val nom: String,
    var solde: Double
) {
    // Fonction pour déterminer la catégorie du solde
    fun getCategorieSolde(): String {
        return when {
            solde < 1000 -> "insuffisant"
            solde <= 5000 -> "moyen"
            else -> "élevé"
        }
    }
}


// Repository simulé pour gérer les données (en attendant l'implémentation MySQL/PostgreSQL)
class ClientRepository {
    private val _clients = mutableListOf(
        ClientBancaire("C001", "Martin Dupont", 850.0),
        ClientBancaire("C002", "Sophie Laurent", 3500.0),
        ClientBancaire("C003", "Ahmed Benali", 7500.0),
        ClientBancaire("C004", "Marie Dubois", 1200.0)
    )

    val clients: List<ClientBancaire> get() = _clients.toList()

    fun ajouterClient(client: ClientBancaire) {
        _clients.add(client)
    }

    fun modifierClient(index: Int, client: ClientBancaire) {
        if (index in _clients.indices) {
            _clients[index] = client
        }
    }

    fun supprimerClient(index: Int) {
        if (index in _clients.indices) {
            _clients.removeAt(index)
        }
    }

    // Calculs statistiques
    fun getSoldeTotal(): Double = clients.sumOf { it.solde }
    fun getSoldeMinimal(): Double = clients.minOfOrNull { it.solde } ?: 0.0
    fun getSoldeMaximal(): Double = clients.maxOfOrNull { it.solde } ?: 0.0
}