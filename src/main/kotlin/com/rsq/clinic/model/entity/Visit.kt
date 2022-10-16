package com.rsq.clinic.model.entity

import com.rsq.clinic.model.dto.VisitResponse
import org.hibernate.Hibernate
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.persistence.*

@Table(name = "visit")
@Entity(name = "visit")
data class Visit(

    val visitDate: LocalDate,

    val visitTime: LocalTime,

    val spot: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    val patient: Patient? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    val doctor: Doctor? = null

) {

    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    @GeneratedValue
    val id: UUID? = null

    fun toVisitData() =
        VisitResponse(
            id = id,
            visitDate = visitDate,
            visitTime = visitTime,
            spot = spot,
            patient = patient?.toPatientData(),
            doctor = doctor?.toDoctorData(),
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Visit

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , visitDate = $visitDate , visitTime = $visitTime , patient = $patient , doctor = $doctor )"
    }
}
