package com.pragma.plazoleta.domain.constants;

public final class DomainConstants {

    private DomainConstants() {
    }
    //REGEX
    public static final String DOCUMENT_NUMBER_REGEX = "^[0-9]+$";
    public static final String PHONE_NUMBER_REGEX = "^\\+?\\d{7,13}$";

    //EXCEPTION
    //Dish
    public static final String DIA = "Dish is inactive";
    public static final String DNF = "Dish not found";

    public static final String RNF = "Restaurant not found";
    public static final String ENF = "Employee not found";
    public static final String ONF = "Order not found";
    public static final String NAR = "You are not allowed to manage dishes for this restaurant";
    public static final String NAE = "The employee is not allowed to manage this order";
    public static final String UNO = "The user is not the owner of this restaurant";
    public static final String CAE = "The client already has an active order";
    public static final String IDC = "Invalid delivery code";
    public static final String DCNF = "Delivery code not found for the order";

}
