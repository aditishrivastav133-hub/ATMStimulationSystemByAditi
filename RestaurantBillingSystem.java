import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Menu {
    String dishName;
    double dishPrice;

    Menu(String dishName, double dishPrice) {
        this.dishName = dishName;
        this.dishPrice = dishPrice;
    }
}

public class RestaurantBillingSystem {

    static ArrayList<Menu> menu = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int billCounter = 1001;

    public static void displayMenu() {

        System.out.println("\n------ MENU ------");

        for (int i = 0; i < menu.size(); i++) {

            Menu item = menu.get(i);

            System.out.println((i + 1) + ". " + item.dishName+ " - Rs."+ item.dishPrice);
        }
    }

    public static void addItem() {

        sc.nextLine();

        System.out.print("Enter Item Name: ");
        String dishName = sc.nextLine();

        System.out.print("Enter Item Price: ");
        double dishPrice = sc.nextDouble();

        menu.add(new Menu(dishName, dishPrice));

        System.out.println("Item Added Successfully!");
    }

    public static void removeItem() {

        displayMenu();

        System.out.print("Enter Item Number To Remove: ");
        int choice = sc.nextInt();

        if (choice >= 1 && choice <= menu.size()) {

            String removedItem =menu.get(choice - 1).dishName;

            menu.remove(choice - 1);

            System.out.println(removedItem + " Removed Successfully!");
        }

        else {
            System.out.println("Invalid Choice!");
        }
    }

    public static void createBill() {

        sc.nextLine();

        System.out.print("Enter Customer Name: ");
        String customerName = sc.nextLine();

        System.out.print("Enter Phone Number: ");
        String phoneNumber = sc.nextLine();

        double subtotal = 0;

        ArrayList<String> orderedItems = new ArrayList<>();

        ArrayList<Integer> quantities =new ArrayList<>();

        ArrayList<Double> itemAmounts =new ArrayList<>();

        while (true) {

            displayMenu();

            System.out.print("\nEnter Item Number (0 To Finish): ");

            int choice = sc.nextInt();

            if (choice == 0) {
                break;
            }

            if (choice < 1 || choice > menu.size()) {

                System.out.println("Invalid Choice!");
                continue;
            }

            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();

            Menu item = menu.get(choice - 1);

            double amount =
                    item.dishPrice * qty;

            orderedItems.add(item.dishName);
            quantities.add(qty);
            itemAmounts.add(amount);

            subtotal += amount;
        }

        double gst = subtotal * 0.05;
        double totalBill = subtotal + gst;

        LocalDateTime now =LocalDateTime.now();

        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        System.out.println("\n========================================");

        System.out.println("RESTAURANT BILL");

        System.out.println("========================================");

        System.out.println("Bill No : " + billCounter++);

        System.out.println("Date & Time   : "+ now.format(formatter));

        System.out.println("Customer Name : " + customerName);

        System.out.println("Phone Number  : "+ phoneNumber);

        System.out.println("----------------------------------------");

        System.out.printf("%-20s %-8s %-10s%n","Item","Qty","Amount");

        System.out.println("----------------------------------------");

        for (int i = 0; i < orderedItems.size();i++) {

            System.out.printf("%-20s %-8d Rs.%.2f%n",orderedItems.get(i),quantities.get(i),itemAmounts.get(i));
        }

        System.out.println("----------------------------------------");

        System.out.printf("Subtotal      : Rs.%.2f%n",subtotal);

        System.out.printf( "GST (5%%)      : Rs.%.2f%n", gst);

        System.out.printf("Total Bill    : Rs.%.2f%n", totalBill);

        System.out.println("========================================");

        System.out.println(" Thank You! Visit Again :)");

        System.out.println(  "========================================");
    }

    public static void main(String[] args) {

        menu.add(new Menu("MasalaDosa", 198));
        menu.add(new Menu("MacAndCheese", 298));
        menu.add(new Menu("VegNoodles", 180));
        menu.add(new Menu("IcedLatte", 180));
        menu.add(new Menu("BombaySandwich", 159));

        int option;

        do {

            System.out.println( "\n===== RESTAURANT BILLING SYSTEM =====");

            System.out.println("1. Display Menu");
            System.out.println("2. Add Item");
            System.out.println("3. Remove Item");
            System.out.println("4. Create Bill");
            System.out.println("5. Exit");

            System.out.print("Enter Your Choice: ");

            option = sc.nextInt();

            switch (option) {

                case 1:
                    displayMenu();
                    break;

                case 2:
                    addItem();
                    break;

                case 3:
                    removeItem();
                    break;

                case 4:
                    createBill();
                    break;

                case 5:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (option != 5);

        sc.close();
    }
}

