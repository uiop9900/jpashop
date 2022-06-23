package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor    //생성자로 autowired할때 사용한다.
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";

    }

    @PostMapping("/members/new")    //메소드는 동일하지만 mapping이 다르다.
    public String create(@Valid MemberForm form, BindingResult result) {    //@Valid를 통해 memberForm 의 validation check를 알아서 해준다.-> 객체에 @NotEmpty 선언되어 있음
        //BindingResult - 오류가 나도 튕기지 않고 오류를 가지고 실행이 된다.
        if (result.hasErrors()) {   //에러가 있다면
            return "members/createMemberForm";  //화면으로 에러를 가져가서 뿌려준다.
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";    //첫번째 페이지로 넘어간다.
        //도메인에서의 조건과 실제 넘어오는 value의 validation조건이 다를 수있기떄문에 Member member 자체를 넣지도 않고
        // 넣게 된다고 하더라고 조건들이 자꾸 도메인으로 넘어간다.(@NotEmpty..) 그러면 도메인이 지저분해지기때문에 화면만의 form data를 만들어서 값을 받는게 낫다.
    }

    @GetMapping("/members")
    public String list(Model model) {   //원래는 dto 변환해서 필요한 정보만 송출하는게 좋다. 지금은 간단한 작업이기에 member 객체를 내림
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "/members/memberList";
    }

}
