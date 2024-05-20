package univinvent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univinvent.entities.WareHouse;
import univinvent.services.WareHouseService;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WareHouseController {
    @Autowired
    private WareHouseService wareHouseService;

    @GetMapping("/list")
    public List<WareHouse> getAllWareHouse() {
        return wareHouseService.getAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getDetailWareHouseById(@PathVariable("id")int id) {
        try {
            return ResponseEntity.ok(wareHouseService.detailWareHouseById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<WareHouse>> getDetailWareHouseById(@PathVariable("keyword")String keyword) {
        return ResponseEntity.ok(wareHouseService.getWarehouseByName(keyword));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addWareHouse(@RequestBody WareHouse wareHouse) {
        try {
            return ResponseEntity.ok().body(wareHouseService.add(wareHouse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> UpdateWareHouse(@RequestBody WareHouse wareHouse) {
        try {
            return ResponseEntity.ok().body(wareHouseService.update(wareHouse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")int id) {
        try {
            wareHouseService.delete(id);
            return ResponseEntity.ok("Delete Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
