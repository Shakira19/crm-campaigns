package ec.edu.espe.banquito.crm.campaigns.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author cofre
 */
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contactabilityregistration", indexes = {
    @Index(name = "idx_ctcreg_status", columnList = "status")})
public class ContactabilityRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_client", length = 24)
    private String idClient;
    
    @Column(name = "client_name", length = 128)
    private String clientName;

    @Column(name = "client_surname", length = 128)
    private String clientSurname;
    
    @Column(name = "client_phone", length = 128)
    private String clientPhone;
    
    @Column(name = "client_email", length = 128)
    private String clientEmail;
    
    @ManyToOne
    @JoinColumn(name = "id_campaign")
    private Campaign campaign;

    @Column(name = "status", length = 3)
    private String status;
    
    
}
