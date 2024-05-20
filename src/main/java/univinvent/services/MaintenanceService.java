package univinvent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univinvent.entities.Borrow;
import univinvent.entities.Inventory;
import univinvent.entities.Maintenance;
import univinvent.repositories.InventoryRepository;
import univinvent.repositories.MaintenanceRepository;

import java.util.List;

@Service
public class MaintenanceService implements IService<Maintenance> {
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Override
    public List<Maintenance> getAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance getOne(int id) {
        Maintenance maintenance = maintenanceRepository.getMaintenanceByMaintenanceId(id);
        if(maintenance == null) throw new RuntimeException("Maintenance not found");
        return maintenance;
    }
    public List<Maintenance> search(String keyword) {
        try {
            Maintenance.Status status = Maintenance.Status.valueOf(keyword.toUpperCase());
            return maintenanceRepository.getMaintenanceByStatus(status);
        } catch (Exception e) {
            return maintenanceRepository.getMaintenanceByItemName(keyword);
        }

    }

    @Override
    public Maintenance add(Maintenance object) {
        Inventory inventory = inventoryRepository.getInventoriesByItemId(object.getItemId());
        if(inventory == null) // check item
            throw new RuntimeException("Item does not exist.");

        //kiểm tra số lượng sửa
        if (!object.getStatus().equals(Maintenance.Status.RESOLVED) &&
                (inventory.getTotalQuantity() - inventoryRepository.countItemInMaintenanceNot(inventory.getItemId(), Maintenance.Status.RESOLVED)
                        - inventoryRepository.countItemInBorrowNot(inventory.getItemId(), Borrow.Status.RETURNED)
                        - inventoryRepository.countSumItemInLossesAndDamaged(inventory.getItemId())) < 1)
            throw new RuntimeException( "The quantity exceeds the limit. Please check again.");
        
        //kiểm tra trạng thái sửa xong thì phải có sửa xong,và ngàysửa xong phải sau ngày sửa
        if(object.getStatus().equals(Maintenance.Status.RESOLVED))  {//status: resolved
            if(object.getResolvedDate() == null) throw  new RuntimeException("The Resolved status must have a date. Please try again."); //không có ngày xong
            if(object.getResolvedDate().before(object.getReportedDate())) throw new RuntimeException("The Resolve date must be after the reporting date."); //ngày xong trước ngày bắt đầu sửa
        } else { //status: in progress,pending
            if(object.getResolvedDate() != null) throw new RuntimeException("Resolve_date is only applicable to the resolved status."); //có ngày sửa xong
        }
        return maintenanceRepository.save(object);
    }

    @Override
    public Maintenance update(Maintenance object) {
        Maintenance maintenance = maintenanceRepository.getMaintenanceByMaintenanceId(object.getMaintenanceId());
        //kiểm tra maintenance có tồn tại không
        if(maintenance == null) throw new RuntimeException("Couldn't update because Maintenance not found.");
        //nếu đang là trạng thái sửa xong thì không được edit
        if(maintenance.getStatus().equals(Maintenance.Status.RESOLVED)) throw new RuntimeException("This inventory item has been Resolved and cannot be edited.");
        ///kiểm tra trạng thái sửa xong thì phải có sửa xong,và ngàysửa xong phải sau ngày sửa
        if(object.getStatus().equals(Maintenance.Status.RESOLVED))  {//status: resolved
            if(object.getResolvedDate() == null) throw  new RuntimeException("The Resolved status must have a date. Please try again."); //không có ngày xong
            if(object.getResolvedDate().before(object.getReportedDate())) throw new RuntimeException("The Resolve date must be after the reporting date."); //ngày xong trước ngày bắt đầu sửa
        } else { //status: in progress,pending
            if(object.getResolvedDate() != null) throw new RuntimeException("Resolve_date is only applicable to the resolved status."); //có ngày sửa xong
        }
        maintenance.setStatus(object.getStatus());
        maintenance.setIssueDescription(object.getIssueDescription());
        maintenance.setResolvedDate(object.getResolvedDate());
        return maintenanceRepository.save(maintenance);
    }
    @Override
    public void delete(int id) {
        maintenanceRepository.deleteById(id);
    }
}
