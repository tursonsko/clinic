package com.rsq.clinic.service

import com.rsq.clinic.advice.DoctorUnavailableAtThisTime
import com.rsq.clinic.advice.VisitNotCreatedException
import com.rsq.clinic.advice.VisitNotUpdatedException
import com.rsq.clinic.model.dto.VisitCreateRequest
import com.rsq.clinic.model.dto.VisitResponse
import com.rsq.clinic.model.dto.VisitUpdateRequest
import com.rsq.clinic.model.entity.Doctor
import com.rsq.clinic.repository.DoctorRepository
import com.rsq.clinic.repository.PatientRepository
import com.rsq.clinic.repository.VisitRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val patientRepository: PatientRepository,
    private val doctorRepository: DoctorRepository,
) {

    /**
     * Method creating single Visit
     * @return VisitResponse
     * @throws VisitNotCreatedException
     */
    @Throws(DoctorUnavailableAtThisTime::class)
    fun createVisit(
        createRequest: VisitCreateRequest,
        patientId: UUID,
        doctorId: UUID,
    ): VisitResponse {
        val doctor = doctorRepository.findDoctorByIdWithVisits(doctorId)
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

    //todo
    fun changeVisitTime(
        updateRequest: VisitUpdateRequest
    ): VisitResponse {
        val visitId = updateRequest.visitId
        val visitDate = updateRequest.visitDate
        val visitTime = updateRequest.visitTime
        val visitToUpdate = visitRepository.findVisitById(visitId)

        visitToUpdate.doctor?.id?.let { doctorId ->
            val doctor = doctorRepository.findDoctorByIdWithVisits(doctorId)
            checkDoctorAvailability(doctor, visitDate, visitTime)
        }

        try {
            return visitRepository.save(visitToUpdate.copy(visitTime = visitTime)).toVisitData()
        } catch (ex: Exception) {
            throw VisitNotUpdatedException("Visit time not changed")
        }
    }

    //todo try/catch for DELETE operations (?) - some validation
    fun deleteVisit(visitId: UUID) =
        visitRepository.deleteById(visitId)

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
}