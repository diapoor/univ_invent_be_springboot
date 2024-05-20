package univinvent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univinvent.entities.Borrow;
import univinvent.entities.Inventory;
import univinvent.entities.LossesAndDamage;
import univinvent.entities.Maintenance;
import univinvent.repositories.InventoryRepository;
import univinvent.repositories.LossesAndDamagesRepository;

import java.util.Date;
import java.util.List;

@Service
public class LossesAndDamageService implements IService<LossesAndDamage>{
    @Autowired
    private LossesAndDamagesRepository lossesAndDamagesRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<LossesAndDamage> getAll() {
        return lossesAndDamagesRepository.findAll();
    }

    @Override
    public LossesAndDamage getOne(int id) {
        LossesAndDamage lossesAndDamage = lossesAndDamagesRepository.getLossesAndDamageByReportId(id);
        if(lossesAndDamage == null) throw new RuntimeException("Report not found");
        return lossesAndDamage;
    }

    public List<LossesAndDamage> search(String keyword) {
        try {
            LossesAndDamage.Type type = LossesAndDamage.Type.valueOf(keyword.toUpperCase());
            return lossesAndDamagesRepository.getLossesAndDamageByReportType(type);
        } catch (Exception e) {
            return lossesAndDamagesRepository.getLossesAndDamageByKeyword(keyword);
        }

    }

    @Override
    public LossesAndDamage add(LossesAndDamage object) {
        Inventory inventory = inventoryRepository.getInventoriesByItemId(object.getItemId());
        if(inventory == null) // check item
            throw new RuntimeException("Item does not exist.");

        //kiểm tra số lượng
        if (inventory.getTotalQuantity() - inventoryRepository.countItemInMaintenanceNot(inventory.getItemId(), Maintenance.Status.RESOLVED)
                        - inventoryRepository.countItemInBorrowNot(inventory.getItemId(), Borrow.Status.RETURNED)
                        - inventoryRepository.countSumItemInLossesAndDamaged(inventory.getItemId()) < 1)
            throw new RuntimeException( "The quantity exceeds the limit. Please check again.");
        if(object.getResolvedDate().after(new Date()))
            throw  new RuntimeException("The Resolved Date must be a past or present date.");
        return lossesAndDamagesRepository.save(object);
    }

    @Override
    public LossesAndDamage update(LossesAndDamage object) {
        LossesAndDamage lossesAndDamage = lossesAndDamagesRepository.getLossesAndDamageByReportId(object.getReportId());
        if(lossesAndDamage == null) throw new RuntimeException("Report not found.");
        if(object.getResolvedDate().after(new Date()))
            throw  new RuntimeException("The Resolved Date must be a past or present date.");
        lossesAndDamage.setDescription(object.getDescription());
        lossesAndDamage.setResolvedDate(object.getResolvedDate());
        lossesAndDamagesRepository.save(lossesAndDamage);
        return lossesAndDamage;
    }

    @Override
    public void delete(int id) {
        LossesAndDamage lossesAndDamage = lossesAndDamagesRepository.getLossesAndDamageByReportId(id);
        if(lossesAndDamage == null) throw new RuntimeException("Report not found.");
        lossesAndDamagesRepository.deleteById(id);
    }
}
