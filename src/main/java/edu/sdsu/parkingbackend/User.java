package edu.sdsu.parkingbackend;

import java.util.*;

public abstract class User {

    protected float userID;
    protected String userEmail;
    protected String userPassword;
    protected String userRole;
    protected boolean isLoggedIn = false;
    public boolean isLoggedIn() { return isLoggedIn; }
    Scanner scanner = new Scanner(System.in);

    void login() {

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (email.equals(userEmail) && password.equals(userPassword)) {
            isLoggedIn = true;
            System.out.println("Login successful! Welcome, " + userEmail + ".");
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    void logout() {

        if (isLoggedIn) {
            isLoggedIn = false;
            System.out.println("Logging out");
        } else {
            System.out.println("Not logged in");
        }
    }
}