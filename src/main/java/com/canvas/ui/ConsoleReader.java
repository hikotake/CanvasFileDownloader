package com.canvas.ui;

import java.io.Console;

public class ConsoleReader {
    public String readConsole(String variableName) {
        Console console = System.console();
        return console.readLine("Enter " + variableName + ": ");
    }
}
