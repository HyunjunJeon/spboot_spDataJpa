package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /*
        Entity에 NamedQuery 만들어놓은 것 호출 => (Entity.MethodName) 으로 먼저 찾으니까
        @Query(name = "namedquery~~~") 어노테이션은 생략해도됌
     */
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username); // setParameter 역할을 하는 @Param을 꼭 써줘야함

    // * 메서드에 JPQL을 직접 작성해버림(이름없는 NamedQuery 같은 느낌...) -> 메서드명은 아무거나 써도됌
    /*
        Named Query랑 똑같이 App Loading 시점에 JPQL 검사를 해줌
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findNandA(@Param("username") String username, @Param("age") int age);

    // * JPA의 Embedded 타입도 조회가 가능함
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // * DTO로 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // * 여러개를 조회하고 싶을 때, Collection을 지원함(SQL IN 구문)
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // * Spring Data JPA는 반환타입이 유연함
    List<Member> findListByUsername(String username); // Collection
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalMemberByUsername(String username); // Optional 단건

    // Spring Data JPA Paging
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") // 조인이 걸려있는 상황에서 카운트를 구하고 싶으면 따로 카운트 쿼리만 작성해서 성능 향상
    Page<Member> findByAge(int age, Pageable pageable); // 일반적인 Paging
    List<Member> findTop3ByAge(int age);
//    Slice<Member> findByAge(int age, Pageable pageable); // 모바일화면의 '더보기' 처럼 원하는 Paging 개수보다 +1 더 요청해서 가져옴
}
