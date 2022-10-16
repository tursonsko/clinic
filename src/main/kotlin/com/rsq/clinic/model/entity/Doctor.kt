package com.rsq.clinic.model.entity

import com.rsq.clinic.model.dto.DoctorResponse
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Table(name = "doctor")
@Entity(name = "doctor")
data class Doctor(

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "specialization", nullable = false)
    val specialization: String,

    @OneToMany(mappedBy = "doctor", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    val visits: List<Visit> = mutableListOf()

) {

    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    @GeneratedValue
    val id: UUID? = null

    fun toDoctorData() =
        DoctorResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            specialization = specialization
        )

    fun toDoctorDataWithVisits() =
        DoctorResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            specialization = specialization,
            visits = visits
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Doctor

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "Doctor(id=$id, firstName='$firstName', lastName='$lastName', specialization='$specialization', visits=$visits)"
    }
}
