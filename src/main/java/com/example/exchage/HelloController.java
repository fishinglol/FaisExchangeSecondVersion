package com.example.exchage;
// Importing necessary JavaFX and IO classes
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    // Declaring FXML elements from the FXML file
    @FXML
    private Label welcomeLabel;
    @FXML
    private ComboBox<String> fromCurrencyComboBox;

    @FXML
    private ComboBox<String> toCurrencyComboBox;

    @FXML
    private Label resultLabel;

    @FXML
    private Label conversionLabel;

    @FXML
    private TextField amountTextField;
    // JsonObject to hold currency data retrieved from the API
    private JsonObject currencyData;
    // Method called when the controller is initialized
    @FXML
    private void initialize() {
        try {
            // Fetching currency data from the API
            currencyData = getCurrencyData(new URL("https://api.frankfurter.app/latest"));
            // Populating ComboBoxes with currency codes
            if (currencyData != null) {
                JsonObject ratesObject = currencyData.getAsJsonObject("rates");
                List<String> currencyCodes = new ArrayList<>(ratesObject.keySet());
                currencyCodes.add("EUR");

                fromCurrencyComboBox.getItems().addAll(currencyCodes);
                toCurrencyComboBox.getItems().addAll(currencyCodes);
            } else {
                System.out.println("Failed to fetch currency data from API");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method called when the Convert button is clicked
    @FXML
    private void convertButtonClicked() {
        // Retrieving user inputs
        String fromCurrency = fromCurrencyComboBox.getValue();
        String toCurrency = toCurrencyComboBox.getValue();
        String amountText = amountTextField.getText();

        try {
            // Parsing amount to double
            double amount = Double.parseDouble(amountText);
            // Opening connection to API
            URL url = new URL("https://api.frankfurter.app/latest?amount=" + amount +
                    "&from=" + fromCurrency + "&to=" + toCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Reading response from API
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                // Parsing JSON response
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonObject ratesObject = jsonResponse.getAsJsonObject("rates");
                double convertedAmount = ratesObject.getAsJsonPrimitive(toCurrency).getAsDouble();
                // Formatting and displaying conversion result
                String conversionText = String.format("%.2f %s is %.2f %s", amount, fromCurrency, convertedAmount, toCurrency);
                welcomeLabel.setText(conversionText);
                // Save conversion history
                saveConversion(conversionText);
            }
        } catch (NumberFormatException e) {
            welcomeLabel.setText("Invalid amount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch currency data from API
    private JsonObject getCurrencyData(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonResponse;
        } else {
            System.out.println("Error: HTTP request failed with response code " + responseCode);
            return null;
        }
    }
    // Method to save conversion to history
    private void saveConversion(String data) {
        try (FileWriter fw = new FileWriter("conversion_history.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(data + "\n");
        } catch (IOException e) {
            System.err.println("Unable to save to database: " + e.getMessage());
        }
    }

    // Method to display conversion history
    @FXML
    private void displayHistory() {
        try (FileReader fr = new FileReader("conversion_history.txt");
             BufferedReader br = new BufferedReader(fr)) {
            StringBuilder history = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                history.append(line).append("\n");
            }
            // Display history in a dialog or another UI component
            // For simplicity, we'll just print it to the console here
            System.out.println("Conversion History:");
            System.out.println(history.toString());
        } catch (IOException e) {
            System.err.println("Unable to read from database: " + e.getMessage());
        }
    }
}
