package com.practise.Currency;

import com.practise.utils.ConnectionManager;
import com.practise.utils.Parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Currency;
import java.util.List;
import java.util.logging.Logger;

public class DatabasePopulator implements PopulatorStatements{

    private static final Connection connection = ConnectionManager.getConnection();
    private static final Logger LOGGER = Logger.getLogger(DatabasePopulator.class.getName());

    public static void init(){
        //delete
        //populate using list of currencies using interface
        //then delete people from parser database
        //adding employees - search up
        LOGGER.info("Initialising Database Populator...");
        deleteCurrenciesFromDatabase();
        populateCurrencies(Parser.parseCurrency());
    }

    private static void populateCurrencies(List<CurrencyDTO> currencies){
        LOGGER.info("Populating currencies from currency list");
        for(CurrencyDTO currency: currencies){
            addCurrencyToDatabase(currency);
        }
        LOGGER.config("Employees populated successfully");
    }

    public static void addCurrencyToDatabase(CurrencyDTO currency){
        try(PreparedStatement populateStatement = connection.prepareStatement(INSERT)){
            populateStatement.setString(1,currency.code());
            populateStatement.setString(2, String.valueOf(currency.currency()));
            populateStatement.setString(3, currency.name());

            populateStatement.executeUpdate();
        } catch(SQLException e) {
            LOGGER.warning("Error when populating the current currency: " + e.getMessage());
        }
    }


    public static void deleteCurrenciesFromDatabase(){
        LOGGER.info("Deleting from database");
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(DELETE);
        } catch(SQLException e){
            LOGGER.severe("Error when deleting currencies from table: " + e.getMessage());
        }
    }
}
