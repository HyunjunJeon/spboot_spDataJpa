package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @ToString(of = {"id", "username", "age"})
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null){
            changeTeam(team);
        }
    }

    /* 연관관계 메서드 */
    public void changeTeam(Team team){
        // 기존 팀에서 지우고
        // this.team.getMembers().remove(this);
        // 새로운 팀을 설정하고
        this.team = team;
        this.team.getMembers().add(this);
    }
}
