package univinvent.controllers;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import univinvent.entities.Inventory;
import univinvent.services.InventoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list")
    public List<Inventory> getAll() {
        return inventoryService.getAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable("id") int id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }
    //link url image
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage (@PathVariable("id") int id) {
        try {
            String image = inventoryService.getImage(id);
            Path filePath = Path.of(image);
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));
            MediaType mediaType;
            if (image.toLowerCase().endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
            else mediaType = MediaType.IMAGE_JPEG;
            return ResponseEntity.ok().contentType(mediaType).body(resource);
        } catch (NullPointerException e) {
            return ResponseEntity.status(404).body("null");
        } catch (IOException e) {
            return ResponseEntity.status(404).body("null ");
        }
    }
    @GetMapping("/search/{keyword}")
    public List<Inventory> searchInventory(@PathVariable("keyword")String keyword) {
        return inventoryService.getInventoriesByNameOrDescription(keyword);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addInventory(@RequestPart("image")MultipartFile image,@ModelAttribute Inventory inventory){
        try {
            return ResponseEntity.ok(inventoryService.addInventory(inventory,image));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestPart("image")MultipartFile image,@ModelAttribute Inventory inventory){
        try {
            return ResponseEntity.ok(inventoryService.updateInventory(inventory,image));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")int id) {
        inventoryService.delete(id);
        return ResponseEntity.ok("Delete Successfully");

    }

}
