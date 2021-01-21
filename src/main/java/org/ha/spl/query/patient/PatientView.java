package org.ha.spl.query.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientView {
    @Id
    private String hkid;
    private String name;

    private String hospCode;
    private String wardCode;
    private Integer bedNum;
}
