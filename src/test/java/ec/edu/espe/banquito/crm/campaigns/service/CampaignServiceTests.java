package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.api.dto.ClientCampaignRq;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.NotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.UpdateException;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.repository.CampaignRepository;
import ec.edu.espe.banquito.crm.campaigns.repository.ContactabilityRegistrationRepository;
import java.sql.Date;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Alan Quimbita
 */
@ExtendWith(MockitoExtension.class)
public class CampaignServiceTests {

    @Mock
    private CampaignRepository campaignRepo;

    @Mock
    private ContactabilityRegistrationRepository contactabilityRegistrationRepo;

    @InjectMocks
    private CampaignService service;
    private Campaign campaignSample1;
    private Campaign campaignSample2;
    private List<Campaign> campaignList;

    @BeforeEach
    public void setUp() {
        this.campaignSample1 = new Campaign();
        this.campaignSample2 = new Campaign();
        this.campaignList = new ArrayList<>();
        this.campaignList.add(campaignSample1);
        this.campaignList.add(campaignSample2);
    }

    @Test
    public void GivenListCampaignsReturnListOfAllCampaigns() {
        when(campaignRepo.findAll()).thenReturn(campaignList);
        List<Campaign> campaignListTest = service.listarCampaigns();
        Assertions.assertEquals(campaignList, campaignListTest);
    }

    @Test
    public void GivenIdReturnCampaign() {
        when(campaignRepo.findById(1)).thenReturn(Optional.of(campaignSample1));
        try {
            Assertions.assertEquals(campaignSample1, service.getCampaignById(1));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdThatNotExistsThrowRegistryNotFoundException() {
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getCampaignById(2));
    }

    @Test
    public void GivenNameReturnListOfCampaigns() {
        campaignSample1.setName("New Campaign");
        campaignSample2.setName("Other Campaign");
        campaignList.add(1, campaignSample1);
        campaignList.add(2, campaignSample2);
        when(campaignRepo.findByNameContainingIgnoreCaseOrderByNameAsc("campaign")).thenReturn(campaignList);
        try {
            Assertions.assertEquals(campaignList, service.getCampaignByName("campaign"));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenNameAndCampaignsWereNotFoundThrowRegistryNotFoundException() {
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getCampaignByName("campaign"));
    }

    @Test
    public void GivenStartDateReturnListOfCampaigns() {
        campaignSample1.setStartDate(Date.valueOf("2021-01-01"));
        campaignSample2.setStartDate(Date.valueOf("2021-01-01"));
        campaignList.add(1, campaignSample1);
        campaignList.add(2, campaignSample2);
        when(campaignRepo.findByStartDate(Date.valueOf("2021-01-01"))).thenReturn(campaignList);
        try {
            Assertions.assertEquals(campaignList, service.getCampaignByStartDate(Date.valueOf("2021-01-01")));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenStartDateAndCampaignsWereNotFoundThrowRegistryNotFoundException() {
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getCampaignByStartDate(Date.valueOf("2021-01-01")));
    }

    @Test
    public void GivenEndDateReturnListOfCampaigns() {
        campaignSample1.setEndDate(Date.valueOf("2021-01-01"));
        campaignSample2.setEndDate(Date.valueOf("2021-01-01"));
        campaignList.add(1, campaignSample1);
        campaignList.add(2, campaignSample2);
        when(campaignRepo.findByEndDate(Date.valueOf("2021-01-01"))).thenReturn(campaignList);
        try {
            Assertions.assertEquals(campaignList, service.getCampaignByEndDate(Date.valueOf("2021-01-01")));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenEndDateAndCampaignsWereNotFoundThrowRegistryNotFoundException() {
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.getCampaignByEndDate(Date.valueOf("2021-01-01")));
    }

    @Test
    public void GivenCampaignReturnCreatedCampaign() {
        campaignSample1 = Campaign.builder()
                .name("New Campaign")
                .description("Campaign decription")
                .startDate(Date.valueOf("2021-01-01"))
                .endDate(Date.valueOf("2021-02-01"))
                .urlTermsConditions("https://banco-banquito.com/campaign-url")
                .kindProduct("PRE")
                .build();
        try {
            Campaign newCampaignTest = service.createCampaign(campaignSample1);
            Assertions.assertEquals(campaignSample1, newCampaignTest);
        } catch (InsertException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenCampaignInCreationThrowInsertException() {
        when(campaignRepo.save(any())).thenThrow(new IllegalArgumentException());
        Assertions.assertThrows(InsertException.class, () -> service.createCampaign(campaignSample1));

    }

    @Test
    public void GivenCampaignAndIdUpdateCampaign() {
        when(campaignRepo.findById(1)).thenReturn(Optional.of(campaignSample1));
        try {
            service.editCampaign(campaignSample1, 1);
        } catch (RegistryNotFoundException | UpdateException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenCampaignAndIdAndCampaignWasNotFoundThrowRegistryNotFoundException() {
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.editCampaign(campaignSample1, 1));
    }

    @Test
    public void GivenCampaignAndIdAndProducedErrorUpdatingCampaignThrowUpdateException() {
        when(campaignRepo.findById(1)).thenReturn(Optional.of(campaignSample1));
        when(campaignRepo.save(any())).thenThrow(new IllegalArgumentException());
        Assertions.assertThrows(UpdateException.class, () -> service.editCampaign(campaignSample1, 1));
    }

    @Test
    public void GivenIdAndNewStatusUpdateCampaign() {
        when(campaignRepo.findById(1)).thenReturn(Optional.of(campaignSample1));
        when(campaignRepo.save(any())).thenReturn(campaignSample1);
        try {
            service.updateCampaignStatus(1, "ACT");
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndNewStatusInUpdateStatusThrowRegistryNotFoundException() {
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.updateCampaignStatus(1, "ACT"));
    }

    @Test
    public void GivenIdAndNewStatusInUpdateStatusThrowUpdateException() {
        when(campaignRepo.findById(1)).thenReturn(Optional.of(campaignSample1));
        when(campaignRepo.save(any())).thenThrow(new IllegalArgumentException());
        Assertions.assertThrows(UpdateException.class, () -> service.updateCampaignStatus(1, "ACT"));
    }

    @Test
    public void GivenIdAndClientAssignClientToCampaign() {

        ContactabilityRegistration contactabilityRegistration = new ContactabilityRegistration();
        String clientIdentification = "0123456789";
        ClientCampaignRq clientCampaignRq = new ClientCampaignRq();
        clientCampaignRq.setClientIdentification(clientIdentification);
        campaignSample1.setId(1);
        campaignSample1.setTotalNumberClients(0);
        campaignSample1.setNumberAssignedClients(0);

        when(campaignRepo.findById(1)).thenReturn(Optional.of(campaignSample1));
        when(contactabilityRegistrationRepo
                .findByClientIdentificationAndCampaign(clientIdentification, campaignSample1))
                .thenReturn(null);
        when(contactabilityRegistrationRepo.save(any())).thenReturn(contactabilityRegistration);
        //when(campaignRepo.save(any())).thenReturn(campaignSample1);
        try {
            service.assignClient(1, clientCampaignRq);
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InsertException ex) {
            Logger.getLogger(CampaignServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndClientWhenAssignClientToCampaignContactabilityValidationFails() {
        ClientCampaignRq clientCampaignRq = new ClientCampaignRq();
        clientCampaignRq.setClientIdentification("0123456789");
        when(campaignRepo.findById(1)).thenReturn(Optional.of(campaignSample1));
        when(contactabilityRegistrationRepo
                .findByClientIdentificationAndCampaign("0123456789", campaignSample1))
                .thenReturn(new ContactabilityRegistration());
        Assertions.assertThrows(InsertException.class, () -> service.assignClient(1, clientCampaignRq));
    }

    @Test
    public void GivenIdAndClientWhenAssignClientToCampaignAndCampaignWasNotFoundThrowRegistryNotFoundException() {
        when(campaignRepo.findById(any())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(RegistryNotFoundException.class, () -> service.assignClient(1, new ClientCampaignRq()));
    }

    @Test
    public void GivenRegionReturnListOfCampaigns() {
        when(campaignRepo.findByRegion(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByRegion("");
        Assertions.assertEquals(campaignList, campaignsTest);

    }

    @Test
    public void GivenRegionThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByRegion(""));
    }

    @Test
    public void GivenNumberClientsInProgressEqualsReturnListOfCampaigns() {
        when(campaignRepo.findByNumberClientsInProgressEquals(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberClientsInProgressEquals(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberClientsInProgressThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberClientsInProgressEquals(1));
    }

    @Test
    public void GivenNumberClientsInProgressLessThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberClientsInProgressLessThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberClientsInProgressLessThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberClientsInProgressLessThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberClientsInProgressLessThan(1));
    }

    @Test
    public void GivenNumberClientsInProgressLessThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberClientsInProgressLessThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberClientsInProgressLessThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberClientsInProgressLessThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberClientsInProgressLessThanEqual(1));
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberClientsInProgressGreaterThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberClientsInProgressGreaterThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberClientsInProgressGreaterThan(1));
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberClientsInProgressGreaterThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberClientsInProgressGreaterThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberClientsInProgressGreaterThanEqual(1));
    }

    @Test
    public void GivenNumberAcceptedClientsEqualsReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAcceptedClientsEquals(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAcceptedClientsEquals(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAcceptedClientsEqualsThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAcceptedClientsEquals(1));
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAcceptedClientsLessThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAcceptedClientsLessThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAcceptedClientsLessThan(1));
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAcceptedClientsLessThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAcceptedClientsLessThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAcceptedClientsLessThanEqual(1));
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAcceptedClientsGreaterThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAcceptedClientsGreaterThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAcceptedClientsGreaterThan(1));
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAcceptedClientsGreaterThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1));
    }

    @Test
    public void GivenNumberAssignedClientsEqualsReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAssignedClientsEquals(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAssignedClientsEquals(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAssignedClientsEqualsThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAssignedClientsEquals(1));
    }

    @Test
    public void GivenNumberAssignedClientsLessThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAssignedClientsLessThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAssignedClientsLessThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAssignedClientsLessThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAssignedClientsLessThan(1));
    }

    @Test
    public void GivenNumberAssignedClientsLessThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAssignedClientsLessThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAssignedClientsLessThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAssignedClientsGreaterThanEqual(1));
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAssignedClientsGreaterThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAssignedClientsGreaterThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAssignedClientsGreaterThan(1));
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberAssignedClientsGreaterThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberAssignedClientsGreaterThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberAssignedClientsLessThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberAssignedClientsLessThanEqual(1));
    }

    //
    @Test
    public void GivenNumberRejectedClientsEqualsReturnListOfCampaigns() {
        when(campaignRepo.findByNumberRejectedClientsEquals(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberRejectedClientsEquals(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberRejectedClientsEqualsThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberRejectedClientsEquals(1));
    }

    @Test
    public void GivenNumberRejectedClientsLessThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberRejectedClientsLessThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberRejectedClientsLessThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberRejectedClientsLessThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberRejectedClientsLessThan(1));
    }

    @Test
    public void GivenNumberRejectedClientsLessThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberRejectedClientsLessThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberRejectedClientsLessThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberRejectedClientsGreaterThanEqual(1));
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanReturnListOfCampaigns() {
        when(campaignRepo.findByNumberRejectedClientsGreaterThan(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberRejectedClientsGreaterThan(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberRejectedClientsGreaterThan(1));
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanEqualReturnListOfCampaigns() {
        when(campaignRepo.findByNumberRejectedClientsGreaterThanEqual(any())).thenReturn(campaignList);
        List<Campaign> campaignsTest = service.getCampaignsByNumberRejectedClientsGreaterThanEqual(1);
        Assertions.assertEquals(campaignList, campaignsTest);
    }

    @Test
    public void GivenNumberRejectedClientsLessThanEqualThatDoesNotExistsThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByNumberRejectedClientsLessThanEqual(1));
    }

    @Test
    public void GivenKindProductReturnListOfCampaigns() {
        when(campaignRepo.findByKindProduct(any())).thenReturn(campaignList);
        Assertions.assertEquals(campaignList, service.getCampaignsByKindProduct(""));
    }

    @Test
    public void GivenKindProductAndCampaignsDoesNotExistsThrowNotFoundException() {
        when(campaignRepo.findByKindProduct(any())).thenReturn(new ArrayList<Campaign>());
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByKindProduct(""));
    }

    @Test
    public void GivenStatusReturnListOfCampaigns() {
        when(campaignRepo.findByStatus(any())).thenReturn(campaignList);
        Assertions.assertEquals(campaignList, service.getCampaignsByStatus(""));
    }

    @Test
    public void GivenStatusAndCampaignsDoesNotExistsThrowNotFoundException() {
        when(campaignRepo.findByStatus(any())).thenReturn(new ArrayList<Campaign>());
        Assertions.assertThrows(NotFoundException.class, () -> service.getCampaignsByStatus(""));
    }

}
