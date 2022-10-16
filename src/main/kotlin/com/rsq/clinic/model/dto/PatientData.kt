package com.rsq.clinic.model.dto

import com.rsq.clinic.advice.WrongPatientDataException
import com.rsq.clinic.model.entity.Patient
import com.rsq.clinic.utils.containsForbiddenCharactersForAddress
import com.rsq.clinic.utils.containsForbiddenCharactersForData
import com.rsq.clinic.utils.containsNoCharacters
import java.util.*

data class PatientCreateRequest(
    val firstName: String,
    val lastName: String,
    val address: String
) {

    fun toPatientEntity() =
        Patient(
            firstName = firstName,
            lastName = lastName,
            address = address
        )

    /**
     * Validator using functions from utils/Extensions.kt
     */
    fun checkRequestFields() {
        val errorList = mutableListOf<String>()

        if (firstName.containsForbiddenCharactersForData())
            errorList.add("Patient's first name is allowed to contain only letters, ' and - ")
        if (firstName.containsNoCharacters())
            errorList.add("Patient's first name can not be empty")
        if (lastName.containsForbiddenCharactersForData())
            errorList.add("Patient's last name is allowed to contain only letters, ' and - ")
        if (lastName.containsNoCharacters())
            errorList.add("Patient's last name can not be empty")
        if (address.containsForbiddenCharactersForAddress())
            errorList.add("Patient's address is allowed to contain only letters, numbers and characters , . ( ) \" / ")
        if (address.containsNoCharacters())
            errorList.add("Patient's address can not be empty")

        throw if (errorList.isNotEmpty()) WrongPatientDataException(errorList) else return
    }
}

data class PatientUpdateRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val address: String? = null
)

data class PatientResponse(
    val id: UUID?,
    val firstName: String,
    val lastName: String,
    val address: String
)