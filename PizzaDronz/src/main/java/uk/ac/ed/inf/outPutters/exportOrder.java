package uk.ac.ed.inf.outPutters;

import uk.ac.ed.inf.ilp.data.Order;

//Helper class to deal with the format for exporting orders
public class exportOrder {
    private String orderNo, orderStatus, orderValidationCode;
    private int costInPence;

    public exportOrder(Order order){
        this.orderNo = order.getOrderNo();
        this.orderStatus = order.getOrderStatus().name();
        this.orderValidationCode = order.getOrderValidationCode().name();
        this.costInPence = order.getPriceTotalInPence();
    }

    public String getOrderNo(){return orderNo;}
    public String getOrderStatus(){return orderStatus;}
    public String getOrderValidationCode(){return orderValidationCode;}
    public int getCostInPence(){return costInPence;}

}
