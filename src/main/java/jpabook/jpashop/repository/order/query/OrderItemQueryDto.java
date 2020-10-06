package jpabook.jpashop.repository.order.query;

import lombok.Data;

@Data
public class OrderItemQueryDto {
    private String itemName;
    private int OrderPrice;
    private int count;
}
