package com.example.covid.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "medicalrecord")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patient implements Serializable {
    @Column(name = "id")
    @Id
    private Integer id;

    @Column(name = "disease_id")
    private String diseaseId;

    @Column(name = "status")
    private String status;

    @Column(name = "confirmed_date")
    private Date confirmedDate;

    @Column(name = "detail")
    private String detail;

    @Column(name="type")
    private String type; // bệnh nhân mắc ngoài cộng đồng hay trong khu cách ly .v.v.v

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

//    @ManyToOne
//    @JoinColumn(name = "disease_id", referencedColumnName = "id")
//    private Disease disease;


    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", diseaseId='" + diseaseId + '\'' +
                ", status='" + status + '\'' +
                ", confirmedDate=" + confirmedDate.toString() +
                ", detail='" + detail + '\'' +
                ", type='" + type + '\'' +
                ", person=" + person +
                '}';
    }
}
