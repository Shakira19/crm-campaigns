/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author cofre
 */

@Data
@Builder
@Entity
@Table(name = "campaign")
public class Campaign {
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name", length = 128)
    private String name;
    
    @Column(name = "description", length = 256)
    private String description;
}
