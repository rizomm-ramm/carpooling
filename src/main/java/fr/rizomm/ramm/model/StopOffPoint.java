package fr.rizomm.ramm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "stopOffs")
@Table(name = "stopoff_point")
@Entity
public class StopOffPoint {

    public enum Type {
        DEPARTURE,
        ARRIVAL
    }

    @Id
    @GeneratedValue
    private Long id; //NOSONAR

    @Column(name = "latitude", nullable = false)
    private double latitude; //NOSONAR

    @Column(name = "longitude", nullable = false)
    private double longitude; //NOSONAR

    @Column(name = "address", nullable = false)
    private String address; //NOSONAR

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date date; //NOSONAR

    @Column(name = "description", nullable = true)
    private String description; //NOSONAR

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type; //NOSONAR

    @OneToMany
    @JsonIgnore
    private List<StopOff> stopOffs; //NOSONAR
}
