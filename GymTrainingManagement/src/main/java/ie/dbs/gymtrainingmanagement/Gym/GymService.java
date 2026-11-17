package ie.dbs.gymtrainingmanagement.Gym;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GymService {
    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @Transactional
    public Gym createGym(GymForm form) {
        Gym gym = new Gym(
                form.getName().trim(),
                blankToNull(form.getPhone()),
                buildAddress(form));
        return gymRepository.save(gym);
    }

    @Transactional(readOnly = true)
    public List<Gym> getGyms() {
        return gymRepository.findAll(Sort.by("name"));
    }

    @Transactional(readOnly = true)
    public Gym getGymById(Long id) {
        return gymRepository.findById(id).orElse(null);
    }

    @Transactional
    public Gym updateGym(Long id, GymForm form) {
        Gym gym = gymRepository.findById(id).orElse(null);
        if (gym == null) return null;

        gym.setName(form.getName().trim());
        gym.setPhone(blankToNull(form.getPhone()));
        gym.setAddress(buildAddress(form));
        return gymRepository.save(gym);
    }

    @Transactional
    public boolean deleteGym(Long id) {
        if (!gymRepository.existsById(id)) return false;
        gymRepository.deleteById(id);
        return true;
    }

    private Address buildAddress(GymForm form) {
        String eircode = blankToNull(form.getEircode());
        return new Address(
                form.getStreet().trim(),
                form.getCity().trim(),
                form.getCounty().trim(),
                eircode == null ? null : eircode.toUpperCase());
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
