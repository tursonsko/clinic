package com.rsq.clinic.service

import com.rsq.clinic.advice.DeleteOperationException
import com.rsq.clinic.advice.DoctorNotCreatedException
import com.rsq.clinic.advice.DoctorNotFoundException
import com.rsq.clinic.model.dto.DoctorCreateRequest
import com.rsq.clinic.model.dto.DoctorResponse
import com.rsq.clinic.model.dto.DoctorUpdateRequest
import com.rsq.clinic.repository.DoctorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

/**
 * Logic for Doctor
 */
@Service
class DoctorService(
    private val doctorRepository: DoctorRepository
) {

    /**
     * Method creating single Doctor
     */
    fun createDoctor(createRequest: DoctorCreateRequest): DoctorResponse {
        createRequest.checkRequestFields()
        try {
            return doctorRepository.save(createRequest.toDoctorEntity()).toDoctorData()
        } catch (ex: Exception) {
            throw DoctorNotCreatedException("Doctor not created")
        }
    }

    /**
     * Method finding single Doctor
     */
    fun getDoctor(doctorId: UUID): DoctorResponse {
        try {
            return doctorRepository.findDoctorById(doctorId).toDoctorData()
        } catch (ex: Exception) {
            throw DoctorNotFoundException("Doctor not found for provided ID")
        }
    }

    /**
     * Method finding in pages Doctors
     */
    fun getAllDoctors(pageNumber: Int, pageSize: Int): Page<DoctorResponse> {
        return doctorRepository.findAll(
            PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(LAST_NAME).and(Sort.by(FIRST_NAME))
            )
        ).map { it.toDoctorData() }
    }

    /**
     * Method updating single Doctor
     */
    fun updateDoctor(doctorId: UUID, updateRequest: DoctorUpdateRequest): DoctorResponse {
        try {
            return doctorRepository.findDoctorById(doctorId).let {
                doctorRepository.save(
                    it.copy(
                        firstName = updateRequest.firstName ?: it.firstName,
                        lastName = updateRequest.lastName ?: it.lastName,
                        specialization = updateRequest.specialization ?: it.specialization
                    )
                )
            }.toDoctorData()
        } catch (ex: Exception) {
            throw DoctorNotFoundException("Doctor not found for provided ID")
        }

    }

    /**
     * Method deleting single Doctor by id
     */
    fun deleteDoctor(doctorId: UUID) {
        try {
            doctorRepository.deleteById(doctorId)
        } catch (ex: Exception) {
            throw DeleteOperationException("Something went wrong while deleting doctor")
        }
    }

    companion object {
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
    }
}