package com.klima.matej.tennis_reservations_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a tennis court.
 * It is used to store information about the court.
 * It is also used to map the court to the database.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SQLDelete(sql = "UPDATE court SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties({"reservations", "deleted"})
@Entity

public class Court {
    /**
     * Unique identifier of the court used as PK in database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * Name of the court.
     */
    private String name;

    /**
     * Surface of the court represented by Surface object.
     */
    @ManyToOne
    @JoinColumn(name="surface_id", nullable=false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Surface surface;

    /**
     * Set of reservations of the court.
     */
    @OneToMany(mappedBy="court")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Reservation> reservations = new HashSet<>();

    /**
     * Boolean value representing whether the court is deleted or not.
     * Used for soft delete.
     */
    @Setter(AccessLevel.NONE)
    private boolean deleted = false;
}
