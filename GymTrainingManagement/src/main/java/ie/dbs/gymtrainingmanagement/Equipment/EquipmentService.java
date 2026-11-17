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
    public Equipment createEquipment(EquipmentForm form) {
        // Throws InvalidImageException before anything is saved if the file is not valid.
        String imagePath = imageStorageService.Store(form.getImage());

        Equipment equipment = new Equipment(
                form.getName().trim(),
                blankToNull(form.getDescription()),
                form.getMuscleGroup(),
                imagePath);
        return equipmentRepository.save(equipment);
    }

    @Transactional(readOnly = true)
    public List<Equipment> getEquipment() {
        return equipmentRepository.findAll(Sort.by("name"));
    }

    @Transactional(readOnly = true)
    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElse(null);
    }

    @Transactional
    public Equipment updateEquipment(Long id, EquipmentForm form) {
        Equipment equipment = equipmentRepository.findById(id).orElse(null);
        if (equipment == null) return null;

        String newImage = imageStorageService.Store(form.getImage());

        equipment.setName(form.getName().trim());
        equipment.setDescription(blankToNull(form.getDescription()));
        equipment.setMuscleGroup(form.getMuscleGroup());

        if (newImage != null) {
            imageStorageService.Delete(equipment.getImagePath());
            equipment.setImagePath(newImage);
        }
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public boolean deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id).orElse(null);
        if (equipment == null) return false;

        equipmentRepository.delete(equipment);
        // Forces the delete now: if the equipment is still used by a gym, the exception happens here,
        // before the image file is removed.
        equipmentRepository.flush();
        imageStorageService.Delete(equipment.getImagePath());
        return true;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
