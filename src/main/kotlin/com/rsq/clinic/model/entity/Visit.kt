package com.rsq.clinic.model.entity

import org.hibernate.Hibernate
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.persistence.*

@Table(name = "visit")
@Entity
data class Visit(

    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    @GeneratedValue
    val id: UUID? = null,

    val visitDate: LocalDate,

    val visitTime: LocalTime,

    @ManyToOne(cascade = [CascadeType.ALL]) @JoinColumn(name = "patient_id")
    val patient: Patient,

    @ManyToOne(cascade = [CascadeType.ALL]) @JoinColumn(name = "doctor_id")
    val doctor: Doctor

) {
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
