package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.enums.UserType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "USER") @Getter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderMethodName = "entityBuilder", toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User {
    @Id
    @Column(name = "USER_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long userNo; // user의 pk

    @Column
    private String name; // 이름

    @Column
    private int age; // 나이

    @Column
    private UserType type; // 회원,비회원 구분

    @Column
    private String email; // 이메일


    //==============enum====================
    @Getter
    @AllArgsConstructor
    public enum UserTypeCd {
        /** 회원 */
        M0001,
        /** 비회원 */
        M0002;

        public static final String USER_TYPE_CD = "USER_TYPE_CD";

        public static UserTypeCd getMemberCd() {
            return M0001;
        }

        public static UserTypeCd getUserCd() {
            return M0002;
        }
    }

    //===========Builder====================
    public static UserBuilder userBuiler( //리턴값은 클래스명 + Builer이어야 한다. @Builder로 인해 가능한 메솓,1
            @NonNull final String name,
            @NonNull final int age,
            @NonNull final UserType type,
            @NonNull final String email
    ) {
        return entityBuilder() //상단에서 선언한 @builder 때문에 사용가능한 메소드2
                .name(name)
                .age(age)
                .type(type)
                .email(email);
    }
}
