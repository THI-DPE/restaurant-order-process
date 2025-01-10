package de.thi.orderservice.jpa.repositories;

import de.thi.orderservice.jpa.entities.Notification;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<Notification> {

}
