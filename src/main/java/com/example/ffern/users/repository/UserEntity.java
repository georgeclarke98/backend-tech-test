package com.example.ffern.users.repository;

import com.example.ffern.waitlist.WaitlistEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "opt_in", nullable = false)
    private Boolean optIn = true;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = false)
    private Timestamp updatedAt;

    @OneToOne(mappedBy = "waitlistUser", cascade = CascadeType.REMOVE)
    private WaitlistEntity waitlist;

    @OneToMany(mappedBy = "analyticsUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<UserAnalyticsEntity> analytics;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(firstName, that.firstName) &&
               Objects.equals(lastName, that.lastName) &&
               Objects.equals(email, that.email) &&
               Objects.equals(phoneNumber, that.phoneNumber) &&
               Objects.equals(optIn, that.optIn) &&
               Objects.equals(createdAt, that.createdAt) &&
               Objects.equals(updatedAt, that.updatedAt) &&
               Objects.equals(waitlist, that.waitlist);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, email, phoneNumber, optIn, createdAt, updatedAt, waitlist);
    }
}
