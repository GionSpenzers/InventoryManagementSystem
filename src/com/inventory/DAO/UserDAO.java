/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.DAO;

import com.inventory.DTO.UserDTO;
import com.inventory.Database.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Locale;
import java.util.Vector;

/**
 *
 * @author GKV
 */

// Data Access Object for Users
public class UserDAO {
    
    private Connection conn = null;
    private PreparedStatement prepStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private ConnectionFactory connectionFactory;

    public UserDAO() {
        try {
            connectionFactory = ConnectionFactory.getInstance();
            conn = connectionFactory.getConn();
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== ADD METHODS ====================
    
    // Method to add new user
    public void addUserDAO(UserDTO userDTO, String userType) {
        String checkQuery = "SELECT * FROM users WHERE username = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, userDTO.getUsername());
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Username already exists.");
            } else {
                addFunction(userDTO, userType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking user: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    private void addFunction(UserDTO userDTO, String userType) {
        String query = "INSERT INTO users (fullname, location, phone, username, password, usertype) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUsername());
            prepStatement.setString(5, userDTO.getPassword());
            prepStatement.setString(6, userDTO.getUserType());
            prepStatement.executeUpdate();
            
            if ("ADMINISTRATOR".equals(userType)) {
                JOptionPane.showMessageDialog(null, "New administrator added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "New employee added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(null, "Username already exists. Please use a different username.");
            } else {
                JOptionPane.showMessageDialog(null, "Error adding user: " + e.getMessage());
            }
        } finally {
            closeResources();
        }
    }

    // ==================== EDIT METHODS ====================
    
    // Method to edit existing user details
    public void editUserDAO(UserDTO userDTO) {
        String query = "UPDATE users SET fullname = ?, location = ?, phone = ?, usertype = ? WHERE username = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUserType());
            prepStatement.setString(5, userDTO.getUsername());
            prepStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "User details have been updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    // Update user without password change
    public void updateUser(UserDTO userDTO, String oldUsername) {
        String query = "UPDATE users SET fullname = ?, location = ?, phone = ?, username = ?, usertype = ? WHERE username = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUsername());
            prepStatement.setString(5, userDTO.getUserType());
            prepStatement.setString(6, oldUsername);
            prepStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "User updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(null, "Username already exists! Please choose a different username.");
            } else {
                JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage());
            }
        } finally {
            closeResources();
        }
    }

    // Update user with password change
    public void updateUserWithPassword(UserDTO userDTO, String oldUsername) {
        String query = "UPDATE users SET fullname = ?, location = ?, phone = ?, username = ?, password = ?, usertype = ? WHERE username = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUsername());
            prepStatement.setString(5, userDTO.getPassword());
            prepStatement.setString(6, userDTO.getUserType());
            prepStatement.setString(7, oldUsername);
            prepStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "User updated with new password!");
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(null, "Username already exists! Please choose a different username.");
            } else {
                JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage());
            }
        } finally {
            closeResources();
        }
    }
    
    // Update user with code change (username change)
    public void updateUserWithCodeChange(UserDTO userDTO, String oldUsername) {
        try {
            conn.setAutoCommit(false);
            
            // Check if new username already exists
            String checkQuery = "SELECT username FROM users WHERE username = ? AND username != ?";
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, userDTO.getUsername());
            prepStatement.setString(2, oldUsername);
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Username already exists! Please use a different username.");
                conn.rollback();
                return;
            }
            
            // Update users table
            String query = "UPDATE users SET fullname = ?, location = ?, phone = ?, username = ?, password = ?, usertype = ? WHERE username = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUsername());
            prepStatement.setString(5, userDTO.getPassword());
            prepStatement.setString(6, userDTO.getUserType());
            prepStatement.setString(7, oldUsername);
            prepStatement.executeUpdate();
            
            // Update userlogs table
            String query2 = "UPDATE userlogs SET username = ? WHERE username = ?";
            prepStatement = conn.prepareStatement(query2);
            prepStatement.setString(1, userDTO.getUsername());
            prepStatement.setString(2, oldUsername);
            prepStatement.executeUpdate();
            
            conn.commit();
            JOptionPane.showMessageDialog(null, "User updated with new username!");
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources();
        }
    }

    // ==================== DELETE METHODS ====================
    
    // Method to delete existing user (by username)
    public void deleteUserDAO(String username) {
        // Check if user has any sales records
        String checkQuery = "SELECT * FROM salesinfo WHERE soldby = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, username);
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "This user has sales records. Deleting may affect reports. Continue?",
                    "Warning", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Delete user
            String query = "DELETE FROM users WHERE username = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            int rowsAffected = prepStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    // Method to delete existing user (by userID)
    public void deleteUserDAO(int userId) {
        String query = "DELETE FROM users WHERE userID = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, userId);
            int rowsAffected = prepStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    // ==================== GETTER/QUERY METHODS ====================
    
    // Method to retrieve data set to be displayed
    public ResultSet getQueryResult() {
        try {
            String query = "SELECT username, fullname, location, phone, usertype FROM users ORDER BY fullname ASC";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get user by username
    public ResultSet getUserDAO(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get full name by username
    public void getFullName(UserDTO userDTO, String username) {
        String query = "SELECT fullname FROM users WHERE username = ? LIMIT 1";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                userDTO.setFullName(resultSet.getString("fullname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
    // Verify password
    public boolean verifyPassword(String username, String password) {
        boolean isValid = false;
        String query = "SELECT password FROM users WHERE username = ? AND password = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            resultSet = prepStatement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return isValid;
    }
    
    // Change password
    public void changePassword(String username, String password) {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, password);
            prepStatement.setString(2, username);
            prepStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Password has been changed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error changing password: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    // Get user type
    public String getUserType(String username) {
        String userType = null;
        String query = "SELECT usertype FROM users WHERE username = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                userType = resultSet.getString("usertype");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return userType;
    }
    
    // Get all users (simple list for dropdowns)
    public ResultSet getAllUsers() {
        try {
            String query = "SELECT username, fullname FROM users ORDER BY fullname";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== SEARCH METHODS ====================
    
    // Method to retrieve search data
    public ResultSet searchUsers(String searchText) {
        try {
            String query = """
                SELECT username, fullname, location, phone, usertype 
                FROM users 
                WHERE username LIKE ? 
                   OR fullname LIKE ? 
                   OR location LIKE ? 
                   OR phone LIKE ? 
                   OR usertype LIKE ?
                ORDER BY fullname ASC
                """;
            prepStatement = conn.prepareStatement(query);
            String searchPattern = "%" + searchText + "%";
            prepStatement.setString(1, searchPattern);
            prepStatement.setString(2, searchPattern);
            prepStatement.setString(3, searchPattern);
            prepStatement.setString(4, searchPattern);
            prepStatement.setString(5, searchPattern);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Search users by user type
    public ResultSet searchUsersByType(String userType) {
        try {
            String query = "SELECT username, fullname, location, phone, usertype FROM users WHERE usertype = ? ORDER BY fullname";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userType);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== USER LOGS METHODS ====================
    
    // Get user logs
    public ResultSet getUserLogsDAO() {
        try {
            String query = """
                SELECT 
                    users.fullname,
                    userlogs.username,
                    userlogs.in_time,
                    userlogs.out_time,
                    users.location
                FROM userlogs
                INNER JOIN users ON userlogs.username = users.username
                ORDER BY userlogs.in_time DESC
                """;
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Search user logs
    public ResultSet searchUserLogs(String searchText) {
        try {
            String query = """
                SELECT 
                    users.fullname,
                    userlogs.username,
                    userlogs.in_time,
                    userlogs.out_time,
                    users.location
                FROM userlogs
                INNER JOIN users ON userlogs.username = users.username
                WHERE users.fullname LIKE ? 
                   OR userlogs.username LIKE ? 
                   OR userlogs.in_time LIKE ?
                   OR userlogs.out_time LIKE ?
                   OR users.location LIKE ?
                ORDER BY userlogs.in_time DESC
                """;
            prepStatement = conn.prepareStatement(query);
            String searchPattern = "%" + searchText + "%";
            prepStatement.setString(1, searchPattern);
            prepStatement.setString(2, searchPattern);
            prepStatement.setString(3, searchPattern);
            prepStatement.setString(4, searchPattern);
            prepStatement.setString(5, searchPattern);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Add login log
    public void addUserLogin(UserDTO userDTO) {
        String query = "INSERT INTO userlogs (username, in_time, out_time) VALUES (?, ?, ?)";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getUsername());
            prepStatement.setString(2, userDTO.getInTime());
            prepStatement.setString(3, userDTO.getOutTime());
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // ==================== UTILITY METHODS ====================
    
    // Method to display data set in tabular form
    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return new DefaultTableModel();
        }
        
        ResultSetMetaData metaData = resultSet.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int colCount = metaData.getColumnCount();

        for (int col = 1; col <= colCount; col++) {
            columnNames.add(metaData.getColumnName(col).toUpperCase(Locale.ROOT));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int col = 1; col <= colCount; col++) {
                vector.add(resultSet.getObject(col));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }
    
    // Check if user exists
    public boolean userExists(String username) {
        boolean exists = false;
        String query = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            resultSet = prepStatement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exists;
    }
    
    // Get total number of users
    public int getUserCount() {
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM users";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }
    
    // Get user count by type
    public int getUserCountByType(String userType) {
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM users WHERE usertype = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userType);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    private void closeResources() {
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

    public void close() {
        closeResources();
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}