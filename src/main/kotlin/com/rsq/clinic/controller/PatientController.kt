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

@RequestMapping("/api/patient")
@RestController
class PatientController(
    private val patientService: PatientService
) {

    @PostMapping
    fun createPatient(
        @RequestBody createRequest: PatientCreateRequest
    ): ResponseEntity<PatientResponse> =
        ResponseEntity(
            patientService.createPatient(createRequest),
            HttpStatus.CREATED
        )

    @GetMapping("/{patientId}")
    fun getPatient(
        @PathVariable patientId: UUID
    ): ResponseEntity<PatientResponse> =
        ResponseEntity(
            patientService.getPatient(patientId),
            HttpStatus.OK
        )

    @GetMapping()
    fun getAllPatients(
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int
    ): ResponseEntity<Page<PatientResponse>> =
        ResponseEntity(
            patientService.getAllPatients(pageNumber, pageSize),
            HttpStatus.OK
        )

    @PutMapping("/{patientId}")
    fun updatePatient(
        @PathVariable patientId: UUID,
        @RequestBody updateRequest: PatientUpdateRequest
    ): ResponseEntity<PatientResponse> =
        ResponseEntity(
            patientService.updatePatient(patientId, updateRequest),
            HttpStatus.OK
        )

    @DeleteMapping("/{patientId}")
    fun deletePatient(
        @PathVariable patientId: UUID
    ): ResponseEntity<Void> {
        patientService.deletePatient(patientId)
        return ResponseEntity.ok().build()
    }
}