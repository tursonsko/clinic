package com.rsq.clinic.controller

import com.rsq.clinic.model.dto.DoctorCreateRequest
import com.rsq.clinic.model.dto.DoctorResponse
import com.rsq.clinic.model.dto.DoctorUpdateRequest
import com.rsq.clinic.service.DoctorService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Rest Controller for Doctor
 */
@RequestMapping("/api/doctor")
@RestController
class DoctorController(
    private val doctorService: DoctorService
) {
    /**
     * Endpoint to create single Doctor
     */
    @PostMapping
    fun createDoctor(
        @RequestBody createRequest: DoctorCreateRequest
    ): ResponseEntity<DoctorResponse> =
        ResponseEntity(
            doctorService.createDoctor(createRequest),
            HttpStatus.CREATED
        )

    /**
     * Endpoint to get single Doctor
     */
    @GetMapping("/{doctorId}")
    fun getDoctor(
        @PathVariable doctorId: UUID
    ): ResponseEntity<DoctorResponse> =
        ResponseEntity(
            doctorService.getDoctor(doctorId),
            HttpStatus.OK
        )

    /**
     * Endpoint to get all Doctors in Pages
     */
    @GetMapping()
    fun getAllDoctors(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = Int.MAX_VALUE.toString()) pageSize: Int
    ): ResponseEntity<Page<DoctorResponse>> =
        ResponseEntity(
            doctorService.getAllDoctors(pageNumber, pageSize),
            HttpStatus.OK
        )

    /**
     * Endpoint to edit single Doctor Data
     */
    @PutMapping("/{doctorId}")
    fun updateDoctor(
        @PathVariable doctorId: UUID,
        @RequestBody updateRequest: DoctorUpdateRequest
    ): ResponseEntity<DoctorResponse> =
        ResponseEntity(
            doctorService.updateDoctor(doctorId, updateRequest),
            HttpStatus.OK
        )

    /**
     * Endpoint to delete single Doctor by id
     */
    @DeleteMapping("/{doctorId}")
    fun deleteDoctor(
        @PathVariable doctorId: UUID
    ): ResponseEntity<Void> {
        doctorService.deleteDoctor(doctorId)
        return ResponseEntity.noContent().build()
    }

}