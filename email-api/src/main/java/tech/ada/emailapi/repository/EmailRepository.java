package tech.ada.emailapi.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import tech.ada.emailapi.model.Email;

public interface EmailRepository extends ReactiveCrudRepository<Email, String> {
}
