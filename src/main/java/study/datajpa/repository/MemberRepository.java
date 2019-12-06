package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /*
        Entity에 NamedQuery 만들어놓은 것 호출 => (Entity.MethodName) 으로 먼저 찾으니까
        @Query(name = "namedquery~~~") 어노테이션은 생략해도됌
     */
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username); // setParameter 역할을 하는 @Param을 꼭 써줘야함
}
