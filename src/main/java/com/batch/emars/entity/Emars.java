package com.batch.emars.entity;

import com.batch.emars.generator.Identifiable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
//public class Emars implements Identifiable<Long> {
public class Emars {
    @Id
    /*@GenericGenerator(
            name = "assigned-sequence",
            strategy = "com.batch.emars.generator.AssignedSequenceStyleGenerator",
            parameters = @org.hibernate.annotations.Parameter(
                    name = "sequence_name",
                    value = "post_sequence"
            )
    )
    @GeneratedValue(
            generator = "assigned-sequence",
            strategy = GenerationType.SEQUENCE
    )*/
    @Column(name="id")
    private Long id;
    private String uploadLink;
    private String bciPn;
    private String description;
    private Integer level;
    private String type;
    private String username;
    private String bciPnParent;
    private String serialNo;
    private String computedRohsStatus;
    private String justification;
    private LocalDate bomUploadDate;
    private String parentSerialNo;
    private String site;
    private String cpKey;
    private String justificationForRohsParent;
    private String comments;
}
