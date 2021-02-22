package ec.edu.espe.banquito.crm.campaigns.model;


import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@Table(name = "campaign")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name", length = 128)
    private String name;
    
    @Column(name = "description", length = 256)
    private String description;
    
    @Column(name = "startDate")
    private Date startDate;
    
    @Column(name = "endDate")
    private Date endDate;
    
    @Column(name = "totalNumberClients")
    private Integer totalNumberClients;
    
    @Column(name = "assignedClientsNumber")
    private Integer numberAssignedClients;
    
    @Column(name = "clientsInProgressNumber")
    private Integer numberClientsInProgress;
    
    @Column(name = "rejectedClientsNumber")
    private Integer numberReejectedClients;
    
    @Column(name = "status", length = 3)
    private String status;
    
    @Column(name = "urlTermsConditions", length = 256)
    private String urlTermsConditions;
    
    @Column(name = "region", length = 3)
    private String region;
    
    @Column(name = "kindProduct", length = 3)
    private String kindProduct;
    
    
}
