package ie.dbs.gymtrainingmanagement.Member;

import ie.dbs.gymtrainingmanagement.Login.Login;
import ie.dbs.gymtrainingmanagement.Login.LoginRepository;
import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Gym.Gym;
import ie.dbs.gymtrainingmanagement.Gym.GymRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {
    // Used instead of null in queries, because no member has this id
    private static final long NO_MEMBER = -1L;

    private final MemberRepository memberRepository;
    private final GymRepository gymRepository;
    private final LoginRepository loginRepository;

    public MemberService(MemberRepository memberRepository,
                         GymRepository gymRepository,
                         LoginRepository loginRepository) {
        this.memberRepository = memberRepository;
        this.gymRepository = gymRepository;
        this.loginRepository = loginRepository;
    }

    @Transactional
    public Member createMember(MemberForm form) {
        Gym gym = findGym(form.getGymId());
        Login login = findLogin(form.getLoginId(), NO_MEMBER);

        String email = normalizeEmail(form.getEmail());
        if (memberRepository.existsByEmailIgnoreCase(email)) {
            throw emailTaken();
        }

        Member member = new Member(
                form.getFullName().trim(), email, blankToNull(form.getPhone()),
                form.getBirthDate(), form.isActive(), gym, login);
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<Member> getMembers() {
        return memberRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    /** Logins that can still be linked to a member. Pass the member being edited, or null when creating. */
    @Transactional(readOnly = true)
    public List<Login> getAvailableLogins(Long memberId) {
        return memberRepository.findAvailableLogins(memberId == null ? NO_MEMBER : memberId);
    }

    @Transactional
    public Member updateMember(Long id, MemberForm form) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member == null) return null;

        Gym gym = findGym(form.getGymId());
        Login login = findLogin(form.getLoginId(), id);

        String email = normalizeEmail(form.getEmail());
        if (memberRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw emailTaken();
        }

        member.setFullName(form.getFullName().trim());
        member.setEmail(email);
        member.setPhone(blankToNull(form.getPhone()));
        member.setBirthDate(form.getBirthDate());
        member.setActive(form.isActive());
        member.setGym(gym);
        member.setLogin(login);
        return memberRepository.save(member);
    }

    @Transactional
    public boolean deleteMember(Long id) {
        if (!memberRepository.existsById(id)) return false;
        memberRepository.deleteById(id);
        // Forces the delete now, so a foreign key error (member still has workout plans)
        // is thrown here and the controller can show a message.
        memberRepository.flush();
        return true;
    }

    private Gym findGym(Long gymId) {
        return gymRepository.findById(gymId)
                .orElseThrow(() -> new FieldValidationException(
                        "gymId", "The selected gym does not exist"));
    }

    // loginId is optional, so null means "no login".
    private Login findLogin(Long loginId, long currentMemberId) {
        if (loginId == null) return null;

        Login login = loginRepository.findById(loginId)
                .orElseThrow(() -> new FieldValidationException(
                        "loginId", "The selected login does not exist"));

        if (memberRepository.countOtherMembersUsingLogin(login, currentMemberId) > 0) {
            throw new FieldValidationException(
                    "loginId", "This login is already linked to another member");
        }
        return login;
    }

    private FieldValidationException emailTaken() {
        return new FieldValidationException("email", "Another member already uses this email");
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
