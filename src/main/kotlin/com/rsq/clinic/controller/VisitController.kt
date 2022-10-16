package com.rsq.clinic.controller

import com.rsq.clinic.model.dto.VisitCreateRequest
import com.rsq.clinic.model.dto.VisitResponse
import com.rsq.clinic.model.dto.VisitUpdateRequest
import com.rsq.clinic.service.VisitService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/api/visit")
@RestController
class VisitController(
    private val visitService: VisitService
) {

    @PostMapping
    fun createVisit(
        @RequestParam patientId: UUID,
        @RequestParam doctorId: UUID,
        @RequestBody createRequest: VisitCreateRequest
    ): ResponseEntity<VisitResponse> =
        ResponseEntity(
            visitService.createVisit(createRequest, patientId, doctorId),
            HttpStatus.CREATED
        )

    @PutMapping("/changeTime")
    fun changeVisitTime(
        @RequestBody updateRequest: VisitUpdateRequest
    ): ResponseEntity<VisitResponse> {
        visitService.changeVisitTime(updateRequest)
        return ResponseEntity.noContent().build()

    }

    @GetMapping
    fun getAllVisitsWithOptionalPatientId(
        @RequestParam(required = false) patientId: UUID?,
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = Int.MAX_VALUE.toString()) pageSize: Int
    ): ResponseEntity<Page<VisitResponse>> =
        ResponseEntity(
            visitService.getAllVisitsWithOptionalPatientId(patientId, pageNumber, pageSize),
            HttpStatus.OK
        )



    @DeleteMapping("/{visitId}")
    fun deleteVisit(
        @PathVariable visitId: UUID
    ): ResponseEntity<Void> {
        visitService.deleteVisit(visitId)
        return ResponseEntity.noContent().build()
    }
}