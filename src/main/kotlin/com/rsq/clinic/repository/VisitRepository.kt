package com.rsq.clinic.repository

import com.rsq.clinic.model.entity.Visit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalTime
import java.util.*
import javax.transaction.Transactional


@Repository
interface VisitRepository : JpaRepository<Visit, UUID>, JpaSpecificationExecutor<Visit> {

    fun findVisitById(visitId: UUID): Visit

    @Modifying
    @Transactional
    @Query("UPDATE visit v SET v.visitTime = ?1 WHERE v.id = ?2")
    fun updateVisitTime(visitTime: LocalTime, visitId: UUID)


/*    fun findVisitByIdAndVisitDateAndVisitTime(
        visitId: UUID?,
        visitDate: LocalDate?,
        visitTime: LocalTime?
    ): Visit*/
}