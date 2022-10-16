package com.rsq.clinic.service

import com.rsq.clinic.advice.*
import com.rsq.clinic.model.dto.VisitCreateRequest
import com.rsq.clinic.model.dto.VisitResponse
import com.rsq.clinic.model.dto.VisitUpdateRequest
import com.rsq.clinic.model.entity.Doctor
import com.rsq.clinic.repository.DoctorRepository
import com.rsq.clinic.repository.PatientRepository
import com.rsq.clinic.repository.VisitRepository
import com.rsq.clinic.repository.getVisitWithOptionalPatientIdQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

/**
 * Logic for Visit
 */
@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val patientRepository: PatientRepository,
    private val doctorRepository: DoctorRepository,
) {

    /**
     * Method creating single Visit
     */
    fun createVisit(
        createRequest: VisitCreateRequest,
        patientId: UUID,
        doctorId: UUID,
    ): VisitResponse {
        val doctor = doctorRepository.findDoctorById(doctorId)
        checkDoctorAvailability(doctor, createRequest.visitDate, createRequest.visitTime)
        try {
            val patient = patientRepository.findPatientById(patientId)
            return visitRepository.save(
                createRequest.toVisitEntity(patient, doctor)
            ).toVisitData()
        } catch (ex: Exception) {
            throw VisitNotCreatedException("Visit not created")
        }
    }

    /**
     * Method changing Visit Time
     */
    fun changeVisitTime(
        updateRequest: VisitUpdateRequest
    ) {
        val visitId = updateRequest.visitId
        val visitDate = updateRequest.visitDate
        val visitTime = updateRequest.visitTime
        val visitToUpdate = visitRepository.findVisitById(visitId)

        visitToUpdate.doctor?.id?.let { doctorId ->
            val doctor = doctorRepository.findDoctorByIdWithVisits(doctorId)
            checkDoctorAvailability(doctor, visitDate, visitTime)
        }

        try {
            visitRepository.updateVisitTime(visitTime, visitId)
        } catch (ex: Exception) {
            throw VisitNotUpdatedException("Visit time not changed")
        }
    }

    /**
     * Method finding all Visits with optional parameter "patientId" to narrow searching
     */
    fun getAllVisitsWithOptionalPatientId(
        patientId: UUID?,
        pageNumber: Int,
        pageSize: Int
    ): Page<VisitResponse> {
        try {
            return visitRepository.findAll(
                getVisitWithOptionalPatientIdQuery(patientId),
                PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.by(VISIT_DATE).and(Sort.by(VISIT_TIME))
                )
            ).map { it.toVisitData() }
        } catch (ex: Exception) {
            throw VisitNotFoundException("Something went wrong while searching for visits...")
        }
    }

    /**
     * Method to delete single Visit
     */
    fun deleteVisit(visitId: UUID) =
        try {
            visitRepository.deleteById(visitId)
        } catch (ex: Exception) {
            throw DeleteOperationException("Something went wrong while deleting visit")
        }

    /**
     * Validator to check if Doctor is available at provided Time during creating new Visit
     */
    private fun checkDoctorAvailability(
        doctor: Doctor,
        visitDate: LocalDate,
        visitTime: LocalTime
    ): Boolean {
        doctor.visits.forEach {
            if (it.visitDate == visitDate && it.visitTime == visitTime) {
                throw DoctorUnavailableAtThisTime(
                    "Doctor ${doctor.firstName} ${doctor.lastName} is not available " +
                            "at $visitDate $visitTime. Please pick other date"
                )
            }
        }
        return true
    }

    companion object {
        const val VISIT_DATE = "visitDate"
        const val VISIT_TIME = "visitTime"
    }
}