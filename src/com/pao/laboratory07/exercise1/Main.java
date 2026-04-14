package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        OrderState initialState = OrderState.valueOf(scanner.next());
        Order order = new Order(initialState);
        System.out.println("Initial order state: " + initialState);

        while (true) {
            OrderCommand orderCommand = OrderCommand.valueOf(scanner.next());
            switch (orderCommand) {
                case next -> {
                    try {
                        order.nextState();
                    } catch (OrderIsAlreadyFinalException e) {
                        System.out.println("Comanda este in stare finala.");
                    }
                }
                case cancel -> {
                    try {
                        order.cancel();
                    } catch (CannotCancelFinalOrderException e) {
                        System.out.println("Comanda este in stare finala.");
                    }
                }
                case undo -> {
                    try {
                        order.undoState();
                    } catch (CannotRevertInitialOrderStateException e) {
                        System.out.println("Nu există stare anterioară pentru undo.");
                    }
                }
                case QUIT -> {
                    System.out.println("User quit the program.");
                    return;
                }
            }
        }
    }
}