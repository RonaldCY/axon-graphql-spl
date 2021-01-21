package org.ha.spl.query.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ward {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "hospCode")
    private Hospital hospital;

    private String wardCode;
}
