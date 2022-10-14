package com.rsq.clinic.service

import com.rsq.clinic.advice.PatientNotFoundException
import com.rsq.clinic.model.dto.PatientCreateRequest
import com.rsq.clinic.model.dto.PatientResponse
import com.rsq.clinic.model.dto.PatientUpdateRequest
import com.rsq.clinic.model.entity.Patient
import com.rsq.clinic.repository.PatientRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PatientService(
    private val patientRepository: PatientRepository
) {
    /**
     * Method creating single Patient
     * @return PatientResponse
     */
    fun createPatient(createRequest: PatientCreateRequest) =
        patientRepository.save(createRequest.toPatientEntity()).toPatientData()

    fun getPatient(patientId: UUID): PatientResponse {
        try {
            return patientRepository.findPatientById(patientId).toPatientData()
        } catch (ex: Exception) {
            throw PatientNotFoundException("Patient not found for provided ID")
        }
    }

    //todo remember about visits - lazy ex...
    fun getAllPatients(pageNumber: Int, pageSize: Int): Page<PatientResponse> {
        return patientRepository.findAll(
            PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(LAST_NAME).ascending()
            )
        ).map { it.toPatientData() }
    }

    fun updatePatient(patientId: UUID, updateRequest: PatientUpdateRequest): PatientResponse {
        try {
            return patientRepository.findPatientById(patientId).let {
                patientRepository.save(
                    it.copy(
                        firstName = updateRequest.firstName ?: it.firstName,
                        lastName = updateRequest.lastName ?: it.lastName,
                        address = updateRequest.address ?: it.address
                    )
                )
            }.toPatientData()
        } catch (ex: Exception) {
            throw PatientNotFoundException("Patient not found for provided ID")
        }

    }

    fun deletePatient(patientId: UUID) =
        patientRepository.deleteById(patientId)

    companion object {
        const val LAST_NAME = "lastName"
    }
}