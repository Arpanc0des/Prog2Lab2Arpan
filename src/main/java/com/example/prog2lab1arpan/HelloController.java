package com.example.prog2lab1arpan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public TableView<Table> table_view;
    @FXML
    private TableColumn<Table, Integer> id;
    @FXML
    private TableColumn<Table, String> name;
    @FXML
    private TableColumn<Table, Integer> sinNumber;
    @FXML
    private TableColumn<Table, Integer> age;

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

    @FXML
    protected void getData() {
        populateTable();
    }

    public void populateTable() {
        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/programming2_lab1_arpansilwal";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute a SQL query to retrieve data from the database
            String query = "SELECT * FROM `table`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            // Populate the table with data from the database
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int sinNumber = resultSet.getInt("sinNumber");
                int age = resultSet.getInt("age");
                table_view.getItems().add(new Table(id, name, sinNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}