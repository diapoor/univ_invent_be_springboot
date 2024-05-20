package univinvent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univinvent.dto.WareHouseDTO;
import univinvent.entities.WareHouse;
import univinvent.repositories.WareHouseRepository;

import java.util.List;

@Service
public class WareHouseService implements IService<WareHouse> {
    @Autowired
    private WareHouseRepository wareHouseRepository;

    @Override
    public List<WareHouse> getAll() {
        return wareHouseRepository.findAll();
    }

    @Override
    public WareHouse getOne(int id) {
        return null;
    }

    @Override
    public WareHouse add(WareHouse object) {
        if(wareHouseRepository.existsWareHouseByNameAndLocation(object.getName(),object.getLocation()))
            throw new RuntimeException("This WareHouse already exist");
        return wareHouseRepository.save(object);
    }

    public List<WareHouse> getWarehouseByName(String name) {
        return wareHouseRepository.getWareHouseByName(name);
    }

    @Override
    public WareHouse update(WareHouse object) {
        wareHouseRepository.findById(object.getWarehouseId()).orElseThrow(()-> new RuntimeException("Ware house not found."));
        if(wareHouseRepository.existsWareHouseByNameAndLocation(object.getName(),object.getLocation()))
            throw new RuntimeException("This WareHouse already exist");
        return wareHouseRepository.save(object);
    }

    @Override
    public void delete(int id) {
        wareHouseRepository.findById(id).orElseThrow(()-> new RuntimeException("Ware house not found."));
        if(wareHouseRepository.getInventoryByWareHouseId(id).isEmpty() || wareHouseRepository.getSumInventoryInWareHouse(id) < 1 ) {
            wareHouseRepository.deleteById(id);
        }
        else throw new RuntimeException("You cannot delete the warehouse because there are still items in it.");
    }

    public WareHouseDTO detailWareHouseById(int id) {
        WareHouse wareHouse = wareHouseRepository.getWareHouseByWarehouseId(id);
        if(wareHouse == null) throw  new RuntimeException("Ware house not found.");
        return WareHouseDTO.builder().warehouseId(wareHouse.getWarehouseId())
                .name(wareHouse.getName())
                .location(wareHouse.getLocation())
                .inventories(wareHouseRepository.getInventoryByWareHouseId(id)).build();
    }
}
