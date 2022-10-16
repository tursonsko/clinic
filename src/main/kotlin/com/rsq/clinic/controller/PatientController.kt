package com.rsq.clinic.controller

import com.rsq.clinic.model.dto.PatientCreateRequest
import com.rsq.clinic.model.dto.PatientResponse
import com.rsq.clinic.model.dto.PatientUpdateRequest
import com.rsq.clinic.service.PatientService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Rest Controller for Patient
 */
@RequestMapping("/api/patient")
@RestController
class PatientController(
    private val patientService: PatientService
) {

    /**
     * Endpoint to create single Patient
     */
    @PostMapping
    fun createPatient(
        @RequestBody createRequest: PatientCreateRequest
    ): ResponseEntity<PatientResponse> =
        ResponseEntity(
            patientService.createPatient(createRequest),
            HttpStatus.CREATED
        )

    /**
     * Endpoint to get single Patient
     */
    @GetMapping("/{patientId}")
    fun getPatient(
        @PathVariable patientId: UUID
    ): ResponseEntity<PatientResponse> =
        ResponseEntity(
            patientService.getPatient(patientId),
            HttpStatus.OK
        )

    /**
     * Endpoint to get all Patients in Pages
     */
    @GetMapping()
    fun getAllPatients(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = Int.MAX_VALUE.toString()) pageSize: Int
    ): ResponseEntity<Page<PatientResponse>> =
        ResponseEntity(
            patientService.getAllPatients(pageNumber, pageSize),
            HttpStatus.OK
        )

    /**
     * Endpoint to edit single Patient Data
     */
    @PutMapping("/{patientId}")
    fun updatePatient(
        @PathVariable patientId: UUID,
        @RequestBody updateRequest: PatientUpdateRequest
    ): ResponseEntity<PatientResponse> =
        ResponseEntity(
            patientService.updatePatient(patientId, updateRequest),
            HttpStatus.OK
        )

    /**
     * Endpoint to delete single Patient by id
     */
    @DeleteMapping("/{patientId}")
    fun deletePatient(
        @PathVariable patientId: UUID
    ): ResponseEntity<Void> {
        patientService.deletePatient(patientId)
        return ResponseEntity.noContent().build()
    }
}