package com.practise.utils;

import com.practise.Currency.CurrencyDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Parser {

    private static List<CurrencyDTO> parsedCurrency;
    private static final Logger LOGGER = Logger.getLogger(Parser.class.getName());


    public static void init(){
        ArrayList<String> rawCurriencies = getCurrenciesFromCSV();
        List<CurrencyDTO> currencyList = parseUncheckedCurrenciesData(rawCurriencies);
        parsedCurrency = parseCurrency(currencyList);

        LOGGER.info("Successfully parsed currencies");
    }

    public static ArrayList<String> getCurrenciesFromCSV(){
        LOGGER.info("Getting data from CSV...");
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resoucres/employees-corrupted.csv"))){
            bufferedReader.readLine();

            for(String line = bufferedReader.readLine(); line!=null; line = bufferedReader.readLine()){
                result.add(line);
            }

        } catch (IOException e) {
            LOGGER.severe("Could not read data from CSV: " + e.getMessage());
        }
        LOGGER.config("Successfully read data from CSV");
        return result;
    }

    public static List<CurrencyDTO> parseUncheckedCurrenciesData(ArrayList<String> rawCurrencies){
        LOGGER.info("Parsing data into employee objects...");
        ArrayList<CurrencyDTO> result = new ArrayList<CurrencyDTO>();
        for(String s : rawCurrencies){
            String [] parsedCurrency = (s.split(","));
            result.add(new CurrencyDTO(parsedCurrency[0],parsedCurrency[1].charAt(0), parsedCurrency[2]));
        }
        LOGGER.config("Successively parsed unchecked currencies");
        return result;
    }

    public static List<CurrencyDTO> parseCurrency(List<CurrencyDTO> currencies){
        LOGGER.info("Parsing employee objects for corrupted data...");
        List<CurrencyDTO> filtered = currencies.stream()
                .filter( currency -> checkIfValidCode(currency.code()))
                .collect(Collectors.toList());
        return removeDuplicates(filtered);
    }

    public static boolean checkIfValidCode(String code){
        LOGGER.finest("Checking if the currency code is valid");
        return code.length()==3;
    }

    public static List<CurrencyDTO> removeDuplicates(List<CurrencyDTO> currencies){
        HashMap<String, Integer> codes = new HashMap<String, Integer>();
        for(CurrencyDTO currency : currencies){
            String code = currency.code();
            codes.put(code, codes.getOrDefault(codes, 0)+1);
        }
        List<CurrencyDTO> uniqueCurrencies = new ArrayList<>();
        for(CurrencyDTO currency : currencies){
            String code = currency.code();
            if(1==codes.get(code)){
                uniqueCurrencies.add(currency);
            }
        }
        return uniqueCurrencies;
    }


}
