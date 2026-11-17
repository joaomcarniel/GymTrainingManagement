package ie.dbs.gymtrainingmanagement.Exercise;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Equipment.Equipment;
import ie.dbs.gymtrainingmanagement.Equipment.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final EquipmentRepository equipmentRepository;

    public ExerciseService(ExerciseRepository exerciseRepository,
                           EquipmentRepository equipmentRepository) {
        this.exerciseRepository = exerciseRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    public Exercise createExercise(ExerciseForm form) {
        Equipment equipment = findEquipment(form.getEquipmentId());

        String name = form.getName().trim();
        if (exerciseRepository.existsByNameIgnoreCase(name)) {
            throw nameTaken();
        }

        Exercise exercise = new Exercise(
                name, blankToNull(form.getDescription()), form.getMuscleGroup(), equipment);
        return exerciseRepository.save(exercise);
    }

    @Transactional(readOnly = true)
    public List<Exercise> getExercises() {
        return exerciseRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    @Transactional
    public Exercise updateExercise(Long id, ExerciseForm form) {
        Exercise exercise = exerciseRepository.findById(id).orElse(null);
        if (exercise == null) return null;

        Equipment equipment = findEquipment(form.getEquipmentId());

        String name = form.getName().trim();
        if (exerciseRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw nameTaken();
        }

        exercise.setName(name);
        exercise.setDescription(blankToNull(form.getDescription()));
        exercise.setMuscleGroup(form.getMuscleGroup());
        exercise.setEquipment(equipment);
        return exerciseRepository.save(exercise);
    }

    @Transactional
    public boolean deleteExercise(Long id) {
        if (!exerciseRepository.existsById(id)) return false;
        exerciseRepository.deleteById(id);
        // Forces the delete now, so a foreign key error (exercise used in a plan)
        // is thrown here and the controller can show a message.
        exerciseRepository.flush();
        return true;
    }

    // equipmentId is optional, so null means "no equipment".
    private Equipment findEquipment(Long equipmentId) {
        if (equipmentId == null) return null;
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new FieldValidationException(
                        "equipmentId", "The selected equipment does not exist"));
    }

    private FieldValidationException nameTaken() {
        return new FieldValidationException("name", "An exercise with this name already exists");
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
