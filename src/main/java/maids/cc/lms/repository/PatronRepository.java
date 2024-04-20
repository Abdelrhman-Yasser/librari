package maids.cc.lms.repository;

import org.springframework.data.repository.CrudRepository;

import maids.cc.lms.model.Patron;

public interface PatronRepository extends CrudRepository<Patron, Long> {}
