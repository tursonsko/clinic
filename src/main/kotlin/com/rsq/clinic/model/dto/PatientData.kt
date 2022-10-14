package com.rsq.clinic.model.dto

import com.rsq.clinic.model.entity.Patient
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