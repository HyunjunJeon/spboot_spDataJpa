package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    @Rollback(false)
    void saveAndFindTest_JpaRepository() {
        Member member = new Member("홍길동");
        Member saveMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @Rollback(false)
    void saveAndFindTest_SpringDataJpaRepository() {
        Member member = new Member("홍길동");
        Member saveMember = memberRepository.save(member);

        Optional<Member> optionalMember = memberRepository.findById(saveMember.getId());
        Member findMember = optionalMember.orElseThrow();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @Rollback(false)
    public void basicCRUD_JPA(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 1건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // List 조회
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트
        long count1 = memberJpaRepository.count();
        assertThat(count1).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member2);
        long count2 = memberJpaRepository.count();
        assertThat(count2).isEqualTo(1);
    }

    @Test
    @Rollback(false)
    public void basicCRUD_SpringDataJPA(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 1건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // List 조회
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트
        long count1 = memberRepository.count();
        assertThat(count1).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member2);
        long count2 = memberRepository.count();
        assertThat(count2).isEqualTo(1);
    }

    @Test
    @Rollback(false)
    public void findByUsernameAngAgeGreaterThen_JPA(){
        Member m1 = new Member("aa", 10);
        Member m2 = new Member("bb", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("aa", 9);

        assertThat(result.get(0).getUsername()).isEqualTo("aa");
        assertThat(result.get(0).getAge()).isEqualTo(10);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    @Rollback(false)
    public void findByUsernameAngAgeGreaterThen_SpringDataJPA(){
        Member m1 = new Member("aa", 10);
        Member m2 = new Member("bb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aa", 9);

        assertThat(result.get(0).getUsername()).isEqualTo("aa");
        assertThat(result.get(0).getAge()).isEqualTo(10);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    @Rollback(false)
    public void findByUsername_JPA_NamedQuery(){
        Member m1 = new Member("aa", 10);
        Member m2 = new Member("bb", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsername("aa");

        assertThat(result.get(0).getUsername()).isEqualTo("aa");

    }

    @Test
    @Rollback(false)
    public void findByUsername_SpringDataJPA_NamedQuery(){
        Member m1 = new Member("aa", 10);
        Member m2 = new Member("bb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("aa");

        assertThat(result.get(0).getUsername()).isEqualTo("aa");

    }

    @Test
    @Rollback(false)
    public void findNandA_SpringDataJPA_QueryAnnotation(){
        Member m1 = new Member("aa", 10);
        Member m2 = new Member("bb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findNandA("aa", 10);

        assertThat(result.get(0).getUsername()).isEqualTo("aa");
        assertThat(result.get(0).getAge()).isEqualTo(10);

    }

    @Test
    @Rollback(false)
    public void findMemberDto_SpringDataJPa(){
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("aa", 10);
        m1.changeTeam(teamA);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        assertThat(memberDto.get(0).getTeamname()).isEqualTo(teamA.getName());

    }

    // Collection을 넣어서 쿼리가 나가는 것 Test
    @Test
    public void findByNames_SpringDataJPA_Collection(){
        Member m1 = new Member("aa", 10);
        Member m2 = new Member("bb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("aa", "bb"));
        byNames.forEach(System.out::println);
    }

    @Test
    public void ManyReturnTypeTest_SpringDataJPA(){
        Member m1 = new Member("aa", 10);
        Member m2 = new Member("bb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aa = memberRepository.findListByUsername("aa");
        assertThat(aa.size()).isEqualTo(1);

        // 컬렉션 반환타입에서 없는걸 조회하면 Empty Collection을 반환함
        List<Member> asdasd = memberRepository.findListByUsername("asdasd");
        assertThat(asdasd.size()).isEqualTo(0);

        Member bb = memberRepository.findMemberByUsername("bb");
        assertThat(bb.getUsername()).isEqualTo("bb");

        // Entity 반환타입에서 없는걸 조회하면 Null 객체를 반환함
        Member asbba = memberRepository.findMemberByUsername("asbba");
        assertThat(asbba).isNull();

        // 위의 모든 것을 해결해 줄 Java 8의 Optional <<** 이걸 써야지 **>>
        Optional<Member> aa1 = memberRepository.findOptionalMemberByUsername("aa");
        assertThat(aa1).isNotEmpty();
    }

    @Test
    public void paging_JPA(){
        // Given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));
        memberJpaRepository.save(new Member("member7", 10));
        memberJpaRepository.save(new Member("member8", 10));
        memberJpaRepository.save(new Member("member9", 10));
        memberJpaRepository.save(new Member("member10", 10));
        memberJpaRepository.save(new Member("member11", 10));

        int age = 10;
        int offset = 5;
        int limit = 7;

        // When
        List<Member> members = memberJpaRepository.findByPaging(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // Paging 계산 공식을 적용해야함..논리가 복잡해짐!

        // Then
        assertThat(members.size()).isEqualTo(6); // 6 ~ 11번까지 6개 가져옴
        assertThat(totalCount).isEqualTo(11);
    }
}