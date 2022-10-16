package com.rsq.clinic.repository

import com.rsq.clinic.model.entity.Visit
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

/**
 * Specification used to pass filter with optional patientId
 * in VisitService.getAllVisitsWithOptionalPatientId method
 */
fun getVisitWithOptionalPatientIdQuery(
    patientId: UUID?
): Specification<Visit> {
    return Specification { root: Root<Visit>, _: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
        val predicates = mutableListOf<Predicate>()
        patientId?.let {
            predicates.add(criteriaBuilder.equal(root.join<Any, Any>("patient").get<Any>("id"), patientId))
        }
        criteriaBuilder.and(*predicates.toTypedArray())
    }
}