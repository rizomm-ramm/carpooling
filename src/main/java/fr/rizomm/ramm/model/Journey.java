package fr.rizomm.ramm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "stopOffs")
@Builder
@Entity(name = "journeys")
public class Journey {

    public enum Status {
        INITIALIZED,
        ACTIVATED,
        CANCELED,
        DONE
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "journey")
    private List<StopOff> stopOffs;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.INITIALIZED;

}
