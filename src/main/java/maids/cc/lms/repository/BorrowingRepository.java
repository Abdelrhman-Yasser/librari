package maids.cc.lms.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import maids.cc.lms.model.BorrowingRecord;

public interface BorrowingRepository extends CrudRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookIdAndPatronId(long bookId, long patronId);
    Optional<BorrowingRecord> findByBookId(long bookId);
    Iterable<BorrowingRecord> findByPatronId(long patronId);
}
