package jpabook.jpashop.domain;


import jpabook.jpashop.domain.item.Delivery;
import jpabook.jpashop.domain.item.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")   //fk 가 member_id가 된다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;  //1:1이니까 둘 중 많이 쓰는 거에 fk 넣는다. order가 주인

    private LocalDateTime orderDate;    //java 제공 기본, 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);

    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메소드==// 셍성하는 지점에서 무언가를 수정하고자 할때 여기에서만 하면 되기떄문에 용이하다.
    // 생성이 복잡하면 도메인에 아예 생성메소드를 만드는게 좋다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);  //얘는 array니까 add한다.
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }


    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancle() {
        //  1. 만약 이미 배송된 제품 - 취소 불가
        if (delivery.getStatus() == DeliveryStatus.COMP) {  //만약 이미 배송된 상태라면 예외가 뜨고
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가합니다.");
        }

        // 2. 아니라면 - 상품 상태를 CALCLE로 바꾸고 재고를 되돌린다.
        this.setStatus(OrderStatus.CANCLE); //아니라면 cancle이 뜬다.
        for(OrderItem orderItem : orderItems) { //this.orderItems 인데 굳이 this 사용하지 않아도 된다.
            orderItem.cancle(); //orderItem에도 cancle을 넣어줘야 한다.
        }
    }

    //==조회로직==//
    /**
     * 전체 가격 주문 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();    //여기서 계산하는게 아니라 값은 orderItem에 계산식을 넣어두고 여기서는 가져만 온다. -> 약할 분담 확실히
        }
        return totalPrice;
    }

}
