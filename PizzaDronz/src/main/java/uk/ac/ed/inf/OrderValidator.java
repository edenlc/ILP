package uk.ac.ed.inf;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

public class OrderValidator implements uk.ac.ed.inf.ilp.interfaces.OrderValidation{
    public static void OrderValidator(Order order, Restaurant[] restaurants){

    }

    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {
        return null;
    }
}
