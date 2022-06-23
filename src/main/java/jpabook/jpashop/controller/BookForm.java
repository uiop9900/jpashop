package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    //item 공통 필드
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

}
