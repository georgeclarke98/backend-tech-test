package com.example.ffern.waitlist;

import com.example.ffern.users.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "waitlist", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
public class WaitlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity waitlistUser;

    @Column(name = "cohort", nullable = false)
    private String cohort;

    @Column(name = "region", nullable = false)
    private String region;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitlistEntity that = (WaitlistEntity) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(waitlistUser, that.waitlistUser) &&
               Objects.equals(cohort, that.cohort) &&
               Objects.equals(region, that.region) &&
               Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, waitlistUser, cohort, region, createdAt);
    }
}
