package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQunantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 재고증가
     * @param quantity
     */
    //비즈니스로직
    //재고를 늘리고 줄이고
    //도메인주도설계
    //엔티티안에 비즈니스설계를 넣어야
    public void addStock(int quantity){
        this.stockQunantity += quantity;
    }

    /**
     * 재고감소
     * @param quantity 
     */
    public void removeStock(int quantity){
        int restStock = this.stockQunantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("Need More Stock");
        }
        this.stockQunantity = restStock;
    }
}
