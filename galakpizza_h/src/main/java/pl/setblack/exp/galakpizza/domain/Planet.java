package pl.setblack.exp.galakpizza.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name="T_PLANET",
indexes = {@Index(name="cnt_idx", columnList = "count", unique = false)} )
public class Planet {
    @Id
    public final String name;

    private int count;

    protected Planet() {
        name = "default";
    }

    public Planet(String name) {
        this.name = name;
    }

    public int getCount() {
        return this.count;
    }

    public void increment() {
        this.count++;
    }

    public void clear() {
        this.count = 0;
    }
}
