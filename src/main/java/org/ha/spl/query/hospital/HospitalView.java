package org.ha.spl.query.hospital;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalView {
    @Id
    private String hospCode;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> wards;
}
