/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author GKV
 */
public class ConnectionFactory {
    
    // SQLite database file
    private static final String URL = "jdbc:sqlite:inventory.db";
    private static ConnectionFactory instance;
    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement prepStatement = null;
    private ResultSet resultSet = null;

    // Private constructor for Singleton pattern
    private ConnectionFactory() {
        try {
            // Create connection
            conn = DriverManager.getConnection(URL);
            
            // Create statement
            statement = conn.createStatement();
            
            // Optional timeout
            statement.setQueryTimeout(30);
            
            System.out.println("SQLite database connected.");
            
            // Create tables automatically if they don't exist
            initializeDatabase();
            
            // Create default admin
            createDefaultAdmin();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Singleton getter
    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }
    
    // Return connection
    public Connection getConn() {
        return conn;
    }
    
    // Return statement
    public Statement getStatement() {
        return statement;
    }
    
    // Initialize all database tables
    private void initializeDatabase() {
        try {
            // USERS TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    userID INTEGER PRIMARY KEY AUTOINCREMENT,
                    fullname TEXT NOT NULL,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    location TEXT,
                    phone TEXT,
                    usertype TEXT NOT NULL
                )
            """);
            
            // PRODUCTS TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS products (
                    pid INTEGER PRIMARY KEY AUTOINCREMENT,
                    productcode TEXT UNIQUE NOT NULL,
                    productname TEXT NOT NULL,
                    costprice REAL DEFAULT 0,
                    sellprice REAL DEFAULT 0,
                    brand TEXT
                )
            """);
            
            // CURRENT STOCK TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS currentstock (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    productcode TEXT UNIQUE NOT NULL,
                    quantity INTEGER DEFAULT 0,
                    FOREIGN KEY (productcode) REFERENCES products(productcode)
                )
            """);
            
            // SUPPLIERS TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS suppliers (
                    supplierID INTEGER PRIMARY KEY AUTOINCREMENT,
                    suppliercode TEXT UNIQUE NOT NULL,
                    fullname TEXT NOT NULL,
                    location TEXT,
                    mobile TEXT,
                    debit REAL DEFAULT 0,
                    credit REAL DEFAULT 0,
                    balance REAL DEFAULT 0
                )
            """);
            
            // CUSTOMERS TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS customers (
                    customerID INTEGER PRIMARY KEY AUTOINCREMENT,
                    customercode TEXT UNIQUE NOT NULL,
                    fullname TEXT NOT NULL,
                    location TEXT,
                    phone TEXT,
                    debit REAL DEFAULT 0,
                    credit REAL DEFAULT 0,
                    balance REAL DEFAULT 0
                )
            """);
            
            // PURCHASE INFO TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS purchaseinfo (
                    purchaseID INTEGER PRIMARY KEY AUTOINCREMENT,
                    suppliercode TEXT NOT NULL,
                    productcode TEXT NOT NULL,
                    date TEXT NOT NULL,
                    quantity INTEGER DEFAULT 0,
                    totalcost REAL DEFAULT 0,
                    FOREIGN KEY (productcode) REFERENCES products(productcode),
                    FOREIGN KEY (suppliercode) REFERENCES suppliers(suppliercode)
                )
            """);
            
            // SALES INFO TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS salesinfo (
                    salesID INTEGER PRIMARY KEY AUTOINCREMENT,
                    date TEXT NOT NULL,
                    productcode TEXT NOT NULL,
                    customercode TEXT NOT NULL,
                    quantity INTEGER DEFAULT 0,
                    revenue REAL DEFAULT 0,
                    soldby TEXT NOT NULL,
                    FOREIGN KEY (productcode) REFERENCES products(productcode),
                    FOREIGN KEY (customercode) REFERENCES customers(customercode),
                    FOREIGN KEY (soldby) REFERENCES users(username)
                )
            """);
            
            // USER LOGS TABLE
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS userlogs (
                    logID INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    in_time TEXT,
                    out_time TEXT,
                    FOREIGN KEY (username) REFERENCES users(username)
                )
            """);
            
            System.out.println("All database tables initialized successfully.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Create default admin account
    private void createDefaultAdmin() {
        try {
            String checkQuery = "SELECT * FROM users WHERE username = 'admin' LIMIT 1";
            ResultSet rs = statement.executeQuery(checkQuery);
            
            // If admin does not exist yet
            if (!rs.next()) {
                String insertQuery = """
                    INSERT INTO users (fullname, username, password, location, phone, usertype)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;
                
                prepStatement = conn.prepareStatement(insertQuery);
                prepStatement.setString(1, "System Administrator");
                prepStatement.setString(2, "admin");
                prepStatement.setString(3, "1234");
                prepStatement.setString(4, "Main Office");
                prepStatement.setString(5, "N/A");
                prepStatement.setString(6, "ADMINISTRATOR");
                prepStatement.executeUpdate();
                
                System.out.println("Default admin account created.");
                System.out.println("Username: admin");
                System.out.println("Password: 1234");
            } else {
                System.out.println("Admin account already exists.");
            }
            
            rs.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Login verification method
    public boolean checkLogin(String username, String password, String userType) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND usertype = ? LIMIT 1";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            prepStatement.setString(3, userType);
            resultSet = prepStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Release connection (for Singleton pattern)
    public void releaseConnection() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (prepStatement != null) {
                prepStatement.close();
                prepStatement = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Close all resources (for application shutdown)
    public void closeConnection() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (prepStatement != null) {
                prepStatement.close();
                prepStatement = null;
            }
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}