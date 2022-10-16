package com.rsq.clinic.model.entity

import com.rsq.clinic.model.dto.PatientResponse
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Table(name = "patient")
@Entity(name = "patient")
data class Patient(

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "address", nullable = false)
    val address: String,

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER)
    val visits: List<Visit> = mutableListOf()

) {

    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    @GeneratedValue
    val id: UUID? = null

    fun toPatientData() =
        PatientResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            address = address
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Patient

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "Patient(id=$id, firstName='$firstName', lastName='$lastName', address='$address', visits=$visits)"
    }
}
