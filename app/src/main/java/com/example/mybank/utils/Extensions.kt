package com.example.mybank.utils

import ClientBancaire
import com.example.mybank.api.data.dto.ClientDto

fun ClientDto.toClientBancaire() = ClientBancaire(
    numCompte = numCompte,
    nom = nom,
    solde = solde
)