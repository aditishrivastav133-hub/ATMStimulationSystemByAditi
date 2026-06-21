This is a console-based ATM Simulation System built using Java.
It simulates real-world ATM operations like authentication, balance enquiry, deposit, and withdrawal with proper session management and security constraints like PIN attempts.

The project is designed using Object-Oriented Programming (OOP) principles and demonstrates clean architecture with multiple classes.
✨ Features
🔐 Account-based login using Account Number & PIN
🔁 Maximum 3 PIN attempts (security feature)
💰 Check account balance
➕ Deposit money
➖ Withdraw money with balance validation
🧠 Session management (IDLE → AUTHENTICATING → AUTHENTICATED → TERMINATED)
❌ Handles invalid inputs and exceptions safely
🔄 Multiple session support (restart system without restart program)

ATMSimulationSystem:-
──> SessionState (Enum - handles session status)
──>Account (handles account details and transactions)
──>Bank (stores multiple accounts using HashMap)
──>ATMSession (handles authentication & ATM operations)
──>AtmStimulationByAditi (Main class - user interface)

🛠️ Technologies Used
Java (Core Java)
OOP Concepts (Encapsulation, Classes, Objects)
Collections Framework (HashMap)
BigDecimal (for accurate financial calculations)
Scanner (for user input)
