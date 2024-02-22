package com.example.prog2lab1arpan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.*;

public class HelloController implements Initializable {
    public String selectedOption;
    @FXML
    public TableView<Table> table_view;
    @FXML
    public TextField idField, nameField, sinNumberField, ageField;
    @FXML
    public Label errorMessage;
    @FXML
    public String AddRow, UpdateRow, DeleteRow;
    @FXML
    public ComboBox comboBoxId;
    @FXML
    private TableColumn<Table, Integer> id, sinNumber, age;
    @FXML
    private TableColumn<Table, String> name;
    ObservableList<Table> list = FXCollections.observableArrayList();
    private URL url;
    private ResourceBundle resourceBundle;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<Table, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Table, String>("name"));
        sinNumber.setCellValueFactory(new PropertyValueFactory<Table, Integer>("sinNumber"));
        age.setCellValueFactory(new PropertyValueFactory<Table, Integer>("age"));
        table_view.setItems(list);
    }
    public void fetchTable() {
        list.clear(); // Clear the ObservableList before adding new items
        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab2_arpansilwal";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute a SQL query to retrieve data from the database
            String query = "SELECT * FROM `table`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            // Populate the ObservableList with data from the database
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int sinNumber = resultSet.getInt("sinNumber");
                int age = resultSet.getInt("age");

                // Add the data to the ObservableList
                list.add(new Table(id, name, sinNumber, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void getData() {
        fetchTable();
    }

    @FXML
    public void handleCRUDSelection() {
        selectedOption = (String) comboBoxId.getValue();
        if (selectedOption.equals("AddRow")) {
            idField.setDisable(true);
            nameField.setDisable(false);
            sinNumberField.setDisable(false);
            ageField.setDisable(false);
        } else if (selectedOption.equals("UpdateRow")) {
            idField.setDisable(false);
            nameField.setDisable(false);
            sinNumberField.setDisable(false);
            ageField.setDisable(false);
        } else if (selectedOption.equals("DeleteRow")) {
            idField.setDisable(false);
            nameField.setDisable(true);
            sinNumberField.setDisable(true);
            ageField.setDisable(true);
        } else {
            errorMessage.setText("Choose one of the CRUD operation to make any kind of change");
        }

    }

    public void executeCrud() {
        if ( (selectedOption == "AddRow") && validateAddRow()){
            insertRow();
        }
    }

    private void insertRow() {

        int idValue = parseInt(idField.getText());
        String nameValue = nameField.getText();
        int sinNumberValue = parseInt(sinNumberField.getText());
        int ageValue = parseInt(ageField.getText());

        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab2_arpansilwal";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute an SQL query to insert data into the database


            String query = "INSERT INTO `table` (name, sinNumber, age) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameValue);
            preparedStatement.setInt(2, sinNumberValue);
            preparedStatement.setInt(3, ageValue);
            // Execute the update
            preparedStatement.executeUpdate();
            // Clear the text fields after successful upload
            nameField.clear();
            sinNumberField.clear();
            ageField.clear();

            fetchTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateAddRow() {
        if (nameField.getText().isEmpty() || sinNumberField.getText().isEmpty() || ageField.getText().isEmpty()) {
            errorMessage.setText("You need data to insert. fill data to execute");
            return false;
        }
        try {
            int x = Integer.parseInt(sinNumberField.getText());
        }catch(NumberFormatException e) {
            errorMessage.setText("sin number must be a NUMBER");
            return false;
        }
        try {
            int x = Integer.parseInt(ageField.getText());
        }catch(NumberFormatException e) {
            errorMessage.setText("sin number must be a NUMBER");
            return false;
        }
        return true;
    }
}