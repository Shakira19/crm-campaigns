package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignRq;
import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignStatusRq;
import ec.edu.espe.banquito.crm.campaigns.api.dto.ClientCampaignRq;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.NotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.UpdateException;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.service.CampaignService;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Alan Quimbita
 */
@ExtendWith(MockitoExtension.class)
public class CampaignControllerTests {

    @Mock
    private CampaignService service;
    private List<Campaign> campaignList;
    private Campaign campaignSample1;
    private Campaign campaignSample2;

    @InjectMocks
    private CampaignController controller;

    @BeforeEach
    public void setUp() {
        campaignSample1 = new Campaign();
        campaignSample2 = new Campaign();
        campaignList = new ArrayList<>();
        campaignList.add(campaignSample1);
        campaignList.add(campaignSample2);
    }

    @Test
    public void GivenGetCampaignsReturnResponseEntityOkWithListOfCampaigns() {
        when(service.listarCampaigns()).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList), controller.listarCampaigns());
    }

    @Test
    public void GivenGetCampaignsAndListIsEmptyReturnResponseEntityNotFound() {
        when(service.listarCampaigns()).thenReturn(new ArrayList<Campaign>());
        Assertions.assertEquals(ResponseEntity.notFound().build(), controller.listarCampaigns());
    }

    @Test
    public void GivenGetCampaignsAndErrorHappenReturnResponseEntityInternalServerError() {
        when(service.listarCampaigns()).thenReturn(null);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.listarCampaigns());
    }

    @Test
    public void GivenIdReturnResponseEntityWithCampaing() {
        try {
            when(service.getCampaignById(any())).thenReturn(campaignSample1);
            Assertions.assertEquals(ResponseEntity.ok(campaignSample1), controller.getCampaignById(1));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaignDoesNotExistsReturnResponseEntityBadRequest() {
        try {
            when(service.getCampaignById(any())).thenThrow(RegistryNotFoundException.class);
            Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.getCampaignById(1));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndErrorHappenReturnResponseEntityInternalServerError() {
        try {
            when(service.getCampaignById(any())).thenThrow(IllegalArgumentException.class);
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.getCampaignById(1));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenNameReturnResponseEntityOkWithCampaigns() {
        try {
            when(service.getCampaignByName(any())).thenReturn(campaignList);
            Assertions.assertEquals(ResponseEntity.ok(campaignList), controller.getCampaignByName(""));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenNameAndCampaignsWereNotFoundReturnResponseEntityNotFound() {
        try {
            when(service.getCampaignByName(any())).thenThrow(RegistryNotFoundException.class);
            Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getCampaignByName(""));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void GivenStartDateNullReturnResponseEntityBadRequest() {
        Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.getCampaignByStartDateBetween(null));
    }

    @Test
    public void GivenStartDateReturnResponseEntityWithListOfCampaigns() {
        try {
            when(service.getCampaignByStartDate(any())).thenReturn(campaignList);
            Assertions.assertEquals(ResponseEntity.ok(campaignList), controller.getCampaignByStartDateBetween(Date.valueOf("2021-01-01")));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenStartDateAndCampaignsWereNotFoundReturnResponseEntityNotFound() {
        try {
            when(service.getCampaignByStartDate(any())).thenThrow(RegistryNotFoundException.class);
            Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getCampaignByStartDateBetween(Date.valueOf("2021-01-01")));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void GivenEndDateNullReturnResponseEntityBadRequest() {
        Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.getCampaignByEndDateBetween(null));
    }

    @Test
    public void GivenEndDateReturnResponseEntityWithListOfCampaigns() {
        try {
            when(service.getCampaignByEndDate(any())).thenReturn(campaignList);
            Assertions.assertEquals(ResponseEntity.ok(campaignList), controller.getCampaignByEndDateBetween(Date.valueOf("2021-01-01")));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenEndDateAndCampaignsWereNotFoundReturnResponseEntityNotFound() {
        try {
            when(service.getCampaignByEndDate(any())).thenThrow(RegistryNotFoundException.class);
            Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getCampaignByEndDateBetween(Date.valueOf("2021-01-01")));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenCampaignRqCreateCampaignAndReturnResponseEntityOkWithCampaign() {
        try {
            when(service.createCampaign(any())).thenReturn(campaignSample1);
            Assertions.assertEquals(ResponseEntity.ok(campaignSample1), controller.createCampaign(new CampaignRq()));
        } catch (InsertException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void GivenCampaignRqAndThrowInsertExceptionReturnResponseEntityBadRequest() {
        try {
            when(service.createCampaign(any())).thenThrow(InsertException.class);
            Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.createCampaign(new CampaignRq()));
        } catch (InsertException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenCampaignRqAndThrowExceptionReturnResponseEntityInternalServerError() {
        try {
            when(service.createCampaign(any())).thenThrow(IllegalArgumentException.class);
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.createCampaign(new CampaignRq()));
        } catch (InsertException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaignEditCampaign() {
        try {
            doNothing().when(service).editCampaign(campaignSample1, 1);
            Assertions.assertEquals(ResponseEntity.ok().build(), controller.editCampaign(1, new CampaignRq()));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaignCampaignToEditReturnResponseEntityNotFound() {
        try {
            doThrow(RegistryNotFoundException.class).when(service).editCampaign(campaignSample1, 1);
            Assertions.assertEquals(ResponseEntity.notFound().build(), controller.editCampaign(1, new CampaignRq()));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaignCampaignToEditReturnResponseEntityIternalServerError() {
        try {
            doThrow(UpdateException.class).when(service).editCampaign(campaignSample1, 1);
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.editCampaign(1, new CampaignRq()));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaignStatusRqUpdateCampaignStatusActive() {
        CampaignStatusRq campaignStatusRq = new CampaignStatusRq();
        campaignStatusRq.setActive(true);

        try {
            doNothing().when(service).updateCampaignStatus(1, "ACT");
            Assertions.assertEquals(ResponseEntity.ok().build(), controller.updateCampaignStatus(1, campaignStatusRq));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaignStatusRqUpdateCampaignStatusSuspended() {
        CampaignStatusRq campaignStatusRq = new CampaignStatusRq();
        campaignStatusRq.setSuspended(true);

        try {
            doNothing().when(service).updateCampaignStatus(1, "SUS");
            Assertions.assertEquals(ResponseEntity.ok().build(), controller.updateCampaignStatus(1, campaignStatusRq));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaignStatusRqUpdateCampaignStatusTerminated() {
        CampaignStatusRq campaignStatusRq = new CampaignStatusRq();
        campaignStatusRq.setTerminated(true);

        try {
            doNothing().when(service).updateCampaignStatus(1, "TER");
            Assertions.assertEquals(ResponseEntity.ok().build(), controller.updateCampaignStatus(1, campaignStatusRq));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaStatusRqIsNullThrowResponseEntityBadRequest() {
        Assertions.assertEquals(ResponseEntity
                .badRequest()
                .body("No/Bad status defined in HTTP Request to update campaign statuts"),
                controller.updateCampaignStatus(1, new CampaignStatusRq()));
    }

    @Test
    public void GivenIdAndCampaStatusRqReturnResponseEntityNotFound() {
        try {
            CampaignStatusRq campaignStatusRq = new CampaignStatusRq();
            campaignStatusRq.setActive(true);

            doThrow(RegistryNotFoundException.class).when(service).updateCampaignStatus(1, "ACT");

            Assertions.assertEquals(ResponseEntity.notFound().build(), controller.updateCampaignStatus(1, campaignStatusRq));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndCampaStatusRqReturnResponseEntityInternalServerError() {
        try {
            CampaignStatusRq campaignStatusRq = new CampaignStatusRq();
            campaignStatusRq.setActive(true);

            doThrow(UpdateException.class).when(service).updateCampaignStatus(1, "ACT");

            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                    controller.updateCampaignStatus(1, campaignStatusRq));
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //HERE
    @Test
    public void GivenNumberClientsInProgressEqualsReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberClientsInProgressEquals(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberClientsInProgressEquals(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressEqualsReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberClientsInProgressEquals(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberClientsInProgressEquals(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressEqualsReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberClientsInProgressEquals(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberClientsInProgressEquals(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressLessThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberClientsInProgressLessThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberClientsInProgressLessThan(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressLessThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberClientsInProgressLessThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberClientsInProgressLessThan(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressLessThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberClientsInProgressLessThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberClientsInProgressLessThan(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressLessThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberClientsInProgressLessThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberClientsInProgressLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressLessThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberClientsInProgressLessThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberClientsInProgressLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressLessThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberClientsInProgressLessThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberClientsInProgressLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberClientsInProgressGreaterThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberClientsInProgressGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberClientsInProgressGreaterThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberClientsInProgressGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberClientsInProgressGreaterThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberClientsInProgressGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberClientsInProgressGreaterThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberClientsInProgressGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberClientsInProgressGreaterThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberClientsInProgressGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberClientsInProgressGreaterThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberClientsInProgressGreaterThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberClientsInProgressGreaterThanEqual(1)
        );
    }

    //HERE
    @Test
    public void GivenNumberAcceptedClientsEqualsReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAcceptedClientsEquals(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAcceptedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsEqualsReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAcceptedClientsEquals(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAcceptedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsEqualsReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAcceptedClientsEquals(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAcceptedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAcceptedClientsLessThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAcceptedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAcceptedClientsLessThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAcceptedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAcceptedClientsLessThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAcceptedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAcceptedClientsLessThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAcceptedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAcceptedClientsLessThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAcceptedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsLessThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAcceptedClientsLessThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAcceptedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAcceptedClientsGreaterThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAcceptedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAcceptedClientsGreaterThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAcceptedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAcceptedClientsGreaterThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAcceptedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAcceptedClientsGreaterThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAcceptedClientsGreaterThanEqual(1)
        );
    }

    //HERE
    @Test
    public void GivenNumberAssignedClientsEqualsReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAssignedClientsEquals(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAssignedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsEqualsReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAssignedClientsEquals(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAssignedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsEqualsReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAssignedClientsEquals(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAssignedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsLessThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAssignedClientsLessThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAssignedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsLessThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAssignedClientsLessThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAssignedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsLessThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAssignedClientsLessThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAssignedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsLessThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAssignedClientsLessThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAssignedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsLessThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAssignedClientsLessThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAssignedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsLessThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAssignedClientsLessThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAssignedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAssignedClientsGreaterThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAssignedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAssignedClientsGreaterThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAssignedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAssignedClientsGreaterThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAssignedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberAssignedClientsGreaterThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberAssignedClientsGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberAssignedClientsGreaterThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberAssignedClientsGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberAssignedClientsGreaterThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberAssignedClientsGreaterThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberAssignedClientsGreaterThanEqual(1)
        );
    }

    //HERE
    @Test
    public void GivenNumberRejectedClientsEqualsReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberRejectedClientsEquals(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberRejectedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsEqualsReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberRejectedClientsEquals(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberRejectedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsEqualsReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberRejectedClientsEquals(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberRejectedClientsEquals(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsLessThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberRejectedClientsLessThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberRejectedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsLessThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberRejectedClientsLessThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberRejectedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsLessThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberRejectedClientsLessThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberRejectedClientsLessThan(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsLessThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberRejectedClientsLessThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberRejectedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsLessThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberRejectedClientsLessThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberRejectedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsLessThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberRejectedClientsLessThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberRejectedClientsLessThanEqual(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberRejectedClientsGreaterThan(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberRejectedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberRejectedClientsGreaterThan(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberRejectedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberRejectedClientsGreaterThan(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberRejectedClientsGreaterThan(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanEqualReturnResponseEntityWithListOfCampaigns() {
        when(service.getCampaignsByNumberRejectedClientsGreaterThanEqual(1)).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList),
                controller.getCampaignsByNumberRejectedClientsGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanEqualReturnResponseEntityNotFound() {
        when(service.getCampaignsByNumberRejectedClientsGreaterThanEqual(1)).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(),
                controller.getCampaignsByNumberRejectedClientsGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenNumberRejectedClientsGreaterThanEqualReturnResponseEntityInternalSeverError() {
        when(service.getCampaignsByNumberRejectedClientsGreaterThanEqual(1)).thenThrow(IllegalArgumentException.class);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                controller.getCampaignsByNumberRejectedClientsGreaterThanEqual(1)
        );
    }

    @Test
    public void GivenIdAndClientCampaignRqAssignClientReturnResponseEntityOk() {
        try {
            ClientCampaignRq clientCampaignRq = new ClientCampaignRq();
            doNothing().when(service).assignClient(1, clientCampaignRq);
            Assertions.assertEquals(ResponseEntity.ok().build(),
                    controller.asignarCliente(1, clientCampaignRq)
            );
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InsertException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndClientCampaignRqAssignClientReturnResponseEntityBadRequest() {
        try {
            ClientCampaignRq clientCampaignRq = new ClientCampaignRq();
            doThrow(RegistryNotFoundException.class).when(service).assignClient(1, clientCampaignRq);
            Assertions.assertEquals(ResponseEntity.badRequest().build(),
                    controller.asignarCliente(1, clientCampaignRq)
            );
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InsertException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndClientCampaignRqAssignClientReturnResponseEntityBadRequest2() {
        try {
            ClientCampaignRq clientCampaignRq = new ClientCampaignRq();
            doThrow(InsertException.class).when(service).assignClient(1, clientCampaignRq);
            Assertions.assertEquals(ResponseEntity.badRequest().build(),
                    controller.asignarCliente(1, clientCampaignRq)
            );
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InsertException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenIdAndClientCampaignRqAssignClientReturnResponseEntityInternalServerError() {
        try {
            ClientCampaignRq clientCampaignRq = new ClientCampaignRq();
            doThrow(IllegalArgumentException.class).when(service).assignClient(1, clientCampaignRq);
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(),
                    controller.asignarCliente(1, clientCampaignRq)
            );
        } catch (RegistryNotFoundException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InsertException ex) {
            Logger.getLogger(CampaignControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void GivenKindProductReturnResponseEntityWithListCampaign() {
        when(service.getCampaignsByKindProduct("PRE")).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList), controller.getCampaignsByKindProduct("PRE"));
    }

    @Test
    public void GivenKindProductReturnResponseEntityNotFound() {
        when(service.getCampaignsByKindProduct("PRE")).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getCampaignsByKindProduct("PRE"));
    }

    @Test
    public void GivenStatusReturnResponseEntityWithListCampaign() {
        when(service.getCampaignsByStatus("ACT")).thenReturn(campaignList);
        Assertions.assertEquals(ResponseEntity.ok(campaignList), controller.getCampaignsByStatus("ACT"));
    }

    @Test
    public void GivenStatusReturnResponseEntityNotFound() {
        when(service.getCampaignsByStatus("ACT")).thenThrow(NotFoundException.class);
        Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getCampaignsByStatus("ACT"));
    }

}
