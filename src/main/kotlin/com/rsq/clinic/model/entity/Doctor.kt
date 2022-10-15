package com.rsq.clinic.model.entity

import com.rsq.clinic.model.dto.DoctorResponse
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Table(name = "doctor")
@Entity
data class Doctor(

    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    @GeneratedValue
    val id: UUID? = null,

    val firstName: String,

    val lastName: String,

    val specialization: String,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val visits: List<Visit> = mutableListOf()

) {

    fun toDoctorData() =
        DoctorResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            specialization = specialization
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
