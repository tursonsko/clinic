package com.rsq.clinic.model.dto

import com.rsq.clinic.advice.WrongDoctorDataException
import com.rsq.clinic.model.entity.Doctor
import com.rsq.clinic.utils.containsForbiddenCharactersForData
import com.rsq.clinic.utils.containsNoCharacters
import java.util.*

data class DoctorCreateRequest(
    override val firstName: String,
    override val lastName: String,
    val specialization: String
) : PersonCreateRequest {

    fun toDoctorEntity() =
        Doctor(
            firstName = firstName,
            lastName = lastName,
            specialization = specialization
        )

    override fun checkRequestFields() {
        val errorList = mutableListOf<String>()

        if (firstName.containsForbiddenCharactersForData())
            errorList.add("Doctor's first name is allowed to contain only letters, ' and - ")
        if (firstName.containsNoCharacters())
            errorList.add("Doctor's first namee can not be empty")
        if (lastName.containsForbiddenCharactersForData())
            errorList.add("Doctor's last name is allowed to contain only letters, ' and - ")
        if (lastName.containsNoCharacters())
            errorList.add("Doctor's last name can not be empty")
        if (specialization.containsForbiddenCharactersForData())
            errorList.add("Doctor's specialization is allowed to contain only letters, ' and - ")
        if (specialization.containsNoCharacters())
            errorList.add("Doctor's specialization can not be empty")

        throw if (errorList.isNotEmpty()) WrongDoctorDataException(errorList) else return
    }
}

data class DoctorUpdateRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val specialization: String? = null
)

data class DoctorResponse(
    val id: UUID?,
    val firstName: String,
    val lastName: String,
    val specialization: String
)