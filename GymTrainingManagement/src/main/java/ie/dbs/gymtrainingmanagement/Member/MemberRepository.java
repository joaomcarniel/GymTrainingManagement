package ie.dbs.gymtrainingmanagement.Member;

import ie.dbs.gymtrainingmanagement.Login.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m join fetch m.gym left join fetch m.login order by m.fullName")
    List<Member> findAllWithDetails();

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // How many OTHER members already use this login (pass -1 when creating a new member).
    @Query("select count(m) from Member m where m.login = :login and m.id <> :memberId")
    long countOtherMembersUsingLogin(@Param("login") Login login, @Param("memberId") Long memberId);

    // Logins not linked to any member, plus the one of the member being edited.
    @Query("select l from Login l where not exists " +
            "(select m.id from Member m where m.login = l and m.id <> :memberId) " +
            "order by l.userName")
    List<Login> findAvailableLogins(@Param("memberId") Long memberId);
}
