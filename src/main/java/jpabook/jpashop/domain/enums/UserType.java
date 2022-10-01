package jpabook.jpashop.domain.enums;

import jpabook.jpashop.domain.item.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;


@Builder
@RequiredArgsConstructor
public enum UserType {
    MEMBER(User.UserTypeCd.M0001),
    USER(User.UserTypeCd.M0002);

    private final User.UserTypeCd userTypeCd;


}
