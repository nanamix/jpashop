package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    //상품주문테스트

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("시골JPA", 10000, 10);
        em.persist(book);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        
        //Then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품주문시 상태는 ORDER ", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품종류가 정확해야 한다", 1,  getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 곱하기 수량이다", 10000* orderCount, getOrder.getTotalPrice());
        assertEquals("주문수량만큼 재고가 줄어야 한다", 8, book.getStockQunantity());
    }



    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골JPA", 10000, 10);

        int orderCount = 11;

        //when
        orderService.order(member.getId(),item.getId(), orderCount);
        
        //Then
        fail("재고수량부족예외가 발생해야 한다");
    }
    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Book item = createBook("시골JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when ==> 실제 테스트하고 싶은거
        orderService.cancelOrder(orderId);

        //Then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문취소시 상태는 CANCEL이 되어야 한다", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가되어야 한다", 10, item.getStockQunantity());

        
    }


    private Book createBook(String name, int price, int stockQunantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQunantity(stockQunantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "논현로", "12345"));
        em.persist(member);
        return member;
    }
}