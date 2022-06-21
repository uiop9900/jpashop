package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository //
@RequiredArgsConstructor
public class MemberRepository {

    public final EntityManager em;
    /*@PersistenceContext
    private EntityManager em;*/
    //spring이 자동으로 entitymanager 생성해서 넣어준다.(jpa 제공)
    //service와 동일하게 @requiredArgsConstructor가 em을 대상으로 생성자를 만들어준다.
    // 그러면 굳이 @persistenceContext 하지 않아도 된다.

    public void save(Member member) {
        em.persist(member); //member를 받아서 저장한다.
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);   //Member member =  em.find(Member.class, id) -> id값을 넣어서 member를 반환받는다.
    }

    public List<Member> findAll() { //전체를 select하기 때문에 em이 아니라 jpql을 사용한다., 쿼리복잡도에 따라 jpa < jpql < Querydsl

        return em.createQuery("select m from Member m", Member.class) .getResultList(); //jpql, 반환 타입
        //sql은 테이블 대상, 엔티티 객체를 대상으로 쿼리를 짠다. m = Member
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) //pa
                .getResultList();
    }
}
