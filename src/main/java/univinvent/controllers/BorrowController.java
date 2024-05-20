package univinvent.controllers;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import univinvent.entities.Borrow;
import univinvent.services.BorrowService;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;
    @GetMapping("/list")
    public List<Borrow> getAll() {
        return borrowService.getAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getBorrowById(@PathVariable("id") int id) {
        return ResponseEntity.ok(borrowService.getOne(id));
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Borrow>> getBorrowsByKeyword(@PathVariable("keyword")String keyword) {
        return ResponseEntity.ok(borrowService.getBorrowByKeyword(keyword));
    }
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Borrow borrow) {
        return ResponseEntity.ok(borrowService.add(borrow));
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Borrow borrow) {
        return ResponseEntity.ok(borrowService.update(borrow));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> update(@PathVariable("id")int id) {
        borrowService.delete(id);
        return ResponseEntity.ok("Delete Successfully");
    }
}
