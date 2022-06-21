package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

//원래는 spring, db안 엮고 딱 그 메소드만 테스트 하는 것이 좋으나 지금은 jpa까지 잘 보여줘야하기떄문에 다 같이 보이게 어노테이션 붙인것
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        //given - 1. member를 만들고 item을 생성해서 주문을 넣어본다. 그 생성된 주문이 잘 들어갔는지(equal로 확인)
        Member member = createMember();
        Book book = createBook("jpa1", 10000, 10);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.",1, getOrder.getOrderItems().size());   //order 의 orderItem의 갯수를 확인한다.
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야한다.", 8, book.getStockQuantity());
    }


    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("jpa1", 10000, 10);
        
        int orderCount = 11;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발행되어야 한다."); //혹시모르고 Exception이 안날수있기때문에 꼭 fail을 써줘야한다.
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 jpa", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);   //order가 들어감

        //when - 실제 내가 테스트 하고 싶은 것
       orderService.cancleOrder(orderId);

        //then
       Order getOrder = orderRepository.findOne(orderId);

       assertEquals("주소 취소시 상태는 CALCLE이다.", OrderStatus.CANCLE, getOrder.getStatus());
       assertEquals("주문 취소 된 상품은 그 만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {    //option+command+p : 파라미터를 받게 만들수있다.
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("jia");
        member.setAddress(new Address("서울", "구로구", "123-234"));
        em.persist(member);
        return member;
    }



}