import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
enum SessionState {
    IDLE,
    AUTHENTICATING,
    AUTHENTICATED,
    TERMINATED
}
class Account {
    private final String accountNumber;
    private final String pin;
    private BigDecimal balance;

    Account(String accountNumber, String pin, BigDecimal openingBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = openingBalance.setScale(2, RoundingMode.HALF_UP);
    }

    String getAccountNumber() {
        return accountNumber;
    }
     boolean verifyPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    BigDecimal getBalance() {
        return balance;
    }

    void deposit(BigDecimal amount) {
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
    }

    boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) {
            return false;
        }

        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        return true;
    }
}
class Bank {
    private final Map<String, Account> accounts = new HashMap<>();

    void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
}
class ATMSession {
    private static final int MAX_PIN_ATTEMPTS = 3;

    private final Bank bank;
    private SessionState state = SessionState.IDLE;
    private Account currentAccount;
    private int failedPinAttempts;

    ATMSession(Bank bank) {
        this.bank = bank;
    }
    SessionState getState() {
        return state;
    }

    boolean start(String accountNumber) {
        if (state != SessionState.IDLE) {
            return false;
        }

        currentAccount = bank.findAccount(accountNumber);
        if (currentAccount == null) {
            return false;
        }
        failedPinAttempts = 0;
        state = SessionState.AUTHENTICATING;
        return true;
    }
boolean verifyPin(String pin) {
        ensureState(SessionState.AUTHENTICATING);

        if (currentAccount.verifyPin(pin)) {
            state = SessionState.AUTHENTICATED;
            failedPinAttempts = 0;
            return true;
        }

        failedPinAttempts++;
        if (failedPinAttempts >= MAX_PIN_ATTEMPTS) {
            end();
        }

        return false;
    }
     int getRemainingPinAttempts() {
        return MAX_PIN_ATTEMPTS - failedPinAttempts;
    }

    BigDecimal checkBalance() {
        ensureState(SessionState.AUTHENTICATED);
        return currentAccount.getBalance();
    }

    boolean deposit(BigDecimal amount) {
        ensureState(SessionState.AUTHENTICATED);
        validatePositiveAmount(amount);
        currentAccount.deposit(amount);
        return true;
    }

    boolean withdraw(BigDecimal amount) {
        ensureState(SessionState.AUTHENTICATED);
        validatePositiveAmount(amount);
        return currentAccount.withdraw(amount);
    }
    void end() {
        currentAccount = null;
        failedPinAttempts = 0;
        state = SessionState.TERMINATED;
    }

    void reset() {
        currentAccount = null;
        failedPinAttempts = 0;
        state = SessionState.IDLE;
    }

    private void ensureState(SessionState expectedState) {
        if (state != expectedState) {
            throw new IllegalStateException("Operation not allowed while session is " + state);
        }
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }
}
public class AtmStimulationByAditi {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Bank bank = seedBank();
        ATMSession session = new ATMSession(bank);

        System.out.println("Welcome to the ATM Simulation System");

        boolean running = true;
        while (running) {
            session.reset();

            if (!authenticate(session)) {
                running = askYesNo("Would you like to try another account? (y/n): ");
                continue;
            }

            runAuthenticatedMenu(session);
            running = askYesNo("Would you like to start a new session? (y/n): ");
        }

        System.out.println("Thank you for using the ATM. Goodbye.");
    }
    private static Bank seedBank() {
        Bank bank = new Bank();
        bank.addAccount(new Account("1001", "1234", new BigDecimal("1500.00")));
        bank.addAccount(new Account("1002", "4321", new BigDecimal("250.75")));
        bank.addAccount(new Account("1003", "1111", new BigDecimal("10000.00")));
        return bank;
    }
    private static boolean authenticate(ATMSession session) {
        System.out.println();
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        if (!session.start(accountNumber)) {
            System.out.println("Account not found.");
            return false;
        }
         while (session.getState() == SessionState.AUTHENTICATING) {
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            if (session.verifyPin(pin)) {
                System.out.println("PIN verified. Session authenticated.");
                return true;
            }

            if (session.getState() == SessionState.TERMINATED) {
                System.out.println("Too many incorrect PIN attempts. Session terminated.");
                return false;
            }

            System.out.println("Incorrect PIN. Attempts remaining: " + session.getRemainingPinAttempts());
        }

        return false;
    }
    private static void runAuthenticatedMenu(ATMSession session) {
        boolean active = true;

        while (active && session.getState() == SessionState.AUTHENTICATED) {
            System.out.println();
            System.out.println("1. Check balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. End session");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Current balance: $" + formatMoney(session.checkBalance()));
                    break;
                case "2":
                    handleWithdrawal(session);
                    break;
                case "3":
                    handleDeposit(session);
                    break;
                case "4":
                    session.end();
                    active = false;
                    System.out.println("Session ended.");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-4.");
                    break;
            }
        }
    }

    private static void handleWithdrawal(ATMSession session) {
        BigDecimal amount = readAmount("Enter withdrawal amount: ");

        try {
            if (session.withdraw(amount)) {
                System.out.println("Withdrawal successful.");
                System.out.println("New balance: $" + formatMoney(session.checkBalance()));
            } else {
                System.out.println("Insufficient funds.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void handleDeposit(ATMSession session) {
        BigDecimal amount = readAmount("Enter deposit amount: ");

        try {
            session.deposit(amount);
            System.out.println("Deposit successful.");
            System.out.println("New balance: $" + formatMoney(session.checkBalance()));
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static BigDecimal readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid numeric amount.");
            }
        }
    }

    private static boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }

            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }

            System.out.println("Please enter y or n.");
        }
    }

    private static String formatMoney(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}