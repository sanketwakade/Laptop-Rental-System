import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class laptop {
    private String laptopId;
    private String brand;
    private String Storage;
    private double basePricePerDay;
    private boolean isAvailable;

    public laptop(String laptopId, String brand, String Storage, double basePricePerDay) {
        this.laptopId = laptopId;
        this.brand = brand;
        this.Storage = Storage;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getlaptopId() {
        return laptopId;
    }

    public String getBrand() {
        return brand;
    }

    public String getStorage() {
        return Storage;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnLaptop() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private laptop laptop;
    private Customer customer;
    private int days;

    public Rental(laptop laptop, Customer customer, int days) {
        this.laptop = laptop;
        this.customer = customer;
        this.days = days;
    }

    public laptop getlaptop() {
        return laptop;
    }

    public Customer getcustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class LaptopRentalSystem {
    private List<laptop> laptops; // Changed variable name from 'laptop' to 'laptops'
    private List<Customer> customers;
    private List<Rental> rentals;

    public LaptopRentalSystem() {
        laptops = new ArrayList<>(); // Initialize the list of laptops
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addLaptop(laptop laptop) {
        laptops.add(laptop); // Corrected: adding to 'laptops' list
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentLaptop(laptop laptop, Customer customer, int days) {
        if (laptop.isAvailable()) {
            laptop.rent();
            rentals.add(new Rental(laptop, customer, days));
            System.out.println("Laptop rented successfully."); // Added success message
        } else {
            System.out.println("Laptop is not available for rent.");
        }
    }

    public void returnLaptop(laptop laptop) {
        laptop.returnLaptop();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getlaptop() == laptop) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
            System.out.println("Laptop returned successfully."); // Added success message
        } else {
            System.out.println("Laptop was not rented.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Laptop Rental System ====");
            System.out.println("1. Rent a Laptop");
            System.out.println("2. Return a Laptop"); // Corrected "Return a Car"
            System.out.println("3. Exit");
            System.out.print("Enter your choice : "); // Changed "println" to "print" for better input prompt

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n== Rent a Laptop ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();

                System.out.println("\nAvailable Laptops:"); // Corrected "Available Laptop" to plural
                if (laptops.isEmpty()) { // Check if there are any laptops
                    System.out.println("No laptops available to rent.");
                    continue; // Go back to the main menu
                }
                for (laptop laptop : laptops) { // Iterate over 'laptops' list
                    if (laptop.isAvailable()) {
                        System.out.println(laptop.getlaptopId() + " - " + laptop.getBrand() + " " + laptop.getStorage());
                    }
                }

                System.out.print("\nEnter the laptop ID you want to rent: "); // Corrected "car ID"
                String laptopIdInput = scanner.nextLine(); // Changed variable name to avoid conflict

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                laptop selectedlaptop = null;
                for (laptop laptop : laptops) { // Iterate over 'laptops' list
                    if (laptop.getlaptopId().equals(laptopIdInput) && laptop.isAvailable()) { // Use laptopIdInput
                        selectedlaptop = laptop;
                        break;
                    }
                }

                if (selectedlaptop != null) {
                    double totalPrice = selectedlaptop.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Laptop: " + selectedlaptop.getBrand() + " " + selectedlaptop.getStorage()); // Corrected "Car"
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentLaptop(selectedlaptop, newCustomer, rentalDays);
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid Laptop selection or Laptop not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Laptop ==\n");
                System.out.print("Enter the laptop ID you want to return: ");
                String laptopIdReturn = scanner.nextLine(); // Changed variable name

                laptop laptopToReturn = null;
                for (laptop laptop : laptops) { // Iterate over 'laptops' list
                    if (laptop.getlaptopId().equals(laptopIdReturn) && !laptop.isAvailable()) {
                        laptopToReturn = laptop;
                        break;
                    }
                }

                if (laptopToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getlaptop() == laptopToReturn) {
                            customer = rental.getcustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnLaptop(laptopToReturn);
                        System.out.println("Laptop returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Laptop was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid laptop ID or laptop is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Laptop Rental System!"); // Corrected "Car Rental System"
        scanner.close(); // Close the scanner
    }
}

public class Main {
    public static void main(String[] args) {
        LaptopRentalSystem rentalSystem = new LaptopRentalSystem();

        laptop laptop1 = new laptop("G03024443", "HP-Pavalion", "500Gb", 30.0);
        laptop laptop2 = new laptop("G03024444", "HP-Notebook", "1-Tb", 40.0);
laptop laptop3 = new laptop("G03024446", "Acer", "500Gb", 35.0);
laptop laptop4 = new laptop("G03024447", "Dell-XPS", "1-Tb", 55.0);
laptop laptop5 = new laptop("G03024448", "Lenovo-IdeaPad", "256Gb", 28.0);
laptop laptop6 = new laptop("G03024449", "Asus-ZenBook", "512Gb", 48.0);
laptop laptop7 = new laptop("G03024450", "Apple-MacBookAir", "256Gb", 60.0);
laptop laptop8 = new laptop("G03024451", "Microsoft-Surface", "500Gb", 52.0);
laptop laptop9 = new laptop("G03024452", "HP-Spectre", "1-Tb", 65.0);
laptop laptop10 = new laptop("G03024453", "Dell-Inspiron", "500Gb", 32.0);
laptop laptop11 = new laptop("G03024454", "Lenovo-Yoga", "512Gb", 45.0);
laptop laptop12 = new laptop("G03024455", "Acer-Aspire", "1-Tb", 38.0);
laptop laptop13 = new laptop("G03024456", "Asus-ROG", "1-Tb", 70.0);
laptop laptop14 = new laptop("G03024457", "HP-Envy", "512Gb", 49.0);
laptop laptop15 = new laptop("G03024458", "Dell-Latitude", "256Gb", 40.0);
laptop laptop16 = new laptop("G03024459", "Lenovo-ThinkPad", "1-Tb", 58.0);
laptop laptop17 = new laptop("G03024460", "Apple-MacBookPro", "512Gb", 75.0);
laptop laptop18 = new laptop("G03024461", "Microsoft-SurfacePro", "256Gb", 60.0);
laptop laptop19 = new laptop("G03024462", "HP-OMEN", "1-Tb", 72.0);
laptop laptop20 = new laptop("G03024463", "Acer-Predator", "1-Tb", 68.0);
laptop laptop21 = new laptop("G03024464", "Asus-VivoBook", "500Gb", 33.0);
laptop laptop22 = new laptop("G03024465", "Dell-G-Series", "1-Tb", 50.0);
laptop laptop23 = new laptop("G03024466", "Lenovo-Legion", "1-Tb", 62.0);
laptop laptop24 = new laptop("G03024467", "HP-EliteBook", "512Gb", 55.0);
laptop laptop25 = new laptop("G03024468", "Apple-iMac", "1-Tb", 80.0); // Assuming you might have "laptops" that are all-in-ones
laptop laptop26 = new laptop("G03024469", "Microsoft-SurfaceLaptop", "256Gb", 48.0);
laptop laptop27 = new laptop("G03024470", "HP-Chromebook", "128Gb", 25.0);
laptop laptop28 = new laptop("G03024471", "Dell-Vostro", "500Gb", 30.0);
laptop laptop29 = new laptop("G03024472", "Lenovo-ThinkBook", "512Gb", 42.0);
laptop laptop30 = new laptop("G03024473", "Acer-Swift", "256Gb", 39.0);
laptop laptop31 = new laptop("G03024474", "Asus-TUF-Gaming", "1-Tb", 60.0);
laptop laptop32 = new laptop("G03024475", "HP-ProBook", "500Gb", 35.0);
laptop laptop33 = new laptop("G03024476", "Dell-Alienware", "2-Tb", 90.0);
laptop laptop34 = new laptop("G03024477", "Lenovo-Chromebook", "64Gb", 20.0);
laptop laptop35 = new laptop("G03024478", "Acer-Chromebook", "32Gb", 18.0);
laptop laptop36 = new laptop("G03024479", "Asus-ExpertBook", "512Gb", 47.0);
laptop laptop37 = new laptop("G03024480", "HP-Victus", "1-Tb", 58.0);
laptop laptop38 = new laptop("G03024481", "Dell-Precision", "1-Tb", 85.0);
laptop laptop39 = new laptop("G03024482", "Lenovo-IdeaPad-Flex", "256Gb", 38.0);
laptop laptop40 = new laptop("G03024483", "Acer-Spin", "500Gb", 41.0);
laptop laptop41 = new laptop("G03024484", "Asus-RepublicOfGamers", "2-Tb", 95.0);
laptop laptop42 = new laptop("G03024485", "HP-Essential", "500Gb", 29.0);
laptop laptop43 = new laptop("G03024486", "Dell-XPS-15", "1-Tb", 68.0);
laptop laptop44 = new laptop("G03024487", "Lenovo-ThinkPad-X1", "512Gb", 70.0);
laptop laptop45 = new laptop("G03024488", "Acer-ConceptD", "1-Tb", 78.0);
laptop laptop46 = new laptop("G03024489", "Asus-ZenBook-Pro", "1-Tb", 72.0);
laptop laptop47 = new laptop("G03024490", "HP-ZBook", "2-Tb", 88.0);
laptop laptop48 = new laptop("G03024491", "Dell-G3", "500Gb", 45.0);
laptop laptop49 = new laptop("G03024492", "Lenovo-Yoga-Slim", "512Gb", 50.0);
laptop laptop50 = new laptop("G03024493", "Acer-Nitro", "1-Tb", 55.0);
      
rentalSystem.addLaptop(laptop1); // Corrected method name
        rentalSystem.addLaptop(laptop2);
        rentalSystem.addLaptop(laptop3);
        rentalSystem.addLaptop(laptop4);
        rentalSystem.addLaptop(laptop5);
        rentalSystem.addLaptop(laptop6);
        rentalSystem.addLaptop(laptop7);
        rentalSystem.addLaptop(laptop8);
        rentalSystem.addLaptop(laptop9);
        rentalSystem.addLaptop(laptop10);
        rentalSystem.addLaptop(laptop11);
        rentalSystem.addLaptop(laptop12);
        rentalSystem.addLaptop(laptop13);
        rentalSystem.addLaptop(laptop14);
        rentalSystem.addLaptop(laptop15);
        rentalSystem.addLaptop(laptop16);
        rentalSystem.addLaptop(laptop17);
        rentalSystem.addLaptop(laptop18);
        rentalSystem.addLaptop(laptop19);
        rentalSystem.addLaptop(laptop20);
        rentalSystem.addLaptop(laptop21);
        rentalSystem.addLaptop(laptop22);
        rentalSystem.addLaptop(laptop23);
        rentalSystem.addLaptop(laptop24);
        rentalSystem.addLaptop(laptop25);
        rentalSystem.addLaptop(laptop26);
        rentalSystem.addLaptop(laptop27);
        rentalSystem.addLaptop(laptop28);
        rentalSystem.addLaptop(laptop29);
        rentalSystem.addLaptop(laptop30);
        rentalSystem.addLaptop(laptop31);
        rentalSystem.addLaptop(laptop32);
        rentalSystem.addLaptop(laptop33);
        rentalSystem.addLaptop(laptop34);
        rentalSystem.addLaptop(laptop35);
        rentalSystem.addLaptop(laptop36);
        rentalSystem.addLaptop(laptop37);
        rentalSystem.addLaptop(laptop38);
        rentalSystem.addLaptop(laptop39);
        rentalSystem.addLaptop(laptop40);
        rentalSystem.addLaptop(laptop41);
        rentalSystem.addLaptop(laptop42);
        rentalSystem.addLaptop(laptop43);
        rentalSystem.addLaptop(laptop44);
        rentalSystem.addLaptop(laptop45);
        rentalSystem.addLaptop(laptop46);
        rentalSystem.addLaptop(laptop47);
        rentalSystem.addLaptop(laptop48);
        rentalSystem.addLaptop(laptop49);
        rentalSystem.addLaptop(laptop50);

        rentalSystem.menu();
    }
}