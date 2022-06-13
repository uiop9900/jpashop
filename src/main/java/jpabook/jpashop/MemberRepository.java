package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;


    public Long save(Member member) {
        em.persist(member);     //member를 저장한다.
        return member.getId();      //왜 id만 반환하는가? command랑 query를 분리.
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
