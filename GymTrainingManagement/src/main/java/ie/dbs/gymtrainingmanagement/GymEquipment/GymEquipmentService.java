package ie.dbs.gymtrainingmanagement.GymEquipment;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Equipment.Equipment;
import ie.dbs.gymtrainingmanagement.Equipment.EquipmentRepository;
import ie.dbs.gymtrainingmanagement.Gym.Gym;
import ie.dbs.gymtrainingmanagement.Gym.GymRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GymEquipmentService {
    private final GymEquipmentRepository gymEquipmentRepository;
    private final GymRepository gymRepository;
    private final EquipmentRepository equipmentRepository;

    public GymEquipmentService(GymEquipmentRepository gymEquipmentRepository,
                               GymRepository gymRepository,
                               EquipmentRepository equipmentRepository) {
        this.gymEquipmentRepository = gymEquipmentRepository;
        this.gymRepository = gymRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    public GymEquipment createGymEquipment(GymEquipmentForm form) {
        Gym gym = gymRepository.findById(form.getGymId())
                .orElseThrow(() -> new FieldValidationException(
                        "gymId", "The selected gym does not exist"));
        Equipment equipment = equipmentRepository.findById(form.getEquipmentId())
                .orElseThrow(() -> new FieldValidationException(
                        "equipmentId", "The selected equipment does not exist"));

        if (gymEquipmentRepository.existsByGymIdAndEquipmentId(gym.getId(), equipment.getId())) {
            throw new FieldValidationException("equipmentId",
                    "This gym already has this equipment. Edit the existing record to change the quantity");
        }

        return gymEquipmentRepository.save(
                new GymEquipment(gym, equipment, form.getQuantity(), form.getCondition()));
    }

    /** All records, or only the ones of one gym when gymId is given. */
    @Transactional(readOnly = true)
    public List<GymEquipment> getGymEquipment(Long gymId) {
        return gymId == null
                ? gymEquipmentRepository.findAllWithDetails()
                : gymEquipmentRepository.findByGymIdWithDetails(gymId);
    }

    @Transactional(readOnly = true)
    public GymEquipment getGymEquipmentById(Long id) {
        return gymEquipmentRepository.findById(id).orElse(null);
    }

    // Only quantity and condition can change. To move equipment to another gym, delete and create again.
    @Transactional
    public GymEquipment updateGymEquipment(Long id, GymEquipmentForm form) {
        GymEquipment gymEquipment = gymEquipmentRepository.findById(id).orElse(null);
        if (gymEquipment == null) return null;

        gymEquipment.setQuantity(form.getQuantity());
        gymEquipment.setCondition(form.getCondition());
        return gymEquipmentRepository.save(gymEquipment);
    }

    @Transactional
    public boolean deleteGymEquipment(Long id) {
        if (!gymEquipmentRepository.existsById(id)) return false;
        gymEquipmentRepository.deleteById(id);
        return true;
    }
}
