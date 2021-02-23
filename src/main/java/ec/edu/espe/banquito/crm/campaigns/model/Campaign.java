package ec.edu.espe.banquito.crm.campaigns.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
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
@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "campaign", indexes = {
    @Index(name = "idx_campaign_status", columnList = "status"),
    @Index(name = "idx_campaign_kindProduct", columnList = "kindProduct")})
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

    @Column(name = "total_number_clients")
    private Integer totalNumberClients;

    @Column(name = "assigned_clients_number")
    private Integer numberAssignedClients;

    @Column(name = "clients_in_progress_number")
    private Integer numberClientsInProgress;

    @Column(name = "accepted_clients_number")
    private Integer numberAccepetdClients;

    @Column(name = "rejected_clients_number")
    private Integer numberRejectedClients;

    @Column(name = "status", length = 3)
    private String status;

    @Column(name = "urlTermsConditions", length = 256)
    private String urlTermsConditions;

    @Column(name = "region", length = 3)
    private String region;

    @Column(name = "kindProduct", length = 3)
    private String kindProduct;

}
