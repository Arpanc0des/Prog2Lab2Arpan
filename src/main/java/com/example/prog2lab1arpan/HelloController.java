package com.example.prog2lab1arpan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

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
    public ComboBox<String> comboBoxId;
    @FXML
    private TableColumn<Table, Integer> id, sinNumber, age;
    @FXML
    private TableColumn<Table, String> name;
    ObservableList<Table> list = FXCollections.observableArrayList();
    private URL url;
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        sinNumber.setCellValueFactory(new PropertyValueFactory<>("sinNumber"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        table_view.setItems(list);
        comboBoxId.setItems(FXCollections.observableArrayList("AddRow", "UpdateRow", "DeleteRow"));
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
        selectedOption = comboBoxId.getValue();
        if (selectedOption.equals("AddRow")) {
            idField.setDisable(false);
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
        if (selectedOption != null) {
            if (validateRow()) {
                switch (selectedOption) {
                    case "AddRow":
                        insertRow();
                        break;
                    case "UpdateRow":
                        updateRow();
                        break;
                    case "DeleteRow":
                        deleteRow();
                        break;
                }
            }
        } else {
            errorMessage.setText("Choose one of the CRUD operation to make any kind of change");
        }
    }

    private boolean validateRow() {
        boolean isValid = true;
        String errorMessageText = "";

        if (selectedOption.equals("AddRow")) {
            if (nameField.getText().isEmpty() || sinNumberField.getText().isEmpty() || ageField.getText().isEmpty()) {
                errorMessageText = "You need data to insert. Fill in data to execute.";
                isValid = false;
            }
            try {
                Integer.parseInt(sinNumberField.getText());
            } catch (NumberFormatException e) {
                errorMessageText = "Sin number must be a number.";
                isValid = false;
            }
            try {
                Integer.parseInt(ageField.getText());
            } catch (NumberFormatException e) {
                errorMessageText = "Age must be a number.";
                isValid = false;
            }
        } else if (selectedOption.equals("UpdateRow")) {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() || sinNumberField.getText().isEmpty() || ageField.getText().isEmpty()) {
                errorMessageText = "You need data to update. Fill in data to execute.";
                isValid = false;
            }
            try {
                Integer.parseInt(idField.getText());
            } catch (NumberFormatException e) {
                errorMessageText = "ID must be a number.";
                isValid = false;
            }
            try {
                Integer.parseInt(sinNumberField.getText());
            } catch (NumberFormatException e) {
                errorMessageText = "Sin number must be a number.";
                isValid = false;
            }
            try {
                Integer.parseInt(ageField.getText());
            } catch (NumberFormatException e) {
                errorMessageText = "Age must be a number.";
                isValid = false;
            }
        } else if (selectedOption.equals("DeleteRow")) {
            if (idField.getText().isEmpty()) {
                errorMessageText = "ID is required to delete a row. Fill in ID to execute.";
                isValid = false;
            }
            try {
                Integer.parseInt(idField.getText());
            } catch (NumberFormatException e) {
                errorMessageText = "ID must be a number.";
                isValid = false;
            }
        }

        if (!isValid) {
            errorMessage.setText(errorMessageText);
        }

        return isValid;
    }

    private void insertRow() {
        int idValue = Integer.parseInt(idField.getText());
        String nameValue = nameField.getText();
        int sinNumberValue = Integer.parseInt(sinNumberField.getText());
        int ageValue = Integer.parseInt(ageField.getText());

        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab2_arpansilwal";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute an SQL query to insert data into the database
            String query = "INSERT INTO `table` (id, name, sinNumber, age) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idValue);
            preparedStatement.setString(2, nameValue);
            preparedStatement.setInt(3, sinNumberValue);
            preparedStatement.setInt(4, ageValue);
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

    private void updateRow() {
        int idValue = Integer.parseInt(idField.getText());
        String nameValue = nameField.getText();
        int sinNumberValue = Integer.parseInt(sinNumberField.getText());
        int ageValue = Integer.parseInt(ageField.getText());

        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab2_arpansilwal";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute an SQL query to update data in the database
            String query = "UPDATE `table` SET name=?, sinNumber=?, age=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameValue);
            preparedStatement.setInt(2, sinNumberValue);
            preparedStatement.setInt(3, ageValue);
            preparedStatement.setInt(4, idValue);
            // Execute the updates
            preparedStatement.executeUpdate();
            // Clear the text fields after successful update
            idField.clear();
            nameField.clear();
            sinNumberField.clear();
            ageField.clear();

            fetchTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRow() {
        int idValue = Integer.parseInt(idField.getText());

        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab2_arpansilwal";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute an SQL query to delete data from the database
            String query = "DELETE FROM `table` WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idValue);
            // Execute the delete
            preparedStatement.executeUpdate();
            // Clear the text fields after successful delete
            idField.clear();
            nameField.clear();
            sinNumberField.clear();
            ageField.clear();

            fetchTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
