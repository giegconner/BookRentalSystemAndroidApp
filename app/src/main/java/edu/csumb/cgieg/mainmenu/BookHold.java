package edu.csumb.cgieg.mainmenu;

public class BookHold {
    private String transType;
    private String username;
    private String pickupDateHour;
    private String returnDateHour;
    private String bookTitle;
    private int reserveNum;
    private double totalAmount;
    private String dateHour;

    public BookHold(String aTransType, String aUsername, String aPickupDateHour, String aReturnDateHour,
                    String aBookTitle, int aReserveNum, double aTotalAmount, String aDateHour) {
        this.transType = aTransType;
        this.username = aUsername;
        this.pickupDateHour = aPickupDateHour;
        this.returnDateHour = aReturnDateHour;
        this.bookTitle = aBookTitle;
        this.reserveNum = aReserveNum;
        this.totalAmount = aTotalAmount;
        this.dateHour = aDateHour;
    }

    public String getTransType() {
        return this.transType;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPickupDateHour() {
        return this.pickupDateHour;
    }

    public String getReturnDateHour() {
        return this.returnDateHour;
    }

    public String getBookTitle() {
        return this.bookTitle;
    }

    public int getReserveNum() {
        return this.reserveNum;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public String getDateHour() {
        return this.dateHour;
    }
}
