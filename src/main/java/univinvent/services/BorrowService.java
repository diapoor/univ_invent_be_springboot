package univinvent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univinvent.entities.Borrow;
import univinvent.entities.Inventory;
import univinvent.entities.Maintenance;
import univinvent.repositories.BorrowRepository;
import univinvent.repositories.InventoryRepository;

import java.util.List;

@Service
public class BorrowService implements IService<Borrow> {
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Borrow> getAll() {
        return borrowRepository.findAll();
    }

    @Override
    public Borrow getOne(int id) {
        Borrow borrow = borrowRepository.getBorrowByBorrowingId(id);
        if(borrow == null) throw new RuntimeException("Borrow not found.");
        return borrow;
    }
    public List<Borrow> getBorrowByKeyword(String keyword) {
        try {
            Borrow.Status status = Borrow.Status.valueOf(keyword.toUpperCase());
            return borrowRepository.getBorrowByStatus(status);
        } catch (Exception e) {
            return borrowRepository.getBorrowByKeyword(keyword);
        }
    }

    @Override
    public Borrow add(Borrow object) {
        Inventory inventory = inventoryRepository.getInventoriesByItemId(object.getItemId());
        if(inventory == null) // check item
            throw new RuntimeException("Item does not exist.");

        //kiểm tra số lượng mượn
        if (!object.getStatus().equals(Borrow.Status.RETURNED) &&
                (inventory.getTotalQuantity() - inventoryRepository.countItemInMaintenanceNot(inventory.getItemId(), Maintenance.Status.RESOLVED)
                                              - inventoryRepository.countItemInBorrowNot(inventory.getItemId(), Borrow.Status.RETURNED)
                                              - inventoryRepository.countSumItemInLossesAndDamaged(inventory.getItemId())) < 1)
            throw new RuntimeException( "The quantity exceeds the limit. Please check again.");
        if(object.getBorrowedDate().after(object.getDueDate())) // kiểm tra ngày mượn < ngày trả dự kiến
            throw new RuntimeException("The due date cannot be before the borrowing date.Please try again.");
        //kiểm tra trạng thái đã trả thì phải có ngày,và ngày trả phải sau ngày mượn
        if(object.getStatus().equals(Borrow.Status.RETURNED) && (object.getReturnedDate() == null || object.getReturnedDate().before(object.getBorrowedDate())))
            throw new RuntimeException("The returned status must have a return date on or after the borrowing date. Please check again.");
        //kiểm tra nếu có ngày trả thì trạng thái phải là đã trả
        if(object.getReturnedDate() != null && !object.getStatus().equals(Borrow.Status.RETURNED))
            throw new RuntimeException("The item has a return date, please change its status to returned.");
        return borrowRepository.save(object);
    }

    @Override
    public Borrow update(Borrow object) {
        Borrow borrow = borrowRepository.getBorrowByBorrowingId(object.getBorrowingId());
        //kiểm tra borrow có tồn tại không
        if(borrow == null) throw new RuntimeException("Couldn't update because Borrow not found.");
        //nếu đang là trạng thái đã tra thì không được sửa
        if(borrow.getStatus().equals(Borrow.Status.RETURNED)) throw new RuntimeException("This inventory item has been returned and cannot be edited.");
        //nếu trng thái đã trả thì phải có ngày trả và ngày trả phải sau ngày mượn
        if(object.getStatus().equals(Borrow.Status.RETURNED) && (object.getReturnedDate() == null || object.getReturnedDate().before(object.getBorrowedDate())))
            throw new RuntimeException("The returned status must have a return date on or after the borrowing date. Please check again.");

        if(object.getReturnedDate() != null && !object.getStatus().equals(Borrow.Status.RETURNED))
            throw new RuntimeException("The item has a return date, please change its status to returned.");
        borrow.setStatus(object.getStatus());
        borrow.setBorrowedDate(object.getBorrowedDate());
        borrow.setDueDate(object.getDueDate());
        borrow.setReturnedDate(object.getReturnedDate());
        borrow.setBorrowerPhone(object.getBorrowerPhone());
        return borrowRepository.save(borrow);
    }

    @Override
    public void delete(int id) {
        borrowRepository.deleteById(id);
    }
}
