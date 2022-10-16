package com.rsq.clinic.repository

import com.rsq.clinic.model.entity.Doctor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DoctorRepository : JpaRepository<Doctor, UUID> {
    fun findDoctorById(doctorId: UUID): Doctor

    @Query("FROM doctor d JOIN FETCH d.visits WHERE d.id = ?1")
    fun findDoctorByIdWithVisits(doctorId: UUID): Doctor

}