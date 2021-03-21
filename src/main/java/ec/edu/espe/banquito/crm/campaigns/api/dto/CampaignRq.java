package ec.edu.espe.banquito.crm.campaigns.api.dto;

import java.sql.Date;
import lombok.Data;

@Data
public class CampaignRq {

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String urlTermsConditions;
    private String region;
    private String kindProduct;

}
