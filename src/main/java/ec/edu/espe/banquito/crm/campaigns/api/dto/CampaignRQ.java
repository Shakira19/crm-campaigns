/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.api.dto;

import java.sql.Date;
import lombok.Data;

/**
 *
 * @author cofre
 */
@Data
public class CampaignRQ {

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String urlTermsConditions;
    private String region;
    private String kindProduct;

}
