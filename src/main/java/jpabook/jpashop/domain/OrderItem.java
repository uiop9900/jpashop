package jpabook.jpashop.domain;


import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //생성자를 막아놓아서 객체 생성해서 값을 넣는 것을 방지한다.
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    //orderItem:item =
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)  //order:orderItem = 1:n
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; //주문 당시 가격
    private int count; //수량

    //==생성메소드==//
    public static OrderItem creataeOrderItem(Item item, int orderPrice, int count) {    //누가 봐도 어디서든 쓸거 같으면 static으로 선언해서 어디서든 다 사용이 가능하게 한다.
        //1. 추후 쿠폰이나 가격변동이 있을시 있기에 따로 빼줘서 set을 하는 게 맞다.
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        //2. order가 되면 재고가 빠진다.
        item.removeStock(count);
        return orderItem;

    }


    //== 비즈니스 로직 == //
    public void cancle() {  // 주문이 취소가 된다면
            //1. 재고를 원상태로 복귀
            getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {    //상품의 총 가격
        return getOrderPrice() * getCount();    //그냥 orderPrice와 count를 넣어도 되지만 JPA 프록시 객체가 생성될 수 있기때문에 최대한 get으로 사용한다.
    }
}
