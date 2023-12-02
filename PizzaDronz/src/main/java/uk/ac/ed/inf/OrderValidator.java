package uk.ac.ed.inf;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;

public class OrderValidator implements uk.ac.ed.inf.ilp.interfaces.OrderValidation{
    public static void OrderValidator(Order order, Restaurant[] restaurants){
    }

    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {
        Order order = orderToValidate;

        /**
         * First we check the card
         * card no; exp date; cvv
         *
         * then order details
         * order total; undefined pizza; max count; multiple restaurants; restaurant closed
         */

        if (checkCardNo(orderToValidate)){
            order.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
        }
        else if (checkExpiry(orderToValidate)){
            order.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
        }
        else if (checkCvv(orderToValidate)){
            order.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
        }
        else if (checkOrderSize(orderToValidate)){
            order.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
        }
        else if (checkOrderTotal(orderToValidate)){
            order.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
        }
        else if (checkRestaurantNo(orderToValidate, definedRestaurants)){
            order.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        }
        else if (checkUndefinedPizza(orderToValidate, definedRestaurants)){
            order.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
        }
        else if (checkRestaurantClosed(orderToValidate, definedRestaurants)){
            order.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
        }
        else{
            order.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        }

        return order;
    }

    /**
     * Checks that the cardnumber is not null and len == 16
     * @param orderToValidate the order to be validated
     * @return true if not valid.
     */
    private boolean checkCardNo(Order orderToValidate){
        String cardNumber = orderToValidate.getCreditCardInformation().getCreditCardNumber();
        if (cardNumber == null || cardNumber.length() != 16){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks that the Expiry date on the card is valid.
     * Checks that it is not null, the month is valid, and that the card has not expired.
     * @param orderToValidate The order to be validated.
     * @return true if not valid
     */
    private boolean checkExpiry(Order orderToValidate){
        String cardExpiry = orderToValidate.getCreditCardInformation().getCreditCardExpiry();

        if (cardExpiry == null || cardExpiry.length() != 5){
            return true;
        }
        int month = Integer.parseInt(cardExpiry.substring(0, 2));
        int year = Integer.parseInt(cardExpiry.substring(3, 5), 10);

        if (month > 12 || month < 1) {
            return true;
        }

        YearMonth expiry = YearMonth.parse(cardExpiry, DateTimeFormatter.ofPattern("MM/yy"));
        YearMonth today = YearMonth.now();

        if (today.isAfter(expiry))
            return true;
        else{
            return false;
        }
    }

    /**
     * Checks that the Cvv is not null and 3 digits
     * @param orderToValidate the order to be validated
     * @return true if not valid
     */
    private boolean checkCvv(Order orderToValidate){
        String cvv = orderToValidate.getCreditCardInformation().getCvv();

        if (cvv == null || cvv.length() != 3){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks that the order total is correct
     * checks ordertotal = total of pizzas + 100p
     * @param orderToValidate the order to be validated
     * @return true if not valid
     */
    private boolean checkOrderTotal(Order orderToValidate){
        int total = 100;

        if (orderToValidate.getPizzasInOrder() == null){
            return true;
        }

        for (int i = 0; i < orderToValidate.getPizzasInOrder().length; i++){
            if (!(orderToValidate.getPizzasInOrder()[i] == null)){
                total += orderToValidate.getPizzasInOrder()[i].priceInPence();
            }
        }

        if (total != orderToValidate.getPriceTotalInPence()){
            return true;
        }
        else{
            return false;
        }


    }

    /**
     * Checks that all pizzas in the order are on the current restaurant's menu
     * The fact that they count be on another restaurant's menu does not matter as we check for that first.
     * @param orderToValidate Order to be validated
     * @param definedRestaurants array of all the restaurants
     * @return true if not valid
     */
    private boolean checkUndefinedPizza(Order orderToValidate, Restaurant[] definedRestaurants){
        Restaurant restaurant = findRestaurant(orderToValidate, definedRestaurants);
        if (restaurant == null || orderToValidate.getPizzasInOrder() == null){
            return true;
        }
        int count = 0;
        for (int n = 0; n < orderToValidate.getPizzasInOrder().length; n++){
            for (int m = 0; m < restaurant.menu().length; m++){
                if (orderToValidate.getPizzasInOrder()[n].name().equals(restaurant.menu()[m].name())){
                    count++;
                }
            }
        }
        if (count == orderToValidate.getPizzasInOrder().length){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Checks that the OrderSize is valid (not null, <=4 and >=1)
     * @param orderToValidate The order to be validated
     * @return true if invalid
     */
    private boolean checkOrderSize(Order orderToValidate){
        if (orderToValidate.getPizzasInOrder() == null || orderToValidate.getPizzasInOrder().length > 4 || orderToValidate.getPizzasInOrder().length < 1){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks that all the pizzas are not in the menus of multiple restaurants
     * @param orderToValidate Order to be validated
     * @param definedRestaurants array of all restaurants
     * @return true if invalid
     */
    private boolean checkRestaurantNo(Order orderToValidate, Restaurant[] definedRestaurants){
        int count = 0;

        if (definedRestaurants == null || orderToValidate == null){
            return true;
        }

        for (Restaurant restaurant : definedRestaurants) {
            if (restaurant == null){
                return true;
            }
            if (Arrays.stream(restaurant.menu()).anyMatch(order -> Arrays.stream(orderToValidate.getPizzasInOrder()).anyMatch(checkrestaurant -> order.name().equals(checkrestaurant.name())))) {
                count += 1;
            }
        }
        if (count != 1){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks that the restaurant is not closed on the date of the order
     * @param orderToValidate the order to validate
     * @param definedRestaurants the array of all restaurants
     * @return True if closed
     */
    private boolean checkRestaurantClosed(Order orderToValidate, Restaurant[] definedRestaurants){
        try {

            Restaurant restaurant = findRestaurant(orderToValidate, definedRestaurants);
            DayOfWeek currentDay = orderToValidate.getOrderDate().getDayOfWeek();

            if (restaurant == null) {
                return true;
            }
            if (Arrays.stream(restaurant.openingDays()).anyMatch(day -> day == currentDay)) {
                return false;
            } else {
                return true;
            }
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    /**
     * Helper function to find which restaurant is being ordered from
     * Since all pizzas are unique, finds the restaurant for which the first pizza on the menu is from
     * @param orderToValidate Order from which the pizza is in
     * @param definedRestaurants an array of all restaurants
     * @return  the restaurant from which the first pizza in the order is from.
     * Public so that can get the restaurant for the order in main
     */
    public Restaurant findRestaurant(Order orderToValidate, Restaurant[] definedRestaurants){
        Restaurant restaurant = null;

        if (definedRestaurants == null || orderToValidate.getPizzasInOrder() == null){
            System.out.println("null");
            return null;
        }
        for (int i = 0; i < definedRestaurants.length; i++){
            if (definedRestaurants[i] == null){
                System.out.println("No defined restaurants");
                return null;
            }
            if (definedRestaurants[i].menu() == null){
                System.out.println("no menu");
                return null;
            }
            for (int j = 0; j < definedRestaurants[i].menu().length; j++){
                if (orderToValidate.getPizzasInOrder()[0].name().equals(definedRestaurants[i].menu()[j].name())){
                    restaurant = definedRestaurants[i];
                }
            }
        }
        return restaurant;
    }


}
