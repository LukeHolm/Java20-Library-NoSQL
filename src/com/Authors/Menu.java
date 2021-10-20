package com.Authors;

public class Menu {
    private final InputHandler userInput = new InputHandler();

    public int mainMenu() {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("1. Add author");
        System.out.println("2. Add book");
        System.out.println("3. Show all authors");
        System.out.println("4. Show authors work");
        System.out.println("5. Remove author");
        System.out.println("0. Exit the Cerberus Assembly");

        return userInput.getIntFromUser(0, 5);
    }
}
