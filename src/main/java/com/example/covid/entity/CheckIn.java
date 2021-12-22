package com.example.covid.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Table(name = "checkinhistory")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CheckIn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "date")
    private Date date;

    @Column(name = "starttime")
    private String startTime;

    @Column(name = "endtime")
    private String endTime;

    @Column(name = "description")
    private String description;

    @Override
    public String toString() {
        return "CheckIn{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
