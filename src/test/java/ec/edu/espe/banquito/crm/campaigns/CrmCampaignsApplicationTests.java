package ec.edu.espe.banquito.crm.campaigns;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class CrmCampaignsApplicationTests {

    @Test(expected = Test.None.class)
    void contextLoads() {
    }

}
