public class Rentals {
    public static void main(String[] args) {
        Database db = new Database();
        db.connect("sgms", "postgres", "pass");
        db.getAllInstruments("piano");
        db.rentInstrument("2","2");
        System.out.println(db.getStudentRentals("2"));
        db.getOngoingRentals();
        db.terminateRental("5");
        db.getOngoingRentals();
    }
}
