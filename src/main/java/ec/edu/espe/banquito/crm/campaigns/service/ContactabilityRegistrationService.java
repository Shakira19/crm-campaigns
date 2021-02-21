/*
 * Creation date: 21 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.repository.ContactabilityRegistrationRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alan Quimbita
 */
@Service
@Slf4j
public class ContactabilityRegistrationService {
    private final ContactabilityRegistrationRepository contactabilityRegistrationRepo;

    public ContactabilityRegistrationService(ContactabilityRegistrationRepository contactabilityRegistrationRepo) {
        this.contactabilityRegistrationRepo = contactabilityRegistrationRepo;
    }
    
    public List<ContactabilityRegistration> getContactabilityRegistryByStatus(String status){        
        return this.contactabilityRegistrationRepo.findByStatusIs(status);
    }
    
    public List<ContactabilityRegistration> getContactabilityRegistryByStatusIn(List<String> statuses){
        return this.contactabilityRegistrationRepo.findByStatusIn(statuses);
    }
}
