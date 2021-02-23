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

    List<Campaign> findByNameLikeIgnoreCaseOrderByNameAsc(String name);

    List<Campaign> findByStartDateBetween(@DateTimeFormat(pattern = "yyyy-MM-dd") @Param("from") Date from, @DateTimeFormat(pattern = "yyyy-MM-dd") @Param("to") Date to);

    List<Campaign> findByEndDateBetween(@DateTimeFormat(pattern = "yyyy-MM-dd") @Param("from") Date from, @DateTimeFormat(pattern = "yyyy-MM-dd") @Param("to") Date to);
    
    List<Campaign> findByRegion(String region);
    
    List<Campaign> findByNumberClientsInProgressEquals(Integer numberClientsInProgress);
}
