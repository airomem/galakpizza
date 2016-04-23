package pl.setblack.exp.galakpizza.system;

import org.junit.After;
import org.junit.Before;

public class GalakPizzaTest extends  GalakPizzaTestBase<GalakPizza>{
    @Before
    public void initGP() {
        gp = new GalakPizza();
    }

    @After
    public void close() {
        gp.close();
    }

}
