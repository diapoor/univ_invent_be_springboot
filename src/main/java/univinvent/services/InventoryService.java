package univinvent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import univinvent.dto.InventoryDTO;
import univinvent.entities.Borrow;
import univinvent.entities.Inventory;
import univinvent.entities.LossesAndDamage;
import univinvent.entities.Maintenance;
import univinvent.repositories.InventoryRepository;
import univinvent.repositories.WareHouseRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService implements IService<Inventory> {
    @Value("${image.upload.dir}")
    private String imagePath;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private WareHouseRepository wareHouseRepository;

    @Override
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory getOne(int id) {
        return null;
    }
    public InventoryDTO getInventoryById(int id) {
        Inventory inventory = inventoryRepository.getInventoriesByItemId(id);
        if(inventory == null) throw new RuntimeException("Inventory not found");
        HashMap<String,Long> statusQuantity = new HashMap<>();
        if (inventory.getTotalQuantity() < 0) {
            throw new IllegalArgumentException("Invalid total quantity");
        } else {
            Long borrowed = inventoryRepository.countItemInBorrow(id, Borrow.Status.BORROWED);
            Long overDue = inventoryRepository.countItemInBorrow(id, Borrow.Status.OVERDUE);
            Long pending = inventoryRepository.countItemInMaintenance(id,Maintenance.Status.PENDING);
            Long inProgress = inventoryRepository.countItemInMaintenance(id, Maintenance.Status.IN_PROGRESS);
            Long lost = inventoryRepository.countItemInLossesAndDamage(id, LossesAndDamage.Type.LOST);
            Long damaged = inventoryRepository.countItemInLossesAndDamage(id, LossesAndDamage.Type.DAMAGED);
            Long available = (long) inventory.getTotalQuantity() - (borrowed + overDue + pending + inProgress + lost + damaged);
            statusQuantity.put("Borrowed",borrowed);
            statusQuantity.put("OverDue",overDue);
            statusQuantity.put("Pending",pending);
            statusQuantity.put("In Progress",inProgress);
            statusQuantity.put("Lost",lost);
            statusQuantity.put("damaged",damaged);
            statusQuantity.put("Available",available);
        }
        return InventoryDTO.builder().itemId(id)
                .itemName(inventory.getItemName())
                .description(inventory.getDescription())
                .imagePath(inventory.getImagePath())
                .statusQuantity(statusQuantity)
                .totalQuantity(inventory.getTotalQuantity())
                .warehouseId(inventory.getWarehouseId()).build();
    }

    public String getImage(int id) {
        Inventory inventory = inventoryRepository.getInventoriesByItemId(id);
        if(inventory == null) return  null;
        return  imagePath + "/" + inventory.getImagePath();
    }
    @Override
    public Inventory add(Inventory object) {

        return null;
    }
    public List<Inventory> getInventoriesByNameOrDescription(String keyword) {
        return inventoryRepository.getInventoriesByItemNameOrDescription(keyword);
    }

    public String addInventory(Inventory inventory, MultipartFile image) throws IOException {
        if(!wareHouseRepository.existsWareHouseByWarehouseId(inventory.getWarehouseId()))
            throw new RuntimeException("Warehouse does not exist.");
        Inventory inventoryData = inventoryRepository.getInventoriesByItemNameAndDescriptionAndWarehouseId(inventory.getItemName(),inventory.getDescription(),inventory.getWarehouseId());
        if(inventoryData != null ) { // kiểm tra nếu inventory đã tồn tại : tự động cộng số lượng
            inventoryData.setTotalQuantity(inventory.getTotalQuantity() + inventoryData.getTotalQuantity());
            inventoryRepository.save(inventoryData);
            return "This item already exists, the system has automatically accumulated the quantity";
        } else {
            if(image == null || image.isEmpty()) throw new IllegalArgumentException("No image data provided.");
            // Tạo tên file duy nhất
            String newImageName = UUID.randomUUID().toString() + "_" +
                    StringUtils.cleanPath(image.getOriginalFilename().replaceAll("\\s+", ""));
            try {
                // Lưu file vào thư mục lưu trữ
                Path uploadDir = Paths.get(imagePath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path filePath = uploadDir.resolve(newImageName);
                image.transferTo(filePath);

                // Lưu tên file vào dữ liệu
                inventory.setImagePath(newImageName);
                inventoryRepository.save(inventory);
                return "Add Inventory Successfully";
            } catch (IOException ex) {
                throw new IOException("Failed to store file: " + ex.getMessage());
            }
        }
    }

    @Override
    public Inventory update(Inventory object) {
        return null;
    }

    public String updateInventory(Inventory inventory, MultipartFile image) throws IOException {
        //query data
        Inventory inventoryData = inventoryRepository.getInventoriesByItemId(inventory.getItemId());
        //kiểm tra dữ liệu
        if(inventoryData == null)
            throw new NullPointerException("Inventory not found");
        //kiểm tra name và description: không hợp lệ: nếu đổi tên thì tên mới không được trùng với tên đã có
        if(!inventory.getItemName().equals(inventoryData.getItemName()) && !inventory.getDescription().equals(inventoryData.getDescription())) {
            if(inventoryRepository.existsInventoriesByItemNameAndDescription(inventory.getItemName(),inventory.getDescription()))
                throw new RuntimeException("Inventory already exist");
        }
        //check id warehouse
        if(!inventoryRepository.checkWarehouse(inventory.getWarehouseId()))
            throw  new RuntimeException("Ware house not found");
        //kiểm tra số lượng: tổng số lượng phải lớn hoặc bằng tổng số lượng mượn,trả,hỏng,mất
        if((inventory.getTotalQuantity() - inventoryRepository.countItemInBorrowNot(inventory.getItemId(), Borrow.Status.RETURNED)
                - inventoryRepository.countItemInMaintenanceNot(inventory.getItemId(), Maintenance.Status.RESOLVED)
                - inventoryRepository.countSumItemInLossesAndDamaged(inventory.getItemId())) < 0)
            throw new RuntimeException(
                    "The total quantity cannot be less than the sum of (lost + damaged + repaired + borrowed) quantities. Please check again.");

        if(image == null || image.isEmpty())
            throw new IllegalArgumentException("No image data provided.");
        // Tạo tên file duy nhất
        String newImageName = UUID.randomUUID().toString() + "_" +
                StringUtils.cleanPath(image.getOriginalFilename().replaceAll("\\s+", ""));
        try {
            // Lưu file vào thư mục lưu trữ
            Path uploadDir = Paths.get(imagePath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(newImageName);
            image.transferTo(filePath);

            // Xóa tệp ảnh cũ nếu có
            String oldImageName = inventoryData.getImagePath();
            if (oldImageName != null && !oldImageName.isEmpty()) {
                Path oldImagePath = uploadDir.resolve(oldImageName);
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                }
            }
            // Lưu tên file vào dữ liệu
            inventory.setImagePath(newImageName);
            inventoryRepository.save(inventory);
            return "Update Inventory Successfully";
        } catch (IOException ex) {
            throw new IOException("Failed to store file: " + ex.getMessage());
        }
    }
    @Override
    public void delete(int id) {
        //kiểm tra số lượng
        if((inventoryRepository.countItemInMaintenanceNot(id, Maintenance.Status.RESOLVED)
            + inventoryRepository.countItemInBorrowNot(id, Borrow.Status.RETURNED)
            + inventoryRepository.countSumItemInLossesAndDamaged(id))>0)
            throw new RuntimeException("You cannot delete the item because there are still items being repaired, borrowed, damaged, or lost.Please check again");
        inventoryRepository.deleteById(id);
    }

}
