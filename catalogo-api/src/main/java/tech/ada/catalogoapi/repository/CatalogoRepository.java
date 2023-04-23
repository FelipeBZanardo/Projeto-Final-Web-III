package tech.ada.catalogoapi.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import tech.ada.catalogoapi.model.Produto;

public interface CatalogoRepository extends ReactiveCrudRepository<Produto, String> {
}
