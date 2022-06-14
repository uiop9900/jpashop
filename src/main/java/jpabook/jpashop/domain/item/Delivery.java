package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter @Getter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;    //1:1일 경우 fk를 어디에 둬도 상관없다. 그렇기떄문에 더 믾이 쓰는 테이블에 fk를 넣는다.

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)    //enum타입은 @Enumerated 필수적으로 넣어주며 기본값이 ORDINAL(숫자)이기 변경해주자
    private DeliveryStatus status;
}
