package univinvent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univinvent.entities.LossesAndDamage;
import univinvent.entities.Maintenance;
import univinvent.services.LossesAndDamageService;
import univinvent.services.MaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/lad")
public class LADController {
    @Autowired
    private LossesAndDamageService lossesAndDamageService;
    @GetMapping("/list")
    public List<LossesAndDamage> getAll() {
        return lossesAndDamageService.getAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMaintenanceById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(lossesAndDamageService.getOne(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<LossesAndDamage>> getLADByKeyword(@PathVariable("keyword")String keyword) {
        return ResponseEntity.ok(lossesAndDamageService.search(keyword));
    }
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LossesAndDamage lossesAndDamage) {
        try {
            return ResponseEntity.ok(lossesAndDamageService.add(lossesAndDamage));
        }catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody LossesAndDamage lossesAndDamage) {
        try {
            return ResponseEntity.ok(lossesAndDamageService.update(lossesAndDamage));
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> update(@PathVariable("id")int id) {
        try {
            lossesAndDamageService.delete(id);
            return ResponseEntity.ok("Delete Successfully");
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
