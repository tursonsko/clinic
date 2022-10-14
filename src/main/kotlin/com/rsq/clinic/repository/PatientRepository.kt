package com.rsq.clinic.repository

import com.rsq.clinic.model.entity.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PatientRepository : JpaRepository<Patient, UUID> {
    fun findPatientById(patientId: UUID): Patient
}