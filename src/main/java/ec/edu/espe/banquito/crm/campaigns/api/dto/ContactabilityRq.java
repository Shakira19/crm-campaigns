package ec.edu.espe.banquito.crm.campaigns.api.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ContactabilityRq {

    private String description;
    private BigDecimal productTotalValue;
    private Integer acceptedFees;
}
