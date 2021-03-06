package ec.edu.espe.banquito.crm.campaigns.repository;

import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactabilityRegistrationRepository extends JpaRepository<ContactabilityRegistration, Integer> {

    List<ContactabilityRegistration> findAll();

    Optional<ContactabilityRegistration> findById(Integer id);

    List<ContactabilityRegistration> findByStatusIs(String status);

    List<ContactabilityRegistration> findByStatusIn(List<String> statuses);

    List<ContactabilityRegistration> findByClientEmail(String clientEmail);

    List<ContactabilityRegistration> findByClientIdentificationOrderByClientSurnameDesc(String clientIdentification);

    ContactabilityRegistration findByClientIdentificationAndCampaign(String clientIdentification, Campaign campaign);

    List<ContactabilityRegistration> findByClientPhone(String clientPhone);

    List<ContactabilityRegistration> findByClientNameIgnoringCaseLikeAndClientSurnameIgnoringCaseLike(
            String clientName, 
            String clientSurname);

    List<ContactabilityRegistration> findByCampaign(Campaign campaign);

}
