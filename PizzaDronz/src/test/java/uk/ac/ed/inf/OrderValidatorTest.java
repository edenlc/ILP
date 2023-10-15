package uk.ac.ed.inf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class OrderValidatorTest {

    @Test
    public void adtest(){
        OrderValidator validator = new OrderValidator();

        Pizza pizza1 = new Pizza("Super Cheese", 1400);
        Pizza pizza2 = new Pizza("All Shrooms", 900);
        Pizza pizza3 = new Pizza("Pure Pepperoni", 1600);
        Pizza pizza4 = new Pizza("", 0);

        Pizza[] pizzasInOrder1 = new Pizza[]{pizza1, pizza2};
        CreditCardInformation cardInformation1 = new CreditCardInformation("1349947269650411", "12/23", "952");
        Order order1 = new Order("19514FE0", LocalDate.of(2023, 10, 30), 2400, pizzasInOrder1, cardInformation1);

        Restaurant restaurant1 = new Restaurant("Civerinos Slice", new LngLat(-3.1912869215011597, 55.945535152517735),
                                                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                                                new Pizza[]{pizza1, pizza2});
        Restaurant restaurant2 = new Restaurant("Dominos", new LngLat(-3.1912869215011597, 55.945535152517735),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                new Pizza[]{pizza3, pizza4});

        Order orderChecked = validator.validateOrder(order1, new Restaurant[]{restaurant1, restaurant2});

        assertEquals(OrderValidationCode.NO_ERROR, orderChecked.getOrderValidationCode());
    }
}
