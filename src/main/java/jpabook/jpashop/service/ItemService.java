package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    /**
     * 준영속 엔티티를 update 하는 방법 1. 변경감지 (dirty checking)
     */
    @Transactional
    public Item updateItem(Long itemId, String name, int price, int stock) {
        //itemId로 item을 가지고 온다 -> 영속성 객체  -> 변경된 객체 값인 param의 값을 넣어준다
        // 이때 jpa가 관리하는 item을 가지고 온 것이기떄문에 별도의 update 쿼리를 날리지 않아도 된다.
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stock);
        return findItem;        //Tranactional이 되면서 em.flush가 되는 시점에 변경된 값들이 알아서 db로 들어간다.(Dirty Checking)
        //-> 위와 같이 set을 하게 되면 원하는 속성들만 값을 변경가능
    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
