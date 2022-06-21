package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)    //spring이랑 같이 run한다.
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    //같은 Transactional안에서 같은 엔티티(id값이 동일)면 같은 영속성 context에서 똑같은 애로 관리가 된다.
    @Test
    @Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("lee");

        Member member2 = new Member();
        member2.setName("lee");

        //where
        memberService.join(member1);
        memberService.join(member2);

        // 상단에서 (expected = IllegalStateException.class)를 넣어주면 try, catch를 안해줘도 된다.
       /* try {
            memberService.join(member2);     //예외가 발생해야한다.
        } catch (IllegalStateException e) {
            return;
        }
*/
        //then
        fail("예외가 발생해야 합니다.");  //여기에 들어오면 안된다. 이 전에서 이미 예외처리가 되어야 한다.
    }
}