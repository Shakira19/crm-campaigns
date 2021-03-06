package ec.edu.espe.banquito.crm.campaigns.model;

import java.math.BigDecimal;
import java.util.Date;
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
    @Index(name = "idx_ctcreg_status", columnList = "status"),
    @Index(name = "idx_ctcreg_clientIdentification", columnList = "client_identification")})
public class ContactabilityRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "client_id", length = 24)
    private String clientId;

    @Column(name = "client_identification", length = 13)
    private String clientIdentification;

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
    
    @Column(name = "accepted_fees")
    private Integer acceptedFees;
    
    @Column(name = "solca_tax")
    private Float solcaTax;
    
    @Column(name = "modification_date")
    private Date modificationDate;
    
    @Column(name = "product_total_value")
    private BigDecimal productTotalValue;
    
    @Column(name = "product_interest")
    private Integer productInterest;

}
