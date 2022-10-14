package com.rsq.clinic.repository

import com.rsq.clinic.model.entity.Visit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VisitRepository : JpaRepository<Visit, UUID> {
}