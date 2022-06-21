package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(readOnly = true) //readOnly를 하면 정보들을 읽는 데에 효과적임
@RequiredArgsConstructor    //생성자를 자동으로 생성해주며 final 이 붙은 멤버변수를 생성자 파라미터에 알아서 넣어준다.
public class MemberService {

    private final MemberRepository memberRepository;

    //생성자가 하나만 있을 경우, spring이 알아서 autowired해준다. -> 굳이 @Autowired 하지 않아도 됨
    //생성자를 통한 autowired를 하는게 좋다. -> 생성자를 굳이 만들지말고 @RequiredArgsConstructior를 통해 생성자를 만들자.
    /*public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    /*
    회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);    //중복 회원 검증
        memberRepository.save(member);
        return member.getId();  //em으로 persist되는 순간, 영속성이 data를 감싸는데 키-value값으로 감싸고 이때 키값이 id값이다. 그렇기떄문에 getId()를 할 수 있음
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION - 중복이 있다면 예외를 터뜨려서 save가 안되게 한다.
        // 동명이인이 동시에 가입을 하게 되면 이게 뚫린다. -> DB에서 이름을 unique key 조건으로 구성해야한다. -> 멀티쓰레드로 인한 오류를 더블 체크
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) { //비어있지 않다 =  존재한다 = 중복이다.
            throw new IllegalStateException("이미 존재하는 회원입니다.");  //
        }

    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
