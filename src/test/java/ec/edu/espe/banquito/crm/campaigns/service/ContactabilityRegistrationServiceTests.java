package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.api.dto.ContactabilityRq;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.exception.NotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.UpdateException;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.repository.CampaignRepository;
import ec.edu.espe.banquito.crm.campaigns.repository.ContactabilityRegistrationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Alan Quimbita
 */
@ExtendWith(MockitoExtension.class)
public class ContactabilityRegistrationServiceTests {

    @Mock
    private ContactabilityRegistrationRepository contactabilityRepo;

    @Mock
    private CampaignRepository campaignRepo;

    @InjectMocks
    private ContactabilityRegistrationService service;
    private List<ContactabilityRegistration> contactabilityList;
    private ContactabilityRegistration contactability1;
    private ContactabilityRegistration contactability2;

    @BeforeEach
    public void setUp() {
        contactability1 = new ContactabilityRegistration();
        contactability2 = new ContactabilityRegistration();
        contactabilityList = new ArrayList<ContactabilityRegistration>();
        contactabilityList.add(contactability1);
        contactabilityList.add(contactability2);
    }

    @Test
    public void GivenStatusReturnListOfContactabilityRegistry() {
        when(contactabilityRepo.findByStatusIs(anyString())).thenReturn(contactabilityList);
        try {
            Assertions.assertEquals(contactabilityList, service.getContactabilityRegistryByStatus(""));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenStatusAndGetEmptyListThrowRegistryNotFoundException() {
        when(contactabilityRepo.findByStatusIs(anyString())).thenReturn(new ArrayList<ContactabilityRegistration>());
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getContactabilityRegistryByStatus(""));
    }

    @Test
    public void GivenClientEmailReturnListOfContactabilityRegistry() {
        when(contactabilityRepo.findByClientEmail(anyString())).thenReturn(contactabilityList);
        try {
            Assertions.assertEquals(contactabilityList, service.getContactabilityRegistrationByEmail(""));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenClientEmailAndGetEmptyListThrowRegistryNotFoundException() {
        when(contactabilityRepo.findByClientEmail(anyString())).thenReturn(new ArrayList<ContactabilityRegistration>());
        Assertions.assertThrows(RegistryNotFoundException.class, () -> Assertions.assertEquals(contactabilityList, service.getContactabilityRegistrationByEmail("")));
    }

    @Test
    public void GivenClientIdentificationReturnListOfContactabilityRegistry() {
        when(contactabilityRepo.findByClientIdentificationOrderByClientSurnameDesc(anyString())).thenReturn(contactabilityList);
        try {
            Assertions.assertEquals(contactabilityList, service.getContactabilityRegistryByClientIdentification(""));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenClientIdentificationAndGetEmptyListThrowRegistryNotFoundException() {
        when(contactabilityRepo.findByClientIdentificationOrderByClientSurnameDesc(anyString())).thenReturn(new ArrayList<ContactabilityRegistration>());
        Assertions.assertThrows(RegistryNotFoundException.class, () -> Assertions.assertEquals(contactabilityList, service.getContactabilityRegistryByClientIdentification("")));
    }

    @Test
    public void GivenIdentificationAndCampaignIdReturnContactabilityRegistration() {
        Campaign campaign = new Campaign();
        when(campaignRepo.findById(anyInt())).thenReturn(Optional.of(campaign));
        when(contactabilityRepo.findByClientIdentificationAndCampaign("", campaign)).thenReturn(contactability1);
        try {
            Assertions.assertEquals(contactability1, service.getContactabilityRegistryByClientIdentificationAndCampaign("", 1));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdentificationAndCampaignIdThrowRegistryNotFoundException() {
        Campaign campaign = new Campaign();
        when(campaignRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getContactabilityRegistryByClientIdentificationAndCampaign("", 1));
    }

    @Test
    public void GivenClientPhoneReturnListOfContactabilityRegistration() {
        when(contactabilityRepo.findByClientPhone(anyString())).thenReturn(contactabilityList);
        try {
            Assertions.assertEquals(contactabilityList, service.getContactabilityRegistrationByClientPhone(""));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenClientPhoneThrowRegistryNotFounException() {
        when(contactabilityRepo.findByClientPhone(anyString())).thenReturn(new ArrayList<ContactabilityRegistration>());
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getContactabilityRegistrationByClientPhone(""));
    }

    @Test
    public void GivenNameAndSurnameReturnListOfContactabilityRegistry() {
        when(contactabilityRepo.findByClientNameIgnoringCaseLikeAndClientSurnameIgnoringCaseLike(anyString(), anyString())).thenReturn(contactabilityList);
        try {
            Assertions.assertEquals(contactabilityList, service.getContactabilityRegistrationByClientNameAndSurname("", ""));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenNameAndSurnameThrowRegistryNotFoundException() {
        when(contactabilityRepo.findByClientNameIgnoringCaseLikeAndClientSurnameIgnoringCaseLike(anyString(), anyString())).thenReturn(new ArrayList<ContactabilityRegistration>());
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getContactabilityRegistrationByClientNameAndSurname("", ""));
    }

    @Test
    public void GivenCampaignIdReturnListOfContactabilityRegistry() {
        Campaign campaign = new Campaign();
        when(campaignRepo.findById(anyInt())).thenReturn(Optional.of(campaign));
        when(contactabilityRepo.findByCampaign(campaign)).thenReturn(contactabilityList);
        Assertions.assertEquals(contactabilityList, service.getContactabilityRegistrationByCampaign(1));
    }

    @Test
    public void GivenCampaignIdThrowNotFoundExceptionByContactabilities() {
        Campaign campaign = new Campaign();
        when(campaignRepo.findById(anyInt())).thenReturn(Optional.of(campaign));
        when(contactabilityRepo.findByCampaign(campaign)).thenReturn(new ArrayList<ContactabilityRegistration>());
        Assertions.assertThrows(NotFoundException.class, () -> service.getContactabilityRegistrationByCampaign(1));
    }

    @Test
    public void GivenCampaignIdThrowNotFoundExceptionByCampaign() {
        when(campaignRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(NotFoundException.class, () -> service.getContactabilityRegistrationByCampaign(1));
    }

    @Test
    public void GivenIdAndStatusUpdateContact() {
        Campaign campaign = new Campaign();
        contactability1.setCampaign(campaign);
        contactability1.setStatus(ContactStatusEnum.ASSIGNED.getStatus());
        campaign.setTotalNumberClients(0);
        campaign.setNumberAcceptedClients(0);
        campaign.setNumberAssignedClients(0);
        campaign.setNumberClientsInProgress(0);
        campaign.setNumberRejectedClients(0);
        when(contactabilityRepo.findById(anyInt())).thenReturn(Optional.of(contactability1));
        when(contactabilityRepo.save(any())).thenReturn(contactability1);
        when(campaignRepo.save(any())).thenReturn(campaign);
        try {
            service.actualizarContacto(1, ContactStatusEnum.ASSIGNED);
            service.actualizarContacto(1, ContactStatusEnum.ACCEPTED);
            service.actualizarContacto(1, ContactStatusEnum.INPROGRESS);
            service.actualizarContacto(1, ContactStatusEnum.REJECTED);
            service.actualizarContacto(1, ContactStatusEnum.REJECTED);
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndStatusAndContactabilityWasNotFoundThrowRegistyNotFoundException() {
        when(contactabilityRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.actualizarContacto(1, ContactStatusEnum.ASSIGNED));
    }

    @Test
    public void GivenIdAndContactabilityRqUpdateDetails() {
        when(contactabilityRepo.findById(anyInt())).thenReturn(Optional.of(contactability1));
        when(contactabilityRepo.save(any())).thenReturn(contactability1);
        ContactabilityRq contactabilityRqSample = new ContactabilityRq();
        try {
            service.updateContactDetails(1, contactabilityRqSample);
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndContactabilityRqThrowUpdateException() {
        when(contactabilityRepo.findById(anyInt())).thenReturn(Optional.of(contactability1));
        when(contactabilityRepo.save(any())).thenThrow(IllegalArgumentException.class);
        ContactabilityRq contactabilityRqSample = new ContactabilityRq();
        try {
            service.updateContactDetails(1, contactabilityRqSample);
            Assertions.assertThrows(UpdateException.class, () -> service.updateContactDetails(1, contactabilityRqSample));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndDescriptionThrowRegistryNotFoundException() {
        when(contactabilityRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        ContactabilityRq contactabilityRqSample = new ContactabilityRq();
        try {
            service.updateContactDetails(1, contactabilityRqSample);
            Assertions.assertThrows(RegistryNotFoundException.class, () -> service.updateContactDetails(1, contactabilityRqSample));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(ContactabilityRegistrationServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
