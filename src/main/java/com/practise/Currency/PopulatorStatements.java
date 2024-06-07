package com.practise.Currency;

public interface PopulatorStatements {
    String INSERT = "INSERT INTO currencies (code, currency, name) VALUES (?, ?, ?)";

    String DELETE = "DELETE FROM currencies";

}