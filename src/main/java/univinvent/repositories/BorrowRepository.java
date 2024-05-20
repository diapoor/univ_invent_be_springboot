package univinvent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import univinvent.entities.Borrow;
import univinvent.entities.Inventory;
import univinvent.entities.Maintenance;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow,Integer> {
    public Borrow getBorrowByBorrowingId(int id);
    @Query("SELECT b FROM Borrow b JOIN Inventory i ON b.itemId = i.itemId WHERE b.borrowerName like :keyword OR i.itemName like :keyword")
    public List<Borrow> getBorrowByKeyword(@Param("keyword")String keyword);
    @Query("SELECT b FROM Borrow b  WHERE b.status = :status")
    public List<Borrow> getBorrowByStatus(@Param("status") Borrow.Status status);

}
