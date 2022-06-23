package jpabook.jpashop.service;

import com.fasterxml.classmate.MemberResolver;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Delivery;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문을 받는다.
    /**
     * 주문
     */
    @Transactional //데이터에 변동이 있는 것이기 떄문에 어노테이션 따로 붙여준다.
    public Long order(Long memberId, Long itemId, int count) {  //id가 들어오는데 여기서 name을 꺼내야하니까 위에 memberRepository를 선언해줘야 한다. -> 코드를 제약하면서 짜는 게 좋다.

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());   //실제로는 배송 정보를 따로 받겠지만 여기서는 간단하게 그냥 member의 주소를 가져와서 address에 붙인다.

        //주문 상품 생성 - Order의 생성메소드   (1주문 1item으로 일부러 제약함-너무 복잡할까봐)
        OrderItem orderItem = OrderItem.creataeOrderItem(item, item.getPrice(), count); //생성로직을 중구난방으로 하면 유지보수가 어렵기 떄문에 한번 만든 생성메소드를 사용한다 -> 객체 생성해서 값을 넣는 걸 막기 위해 생성자를 protected로 선언해서 객체 생성 못하게 한다.

        //주문을 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);    //save를 한번했는데도 다 저장됨 -> 기존에 엔티티 설정에서 cascade = CascadeType.ALL로 선언했기떄문에 한번의 save만 해도 delivery와 order가 알아서 em을 통해 저장된다.
                                        //cascade를 사용할때, order-orderItem- delivery 와 같이 상속관계가 분명히고 라이프 사이클이 맞는 경우에만 사용한다.(다른 곳에서는 OrderItem과 delivery 참조 안함) 잘못 사용하면 데이터가 헬이 됨
        return order.getId();
    }

    // 주문을 취소한다.
    /**
     * 주문취소
     */
    @Transactional
    public void cancleOrder(Long orderId) {
        //파라미터 값으로 엔티티를 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancle(); //원래라면 직접 다 꺼내서 update 쿼리를 날려줘야 한다.
    }

    // 주문을 검색한다.
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
