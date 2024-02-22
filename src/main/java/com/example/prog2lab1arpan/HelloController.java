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
    public ComboBox<String> comboBoxId;
    @FXML
    private TableColumn<Table, Integer> id, sinNumber, age;
    @FXML
    private TableColumn<Table, String> name;
    ObservableList<Table> list = FXCollections.observableArrayList();

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
        switch (selectedOption) {
            case "AddRow", "UpdateRow" -> {
                idField.setDisable(false);
                nameField.setDisable(false);
                sinNumberField.setDisable(false);
                ageField.setDisable(false);
            }
            case "DeleteRow" -> {
                idField.setDisable(false);
                nameField.setDisable(true);
                sinNumberField.setDisable(true);
                ageField.setDisable(true);
            }
            default -> errorMessage.setText("Choose one of the CRUD operation to make any kind of change");
        }
    }

    //-------------------------Input validation section (AKA stupid proofing)
    public void executeCrud() {
        if (selectedOption != null) {
            if (validateRowCRUD()) {
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

    private boolean validateRowCRUD() {
        switch (selectedOption) {
            case "AddRow" -> {
                if (nameField.getText().isEmpty() || sinNumberField.getText().isEmpty() || ageField.getText().isEmpty()) {
                    errorMessage.setText("You need data to insert. Fill in data to execute.");
                    return false;
                }
                try {
                    Integer.parseInt(sinNumberField.getText());
                } catch (NumberFormatException e) {
                    errorMessage.setText("Sin number must be a number.");
                    return false;
                }
                try {
                    Integer.parseInt(ageField.getText());
                } catch (NumberFormatException e) {
                    errorMessage.setText("Age must be a number.");
                    return false;
                }
            }
            case "UpdateRow" -> {
                if (idField.getText().isEmpty() || nameField.getText().isEmpty() || sinNumberField.getText().isEmpty() || ageField.getText().isEmpty()) {
                    errorMessage.setText("You need data to update. Fill in data to execute.");
                    return false;
                }
                try {
                    Integer.parseInt(idField.getText());
                } catch (NumberFormatException e) {
                    errorMessage.setText("ID must be a number.");
                    return false;
                }
                try {
                    Integer.parseInt(sinNumberField.getText());
                } catch (NumberFormatException e) {
                    errorMessage.setText("Sin number must be a number.");
                    return false;
                }
                try {
                    Integer.parseInt(ageField.getText());
                } catch (NumberFormatException e) {
                    errorMessage.setText("Age must be a number.");
                    return false;
                }
            }
            case "DeleteRow" -> {
                if (idField.getText().isEmpty()) {
                    errorMessage.setText("Fill in ID to execute.");
                    return false;
                }
                try {
                    Integer.parseInt(idField.getText());
                } catch (NumberFormatException e) {
                    errorMessage.setText("ID must be a number.");
                    return false;
                }
            }
        }

        return true;
    }

    //=====================end of validation
    //-------------------------------------------insert data================================
    private boolean idExistsCheck(int id) {
        for (Table item : list) {
            if (item.getId() == id) {
                return true; // ID already exists
            }
        }
        return false; // ID does not exist
    }

    private void insertRow() {
        int idValue = Integer.parseInt(idField.getText());
        if (idExistsCheck(idValue)) {
            errorMessage.setText("ID already exists. Please enter a different ID.");
            return;
        }
        ; //cant return in a void so return will just terminate the function
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

    //========================================update data----------------------------------------
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

    //----------------------------------delete data============================================
    private void deleteRow() {
        int idValue = Integer.parseInt(idField.getText());
        if (!idExistsCheck(idValue)) {
            errorMessage.setText("This ID does not exist. Try something that exists.");
            return;
        }
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
