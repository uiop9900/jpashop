package jpabook.jpashop;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOk", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOk", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.creataeOrderItem(book1, book1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.creataeOrderItem(book2, book2.getPrice(), 1);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOk", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOk", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.creataeOrderItem(book1, book1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.creataeOrderItem(book2, book2.getPrice(), 1);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }

}
