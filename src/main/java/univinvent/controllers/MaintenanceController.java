package univinvent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import univinvent.entities.Maintenance;

import univinvent.services.MaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {
    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping("/list")
    public List<Maintenance> getAll() {
        return maintenanceService.getAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMaintenanceById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(maintenanceService.getOne(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Maintenance>> getMaintenancesByKeyword(@PathVariable("keyword")String keyword) {
        return ResponseEntity.ok(maintenanceService.search(keyword));
    }
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Maintenance maintenance) {
        try {
            return ResponseEntity.ok(maintenanceService.add(maintenance));
        }catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Maintenance maintenance) {
        try {
            return ResponseEntity.ok(maintenanceService.update(maintenance));
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> update(@PathVariable("id")int id) {
        try {
            maintenanceService.delete(id);
            return ResponseEntity.ok("Delete Successfully");
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
