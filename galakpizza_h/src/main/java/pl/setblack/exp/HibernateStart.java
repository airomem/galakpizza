package pl.setblack.exp;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.List;

public class HibernateStart {
     private SessionFactory sessionFactory;
    public static void main(final String ... args) {
        final HibernateStart hib = new HibernateStart();
        hib.init();
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public SessionFactory init() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
             sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();

        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
            throw new RuntimeException(e);
        }
        return sessionFactory;
    }

    public void close() {
        this.sessionFactory.close();
        this.sessionFactory = null;
    }


}
