package com.spring.global.entity

import org.hibernate.Hibernate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
    protected set

  @Column(updatable = false, nullable = false)
  @CreatedDate
  var createdAt: LocalDateTime? = null
    protected set

  @Column(nullable = false)
  @LastModifiedDate
  var updatedAt: LocalDateTime? = null
    protected set

  fun isNew(): Boolean = id == null

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as BaseEntity

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()
}
