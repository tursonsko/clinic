package com.rsq.clinic.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.rsq.clinic.model.entity.Doctor
import com.rsq.clinic.model.entity.Patient
import com.rsq.clinic.model.entity.Visit
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

data class VisitCreateRequest(
    @JsonFormat(pattern = "dd-MM-yyyy")
    val visitDate: LocalDate,
    @JsonFormat(pattern = "HH:mm")
    val visitTime: LocalTime,
    val spot: String

) {
    fun toVisitEntity(patient: Patient, doctor: Doctor) =
        Visit(
            visitDate = visitDate,
            visitTime = visitTime,
            spot = spot,
            patient = patient,
            doctor = doctor
        )
}

data class VisitUpdateRequest(
    val visitId: UUID,
    @JsonFormat(pattern = "dd-MM-yyyy")
    val visitDate: LocalDate,
    @JsonFormat(pattern = "HH:mm")
    val visitTime: LocalTime
)

data class VisitResponse(
    val id: UUID?,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val visitDate: LocalDate,
    @JsonFormat(pattern = "HH:mm")
    val visitTime: LocalTime,
    val spot: String,
    val patient: PatientResponse?,
    val doctor: DoctorResponse?
)
