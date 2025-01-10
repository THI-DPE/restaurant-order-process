package de.thi.paymentservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReimbursementRepository implements PanacheRepository<Reimbursement> {

}
