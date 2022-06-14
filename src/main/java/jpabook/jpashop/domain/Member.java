package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    //member:order = 1:n -> order주인 mappedBy="" 안에는 어느 컬럼에 매핑되었나 컬럼명을 적어주는데
    // 이 경우, order클래스의 member컬럼에 매핑된것이기에 member를 기재한다.
    //member필드에 의해서 매핑됨 -> 그냥 거울임
}
