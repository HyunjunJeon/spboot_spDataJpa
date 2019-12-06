package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    MemberRepository memberRepository;

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
}