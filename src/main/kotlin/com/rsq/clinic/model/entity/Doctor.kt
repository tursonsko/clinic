package com.rsq.clinic.model.entity

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

    val name: String,

    val surname: String,

    val specialization: String,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val visits: List<Visit> = mutableListOf()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Doctor

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , surname = $surname , specialization = $specialization )"
    }
}
