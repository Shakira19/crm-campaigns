/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author cofre
 */

@Data
@Builder
@Entity
@Table(name = "contactabilityregistration")
public class ContactabilityRegistration {
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "id_client", length = 24)
    private String idClient;
    
    @ManyToOne
    @JoinColumn(name = "id_campaign")
    private Campaign campaign;
    
    @Column(name = "status")
    private String status;
}
