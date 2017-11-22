package pl.setblack.exp.galakpizza.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;

@Entity
@Table(name="T_ORDER",
        indexes = {@Index(name="planet_idx", columnList = "planet", unique = false)})
@NamedQueries( {
        @NamedQuery(name = "select best planet", query = "SELECT o.planet, count(o)  FROM Order o " +
                " GROUP BY o.planet ORDER BY count(o) desc"),
        @NamedQuery(name = "select orders", query = "SELECT o FROM Order o WHERE o.planet = :planet"),
        @NamedQuery(name = "count orders", query = "SELECT count(o)  FROM Order o ")

})
public class Order {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    final long id;

    public final String planet;
    public final Variant variant;
    public final Size size;

    protected Order() {
        this("this is strange...", null, null);
    }

    public Order(String planet, Variant variant, Size size) {
        this.planet = planet;
        this.variant = variant;
        this.size = size;
        id = 0;
    }

    public long getId() {
        return id;
    }

}
