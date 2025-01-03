package de.thi.notificationservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<Notification> {

}
