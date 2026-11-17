package ie.dbs.gymtrainingmanagement.Equipment;

import ie.dbs.gymtrainingmanagement.Storage.ImageStorageService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final ImageStorageService imageStorageService;

    public EquipmentService(EquipmentRepository equipmentRepository,
                            ImageStorageService imageStorageService) {
        this.equipmentRepository = equipmentRepository;
        this.imageStorageService = imageStorageService;
    }

    @Transactional
    public Equipment CreateEquipment(EquipmentForm form) {
        String imagePath = imageStorageService.Store(form.getImage());

        Equipment equipment = new Equipment(
                form.getName().trim(),
                BlankToNull(form.getDescription()),
                form.getMuscleGroup(),
                imagePath);
        return equipmentRepository.save(equipment);
    }

    @Transactional(readOnly = true)
    public List<Equipment> GetEquipment() {
        return equipmentRepository.findAll(Sort.by("name"));
    }

    @Transactional(readOnly = true)
    public Equipment GetEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElse(null);
    }

    @Transactional
    public Equipment UpdateEquipment(Long id, EquipmentForm form) {
        Equipment equipment = equipmentRepository.findById(id).orElse(null);
        if (equipment == null) return null;

        String newImage = imageStorageService.Store(form.getImage());

        equipment.setName(form.getName().trim());
        equipment.setDescription(BlankToNull(form.getDescription()));
        equipment.setMuscleGroup(form.getMuscleGroup());

        if (newImage != null) {
            imageStorageService.Delete(equipment.getImagePath());
            equipment.setImagePath(newImage);
        }
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public boolean DeleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id).orElse(null);
        if (equipment == null) return false;

        equipmentRepository.delete(equipment);
        imageStorageService.Delete(equipment.getImagePath());
        return true;
    }

    private String BlankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
