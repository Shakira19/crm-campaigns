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

    List<Campaign> findByStartDate(Date date);

    List<Campaign> findByEndDate(Date date);
    
    List<Campaign> findByStartDateBetween(Date from, Date to);

    List<Campaign> findByEndDateBetween(Date from, Date to);

    List<Campaign> findByStartDateAndEndDate(Date startDate, Date endDate);

}
