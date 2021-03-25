package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignRq;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
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

}
