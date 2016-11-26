package galakpizza;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;


public class GalakPizzaTest extends  GalakPizzaTestBase<GalakPizza>{
    @Before
    public void initGP() throws IOException {
        FileUtils.deleteDirectory(new File("pizzaStore"));
        gp = new GalakPizza();
    }

    @After
    public void close() throws IOException{
        gp.close();
        FileUtils.deleteDirectory(new File("pizzaStore"));
    }

}
