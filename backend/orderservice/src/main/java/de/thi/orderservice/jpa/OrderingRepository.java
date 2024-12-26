package de.thi.orderservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderingRepository implements PanacheRepository<Ordering> {

}
