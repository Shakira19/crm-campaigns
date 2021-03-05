/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.repository;

import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cofre
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {

    List<Campaign> findAll();

    Optional<Campaign> findById(Integer id);

    List<Campaign> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<Campaign> findByStartDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @Param("from") Date startDate);

    List<Campaign> findByEndDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @Param("from") Date endDate);

    List<Campaign> findByRegion(String region);

    List<Campaign> findByNumberClientsInProgressEquals(Integer numberClientsInProgress);

    List<Campaign> findByNumberClientsInProgressLessThan(Integer numberClientsInProgress);

    List<Campaign> findByNumberClientsInProgressLessThanEqual(Integer numberClientsInProgress);

    List<Campaign> findByNumberClientsInProgressGreaterThan(Integer numberClientsInProgress);

    List<Campaign> findByNumberClientsInProgressGreaterThanEqual(Integer numberClientsInProgress);

    List<Campaign> findByNumberAcceptedClientsEquals(Integer numberAcceptedClients);

    List<Campaign> findByNumberAcceptedClientsLessThan(Integer numberAcceptedClients);

    List<Campaign> findByNumberAcceptedClientsLessThanEqual(Integer numberAcceptedClients);

    List<Campaign> findByNumberAcceptedClientsGreaterThan(Integer numberAcceptedClients);

    List<Campaign> findByNumberAcceptedClientsGreaterThanEqual(Integer numberAcceptedClients);

    List<Campaign> findByNumberAssignedClientsEquals(Integer numberAssignedClients);

    List<Campaign> findByNumberAssignedClientsLessThan(Integer numberAssignedClients);

    List<Campaign> findByNumberAssignedClientsLessThanEqual(Integer numberAssignedClients);

    List<Campaign> findByNumberAssignedClientsGreaterThan(Integer numberAssignedClients);

    List<Campaign> findByNumberAssignedClientsGreaterThanEqual(Integer numberAssignedClients);

    List<Campaign> findByNumberRejectedClientsEquals(Integer numberRejectedClients);

    List<Campaign> findByNumberRejectedClientsLessThan(Integer numberRejectedClients);

    List<Campaign> findByNumberRejectedClientsLessThanEqual(Integer numberRejectedClients);

    List<Campaign> findByNumberRejectedClientsGreaterThan(Integer numberRejectedClients);

    List<Campaign> findByNumberRejectedClientsGreaterThanEqual(Integer numberRejectedClients);

    List<Campaign> findByKindProduct(String kindProduct);

    List<Campaign> findByStatus(String status);
}
