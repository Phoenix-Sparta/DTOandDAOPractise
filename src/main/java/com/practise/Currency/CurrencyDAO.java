package com.practise.Currency;

import com.practise.utils.ConnectionManager;

import java.sql.*;
import java.util.logging.Logger;

public class CurrencyDAO implements DAOStatements {

    private final static Connection CONNECTION = ConnectionManager.getConnection();
    private final static Logger LOGGER = Logger.getLogger(CurrencyDAO.class.getName());


    private static PreparedStatement setStatementFromValue(String query, Object fieldValue) {
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            if (fieldValue instanceof Integer) {
                preparedStatement.setInt(1, (Integer) fieldValue);
            } else if (fieldValue instanceof String) {
                preparedStatement.setString(1, (String) fieldValue);
            }
            else if (fieldValue instanceof Character) {
                preparedStatement.setString(1, String.valueOf(fieldValue));
            }
            else if (fieldValue instanceof Date) {
                preparedStatement.setDate(1, (Date) fieldValue);
            }
            return preparedStatement;
        } catch (SQLException e) {
            LOGGER.warning("Exception when setting statement from value: " + e.getMessage());
            return null;
        }
    }

    public static void queryFromField(String fieldName, Object fieldValue) {
        LOGGER.config("Searching currencies: " + fieldName + " -> " + fieldValue + "\n");
        ResultSet resultSet = null;
        try {
            final String query = QUERY + fieldName + FIELD;
            PreparedStatement preparedStatement = setStatementFromValue(query, fieldValue);
            assert preparedStatement != null;
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                int counter = 0;
                while (resultSet.next()) {
                    counter++;
                    String result = resultSetToString(resultSet);
                    LOGGER.info(result);
                }
                if (counter != 0) LOGGER.warning(counter + " currencies found.");
                else LOGGER.severe("No currencies found under query: " + fieldName + " -> " + fieldValue);
                resultSet.close();
            }

        } catch (SQLException e) {
            LOGGER.info("Error while executing query: " + e.getMessage());
        }
    }

        private static String resultSetToString(ResultSet resultSet) {
            try {
                String code = resultSet.getString("code");
                String currency = resultSet.getString("currency");
                String name = resultSet.getString("name");
                return "Employee: " + code + " " + currency + " " + name + '\n';
            }
            catch (SQLException e)
            {
                LOGGER.warning("Error while retrieving data from result set: " + e.getMessage());
                return "";
            }

        }


    public static void deleteFromEmployees(String fieldName, Object fieldValue) {
        LOGGER.config("Deleting currency: " + fieldName + " from " + fieldValue + "\n");

        try{
            final String selectQuery = "SELECT * FROM currencies WHERE " + fieldName + " = ?";
            PreparedStatement selectStatement = setStatementFromValue(selectQuery,fieldValue);
            assert selectStatement != null;
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                // Print details of the selected row (if found)
                if (resultSet.next()) {
                    String deletedCurrency = resultSetToString(resultSet);
                    // Print details of the row before deleting
                    LOGGER.warning("Record to be deleted: " + deletedCurrency);
                } else {
                    LOGGER.info("No matching row found for deletion.");
                    return; // No need to proceed with deletion if no row found
                }
            }
        }
        catch (SQLException e) {
            LOGGER.info("Error while executing query: " + e.getMessage());
        }

        try{
            final String deleteQuery = "DELETE FROM currencies WHERE " + fieldName + " = ?";
            PreparedStatement deleteStatement = setStatementFromValue(deleteQuery, fieldValue);
            assert deleteStatement != null;
            int rowsDeleted = deleteStatement.executeUpdate();
            LOGGER.severe(rowsDeleted + " record(s) deleted");
        } catch (SQLException e) {
            LOGGER.severe("Error while executing delete query: " + e.getMessage());
        }

    }

    public static void closeConnection()
    {
        ConnectionManager.closeConnection();
    }
    }


