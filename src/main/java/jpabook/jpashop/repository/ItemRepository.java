package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {   //persist해야 Id생김, Id가 없다 == 완전 새로운 객체
        if (item.getId() == null) {
            em.persist(item);
        } else {
            Item merge = em.merge(item); //merge: 받은 item으로 객체 item을 찾아서 모든 값들을 받은 값으로 변경한다. -> itemSevice의 update1 방법 코드와 동일하다.
            //받은 item은 그대로 비영속성 객체이고 merge된 객체가 영속성 엔티티 이다.
            // merge != item    -> 그 뒤에 또 활용하려면 merge를 이용해야한다. 단, merge를 하면 무조건 모든 필드들이 교체되기 때문에 조심해야한다. (값이 없으면 null로 넣는다,)
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
