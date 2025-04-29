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
