package jpabook.jpashop.domain;


import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //jpa 스펙상 기본생성자 필수 -> 다른 사람들이 쉽게 부르지 않게 protected로 설정
    protected Address() {
    }

    //address의 경우, 초기에만 set되고 그 뒤에는 값을 꺼내는 용도라 getter로만 써놓는게 좋다.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
