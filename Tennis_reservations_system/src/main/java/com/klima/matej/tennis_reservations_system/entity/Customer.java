package com.klima.matej.tennis_reservations_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a customer.
 * It is used to store information about the customer.
 * It is also used to map the customer to the database.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SQLDelete(sql = "UPDATE customer SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties({"reservations", "deleted"})
@Entity
public class Customer {
    /**
     * Unique identifier of the customer used as PK in database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * Phone number of the customer.
     * It is used as a unique identifier of the customer.
     */
    @Column(unique = true)
    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    /**
     * Name of the customer.
     */
    private String name;

    /**
     * Set of reservations created by the customer.
     */
    @OneToMany(mappedBy="customer")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Reservation> reservations = new HashSet<>();

    /**
     * Boolean value representing whether the customer is deleted or not.
     * Used for soft delete.
     */
    @Setter(AccessLevel.NONE)
    private boolean deleted = false;
}


