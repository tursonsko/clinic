package com.rsq.clinic.service

import com.rsq.clinic.advice.DeleteOperationException
import com.rsq.clinic.advice.PatientNotCreatedException
import com.rsq.clinic.advice.PatientNotFoundException
import com.rsq.clinic.model.dto.PatientCreateRequest
import com.rsq.clinic.model.dto.PatientResponse
import com.rsq.clinic.model.dto.PatientUpdateRequest
import com.rsq.clinic.repository.PatientRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

/**
 * Logic for Patient
 */
@Service
class PatientService(
    private val patientRepository: PatientRepository
) {

    /**
     * Method creating single Patient
     */
    fun createPatient(createRequest: PatientCreateRequest): PatientResponse {
        createRequest.checkRequestFields()
        try {
            return patientRepository.save(createRequest.toPatientEntity()).toPatientData()
        } catch (ex: Exception) {
            throw PatientNotCreatedException("Patient not created")
        }
    }

    /**
     * Method finding single Patient
     */
    fun getPatient(patientId: UUID): PatientResponse {
        try {
            return patientRepository.findPatientById(patientId).toPatientData()
        } catch (ex: Exception) {
            throw PatientNotFoundException("Patient not found for provided ID")
        }
    }

    /**
     * Method finding all Patients in Pages
     */
    fun getAllPatients(pageNumber: Int, pageSize: Int): Page<PatientResponse> {
        return patientRepository.findAll(
            PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(LAST_NAME).and(Sort.by(FIRST_NAME))
            )
        ).map { it.toPatientData() }
    }

    /**
     * Method updating single Patient
     */
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

    /**
     * Method deleting single Patient
     */
    fun deletePatient(patientId: UUID) {
        try {
            patientRepository.deleteById(patientId)
        } catch (ex: Exception) {
            throw DeleteOperationException("Something went wrong while deleting patient")
        }
    }

    companion object {
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
    }
}