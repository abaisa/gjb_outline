import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.service.DependencyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class DbTest{
    @Autowired
    private DependencyService dependencyService;

    @Test
    public void testManageSysDevelop() {
        System.out.println(">> test start");
        ManageSysDevelop res =  dependencyService.getSysDevelopModelByDevItemId("441c98a4b7f8460ea6dc6d05bfde9a13");
        System.out.println(">> " + res.getDevName());
    }
}
