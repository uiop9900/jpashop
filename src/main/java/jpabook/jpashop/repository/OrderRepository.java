package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    public final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    //검색을 하면 그 유저의 orderList가 나옴 - 동적쿼리 (이름과 상태를 항상 다 입력받는것이 아니라 둘 중 하나만 받기도 하고 , 유저에 따라 유동적이다.
    public List<Order> findAll(OrderSearch orderSearch) {
        em.createQuery("select o from Order o join o.member m", Order.class);
    }

    /**
     * JPA Criteria: java코드로 jpql을 사용하게끔 만든다. -> 유지보수 0
     */

}
