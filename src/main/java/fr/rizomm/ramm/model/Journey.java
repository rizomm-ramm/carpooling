package fr.rizomm.ramm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@EqualsAndHashCode(of = "id")
@Entity(name = "journeys")
public class Journey {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "journey")
    private List<StopOff> stopOffs;

}
