package pl.setblack.exp.galakpizza.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_ORDER")
public class Order {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    long id;

    public final String planet;
    public final Variant variant;
    public final Size size;

    protected Order() {
        this("xxx", null, null);
    }


    public Order(String planet, Variant variant, Size size) {
        this.planet = planet;
        this.variant = variant;
        this.size = size;
    }

    public long getId() {
        return id;
    }

}
