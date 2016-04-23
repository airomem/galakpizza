package pl.setblack.exp.galakpizza.api;

import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

public interface GalakPizzaOrders {
    long placeOrder(String planet, Variant variant, Size size);
}
