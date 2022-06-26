package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        //간단하나 엔티티 자체를 드러내고, 엔티티에 웹을 위한 설정들이 추가되기때문에 절대 주의한다.
        //array로 바로 받아버리면 확장이 불가하다.
        return memberService.findMembers();
    }

    //v1과 다르게 엔티티-> dto로 변환하는 과정이 추가됨
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                        .map(m -> new MemberDto(m.getName()))
                        .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {    //이렇게 한 번 감싸서 내보내야 한다.
        private int count;      //추가적으로 담고 싶은 정보들은 편하게 추가가 가능하다. 한 번 감쌌기 때문에
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {   //@RequestBody: json으로 데이터를 멤버로 바꿔준다.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id =memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public updateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid updateMemberRequest request) {

        //command와 쿼리를 분리하기 위해 따로 또 쿼리를 날린다.
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new updateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class updateMemberRequest  {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class updateMemberResponse {
        private Long id;
        private String name;

    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }
    @Data
    static class CreateMemberResponse{
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
