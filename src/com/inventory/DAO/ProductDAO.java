/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.DAO;

import com.inventory.DTO.ProductDTO;
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


// Data Access Object for Products, Purchase, Stock and Sales
public class ProductDAO {

    private Connection conn = null;
    private PreparedStatement prepStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private ConnectionFactory connectionFactory;

    public ProductDAO() {
        try {
            connectionFactory = ConnectionFactory.getInstance();
            conn = connectionFactory.getConn();
            statement = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ==================== GETTER METHODS ====================
    
    public ResultSet getSuppInfo() {
        try {
            String query = "SELECT * FROM suppliers";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getCustInfo() {
        try {
            String query = "SELECT * FROM customers";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getProdStock() {
        try {
            String query = """
                SELECT currentstock.productcode, products.productname, 
                       currentstock.quantity, products.costprice, products.sellprice
                FROM currentstock 
                INNER JOIN products ON currentstock.productcode = products.productcode
                """;
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getProdInfo() {
        try {
            String query = "SELECT * FROM products ORDER BY pid";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Double getProdCost(String prodCode) {
        Double costPrice = null;
        String query = "SELECT costprice FROM products WHERE productcode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, prodCode);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                costPrice = resultSet.getDouble("costprice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return costPrice;
    }

    public Double getProdSell(String prodCode) {
        Double sellPrice = null;
        String query = "SELECT sellprice FROM products WHERE productcode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, prodCode);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                sellPrice = resultSet.getDouble("sellprice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return sellPrice;
    }

    public String getSuppCode(String suppName) {
        String suppCode = null;
        String query = "SELECT suppliercode FROM suppliers WHERE fullname = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppName);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                suppCode = resultSet.getString("suppliercode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return suppCode;
    }

    public String getProdCode(String prodName) {
        String prodCode = null;
        String query = "SELECT productcode FROM products WHERE productname = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, prodName);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                prodCode = resultSet.getString("productcode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return prodCode;
    }

    public String getCustCode(String custName) {
        String custCode = null;
        String query = "SELECT customercode FROM customers WHERE fullname = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custName);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                custCode = resultSet.getString("customercode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return custCode;
    }

    public String getSuppName(int ID) {
        String name = null;
        String query = """
            SELECT suppliers.fullname FROM suppliers 
            INNER JOIN purchaseinfo ON suppliers.suppliercode = purchaseinfo.suppliercode 
            WHERE purchaseinfo.purchaseid = ?
            """;
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("fullname");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResources();
        }
        return name;
    }

    public String getCustName(int ID) {
        String name = null;
        String query = """
            SELECT customers.fullname FROM customers 
            INNER JOIN salesinfo ON customers.customercode = salesinfo.customercode 
            WHERE salesinfo.salesid = ?
            """;
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("fullname");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResources();
        }
        return name;
    }

    public String getPurchaseDate(int ID) {
        String date = null;
        String query = "SELECT date FROM purchaseinfo WHERE purchaseid = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                date = resultSet.getString("date");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResources();
        }
        return date;
    }

    public String getSaleDate(int ID) {
        String date = null;
        String query = "SELECT date FROM salesinfo WHERE salesid = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                date = resultSet.getString("date");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResources();
        }
        return date;
    }

    public ResultSet getProdName(String code) {
        String query = "SELECT productname FROM products WHERE productcode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, code);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    
    // Get purchase by ID
    public ResultSet getPurchaseById(int purchaseID) {
        String query = "SELECT * FROM purchaseinfo WHERE purchaseID = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, purchaseID);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== STOCK CHECK METHODS ====================
    
    public boolean checkStock(String prodCode) {
        boolean flag = false;
        String query = "SELECT * FROM currentstock WHERE productcode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, prodCode);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return flag;
    }

    // ==================== ADD METHODS ====================
    
    public void addProductDAO(ProductDTO productDTO) {  // ADD THE PARAMETER
        String checkQuery = "SELECT * FROM products WHERE productcode = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, productDTO.getProdCode());  // Use getProdCode(), not getProdID()
            resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Product code already exists.");
            } else {
                addFunction(productDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
    private void addFunction(ProductDTO productDTO) {
    try {
        System.out.println("=== ADDING PRODUCT ===");
        System.out.println("Product Code: " + productDTO.getProdCode());
        System.out.println("Product Name: " + productDTO.getProdName());
        System.out.println("Cost Price: " + productDTO.getCostPrice());
        System.out.println("Sell Price: " + productDTO.getSellPrice());
        System.out.println("Brand: " + productDTO.getBrand());
        System.out.println("Quantity: " + productDTO.getQuantity());
        
        conn.setAutoCommit(false);
        
        // Insert into products table
        String query = "INSERT INTO products (productcode, productname, costprice, sellprice, brand) VALUES (?, ?, ?, ?, ?)";
        prepStatement = conn.prepareStatement(query);
        prepStatement.setString(1, productDTO.getProdCode());
        prepStatement.setString(2, productDTO.getProdName());
        prepStatement.setDouble(3, productDTO.getCostPrice());
        prepStatement.setDouble(4, productDTO.getSellPrice());
        prepStatement.setString(5, productDTO.getBrand());
        int productResult = prepStatement.executeUpdate();
        System.out.println("Products insert result: " + productResult);

        // Insert into currentstock table
        String query2 = "INSERT INTO currentstock (productcode, quantity) VALUES (?, ?)";
        prepStatement = conn.prepareStatement(query2);
        prepStatement.setString(1, productDTO.getProdCode());
        prepStatement.setInt(2, productDTO.getQuantity());
        int stockResult = prepStatement.executeUpdate();
        System.out.println("Currentstock insert result: " + stockResult);

        conn.commit();
        System.out.println("Transaction committed successfully");
        JOptionPane.showMessageDialog(null, "Product added and ready for sale.");
        
    } catch (SQLException throwables) {
        try {
            conn.rollback();
            System.out.println("Transaction rolled back due to error");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwables.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error adding product: " + throwables.getMessage());
    } finally {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeResources();
    }
}

    public void addPurchaseDAO(ProductDTO productDTO) {
        try {
            conn.setAutoCommit(false);
            
            String query = "INSERT INTO purchaseinfo (suppliercode, productcode, date, quantity, totalcost) VALUES (?, ?, ?, ?, ?)";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, productDTO.getSuppCode());
            prepStatement.setString(2, productDTO.getProdCode());
            prepStatement.setString(3, productDTO.getDate());
            prepStatement.setInt(4, productDTO.getQuantity());
            prepStatement.setDouble(5, productDTO.getTotalCost());
            prepStatement.executeUpdate();
            
            conn.commit();
            JOptionPane.showMessageDialog(null, "Purchase log added.");
            
        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding purchase: " + throwables.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources();
        }

        // Update stock
        String prodCode = productDTO.getProdCode();
        if (checkStock(prodCode)) {
            updateStockQuantity(prodCode, productDTO.getQuantity(), "add");
        } else {
            insertStock(productDTO.getProdCode(), productDTO.getQuantity());
        }
        deleteStock();
    }

    // ==================== UPDATE METHODS ====================
    
    public void editProdDAO(ProductDTO productDTO) {
    try {
        conn.setAutoCommit(false);
        
        String query = "UPDATE products SET productname = ?, costprice = ?, sellprice = ?, brand = ? WHERE productcode = ?";
        prepStatement = conn.prepareStatement(query);
        prepStatement.setString(1, productDTO.getProdName());
        prepStatement.setDouble(2, productDTO.getCostPrice());
        prepStatement.setDouble(3, productDTO.getSellPrice());
        prepStatement.setString(4, productDTO.getBrand());
        prepStatement.setString(5, productDTO.getProdCode());
        prepStatement.executeUpdate();

        conn.commit();
        
    } catch (SQLException throwables) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwables.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error updating product: " + throwables.getMessage());
    } finally {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeResources();
    }
}

    private void updateStockQuantity(String prodCode, int quantity, String operation) {
        String query = "UPDATE currentstock SET quantity = quantity " + (operation.equals("add") ? "+ ?" : "- ?") + " WHERE productcode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, quantity);
            prepStatement.setString(2, prodCode);
            prepStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void insertStock(String prodCode, int quantity) {
        String query = "INSERT INTO currentstock (productcode, quantity) VALUES (?, ?)";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, prodCode);
            prepStatement.setInt(2, quantity);
            prepStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void editPurchaseStock(String code, int quantity) {
        updateStockQuantity(code, quantity, "subtract");
    }

    public void editSoldStock(String code, int quantity) {
        updateStockQuantity(code, quantity, "add");
    }
    
    public void updatePurchaseDAO(ProductDTO productDTO) {
    String query = "UPDATE purchaseinfo SET date = ?, quantity = ?, totalcost = ? WHERE purchaseID = ?";
    try {
        prepStatement = conn.prepareStatement(query);
        prepStatement.setString(1, productDTO.getDate());
        prepStatement.setInt(2, productDTO.getQuantity());
        prepStatement.setDouble(3, productDTO.getTotalCost());
        prepStatement.setInt(4, productDTO.getPurchaseID());
        prepStatement.executeUpdate();
        
        // Also update current stock if quantity changed
        updateStockForPurchase(productDTO);
        
        JOptionPane.showMessageDialog(null, "Purchase updated successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error updating purchase: " + e.getMessage());
    } finally {
        closeResources();
    }
}
    
    // Update stock when purchase is updated
    private void updateStockForPurchase(ProductDTO productDTO) {
        String query = "UPDATE currentstock SET quantity = quantity + ? WHERE productcode = ?";
        try {
            // First get the original quantity
            ResultSet rs = getPurchaseById(productDTO.getPurchaseID());
            if (rs != null && rs.next()) {
                int originalQuantity = rs.getInt("quantity");
                int quantityDifference = productDTO.getQuantity() - originalQuantity;

                if (quantityDifference != 0) {
                    prepStatement = conn.prepareStatement(query);
                    prepStatement.setInt(1, quantityDifference);
                    prepStatement.setString(2, productDTO.getProdCode());
                    prepStatement.executeUpdate();
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // ==================== DELETE METHODS ====================
    
    public void deleteProductDAO(String code) {
        try {
            conn.setAutoCommit(false);
            
            String query = "DELETE FROM products WHERE productcode = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, code);
            prepStatement.executeUpdate();

            String query2 = "DELETE FROM currentstock WHERE productcode = ?";
            prepStatement = conn.prepareStatement(query2);
            prepStatement.setString(1, code);
            prepStatement.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(null, "Product has been removed.");
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources();
        }
        deleteStock();
    }

    public void deletePurchaseDAO(int ID) {
        String query = "DELETE FROM purchaseinfo WHERE purchaseID = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            prepStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Transaction has been removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        deleteStock();
    }

    public void deleteSaleDAO(int ID) {
        String query = "DELETE FROM salesinfo WHERE salesID = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            prepStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Transaction has been removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        deleteStock();
    }

    public void deleteStock() {
        try {
            String query = "DELETE FROM currentstock WHERE productcode NOT IN (SELECT productcode FROM purchaseinfo) AND productcode NOT IN (SELECT productcode FROM products)";
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // ==================== SALES METHOD ====================
    
    public void sellProductDAO(ProductDTO productDTO, String username) {
        int quantity = 0;
        String prodCode = null;
        
        String checkQuery = "SELECT productcode, quantity FROM currentstock WHERE productcode = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, productDTO.getProdCode());
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                prodCode = resultSet.getString("productcode");
                quantity = resultSet.getInt("quantity");
            }
            
            if (productDTO.getQuantity() > quantity) {
                JOptionPane.showMessageDialog(null, "Insufficient stock for this product.");
            } else if (productDTO.getQuantity() <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a valid quantity");
            } else {
                conn.setAutoCommit(false);
                
                String stockQuery = "UPDATE currentstock SET quantity = quantity - ? WHERE productcode = ?";
                prepStatement = conn.prepareStatement(stockQuery);
                prepStatement.setInt(1, productDTO.getQuantity());
                prepStatement.setString(2, productDTO.getProdCode());
                prepStatement.executeUpdate();
                
                String salesQuery = "INSERT INTO salesinfo (date, productcode, customercode, quantity, revenue, soldby) VALUES (?, ?, ?, ?, ?, ?)";
                prepStatement = conn.prepareStatement(salesQuery);
                prepStatement.setString(1, productDTO.getDate());
                prepStatement.setString(2, productDTO.getProdCode());
                prepStatement.setString(3, productDTO.getCustCode());
                prepStatement.setInt(4, productDTO.getQuantity());
                prepStatement.setDouble(5, productDTO.getTotalRevenue());
                prepStatement.setString(6, username);
                prepStatement.executeUpdate();
                
                conn.commit();
                JOptionPane.showMessageDialog(null, "Product sold.");
            }
        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources();
        }
    }

    // ==================== QUERY METHODS ====================
    
    public ResultSet getQueryResult() {
        try {
            String query = "SELECT productcode, productname, costprice, sellprice, brand FROM products ORDER BY productcode ASC";
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getPurchaseInfo() {
    try {
        String query = """
            SELECT purchaseID, purchaseinfo.productcode, products.productname, 
                   purchaseinfo.quantity, purchaseinfo.totalcost, purchaseinfo.date
            FROM purchaseinfo 
            INNER JOIN products ON products.productcode = purchaseinfo.productcode 
            ORDER BY purchaseID DESC
            """;
        resultSet = statement.executeQuery(query);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
    return resultSet;
}

    public ResultSet getCurrentStockInfo() {
        try {
            String query = """
                SELECT products.productcode, products.productname, currentstock.quantity,
                       products.costprice, products.sellprice
                FROM currentstock 
                INNER JOIN products ON currentstock.productcode = products.productcode
                ORDER BY products.productcode ASC
                """;
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSalesInfo() {
    try {
        String query = """
            SELECT salesinfo.salesid, salesinfo.productcode, products.productname,
                   salesinfo.quantity, salesinfo.revenue, users.fullname AS sold_by,
                   salesinfo.date
            FROM salesinfo 
            INNER JOIN products ON salesinfo.productcode = products.productcode
            INNER JOIN users ON salesinfo.soldby = users.username
            ORDER BY salesid DESC
            """;
        resultSet = statement.executeQuery(query);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
    return resultSet;
}

    // ==================== SEARCH METHODS ====================
    
    public ResultSet getProductSearch(String text) {
    try {
        String query = """
            SELECT productcode, productname, costprice, sellprice, brand
            FROM products 
            WHERE productcode LIKE ? 
               OR productname LIKE ? 
               OR brand LIKE ?
            ORDER BY productcode ASC
            """;
        prepStatement = conn.prepareStatement(query);
        String searchPattern = "%" + text + "%";
        prepStatement.setString(1, searchPattern);
        prepStatement.setString(2, searchPattern);
        prepStatement.setString(3, searchPattern);
        resultSet = prepStatement.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return resultSet;
}

    public ResultSet getProdFromCode(String text) {
        try {
            String query = "SELECT productcode, productname, costprice, sellprice, brand FROM products WHERE productcode = ? LIMIT 1";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, text);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSalesSearch(String text) {
        try {
            String query = """
                SELECT salesinfo.salesid, salesinfo.productcode, products.productname,
                       salesinfo.quantity, salesinfo.revenue, users.fullname AS sold_by
                FROM salesinfo 
                INNER JOIN products ON salesinfo.productcode = products.productcode
                INNER JOIN users ON salesinfo.soldby = users.username
                WHERE salesinfo.productcode LIKE ? OR products.productname LIKE ? 
                   OR users.fullname LIKE ?
                ORDER BY salesid DESC
                """;
            prepStatement = conn.prepareStatement(query);
            String searchPattern = "%" + text + "%";
            prepStatement.setString(1, searchPattern);
            prepStatement.setString(2, searchPattern);
            prepStatement.setString(3, searchPattern);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getPurchaseSearch(String text) {
        try {
            String query = """
                SELECT purchaseID, purchaseinfo.productcode, products.productname, 
                       purchaseinfo.quantity, purchaseinfo.totalcost, purchaseinfo.date
                FROM purchaseinfo 
                INNER JOIN products ON purchaseinfo.productcode = products.productcode
                WHERE purchaseinfo.productcode LIKE ? OR products.productname LIKE ?
                ORDER BY purchaseid DESC
                """;
            prepStatement = conn.prepareStatement(query);
            String searchPattern = "%" + text + "%";
            prepStatement.setString(1, searchPattern);
            prepStatement.setString(2, searchPattern);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== UTILITY METHODS ====================
    
    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
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
    
    // Update product including product code change
    public void updateProductWithCodeChange(ProductDTO productDTO, String oldProductCode) {
        try {
            conn.setAutoCommit(false);

            // Check if new product code already exists
            String checkQuery = "SELECT productcode FROM products WHERE productcode = ? AND productcode != ?";
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, productDTO.getProdCode());
            prepStatement.setString(2, oldProductCode);
            resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Product code already exists! Please use a different code.");
                conn.rollback();
                return;
            }

            // Update products table
            String query = "UPDATE products SET productcode = ?, productname = ?, costprice = ?, sellprice = ?, brand = ? WHERE productcode = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, productDTO.getProdCode());
            prepStatement.setString(2, productDTO.getProdName());
            prepStatement.setDouble(3, productDTO.getCostPrice());
            prepStatement.setDouble(4, productDTO.getSellPrice());
            prepStatement.setString(5, productDTO.getBrand());
            prepStatement.setString(6, oldProductCode);
            prepStatement.executeUpdate();

            // Update currentstock table with new product code
            String query2 = "UPDATE currentstock SET productcode = ? WHERE productcode = ?";
            prepStatement = conn.prepareStatement(query2);
            prepStatement.setString(1, productDTO.getProdCode());
            prepStatement.setString(2, oldProductCode);
            prepStatement.executeUpdate();

            // Update purchaseinfo table
            String query3 = "UPDATE purchaseinfo SET productcode = ? WHERE productcode = ?";
            prepStatement = conn.prepareStatement(query3);
            prepStatement.setString(1, productDTO.getProdCode());
            prepStatement.setString(2, oldProductCode);
            prepStatement.executeUpdate();

            // Update salesinfo table
            String query4 = "UPDATE salesinfo SET productcode = ? WHERE productcode = ?";
            prepStatement = conn.prepareStatement(query4);
            prepStatement.setString(1, productDTO.getProdCode());
            prepStatement.setString(2, oldProductCode);
            prepStatement.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(null, "Product updated with new code!");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating product: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources();
        }
    }
    
    public void debugCheckTables() {
    try {
        // Check if tables exist
        ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='currentstock'");
        if (rs.next()) {
            System.out.println("currentstock table EXISTS");
        } else {
            System.out.println("currentstock table DOES NOT EXIST - Please recreate database");
        }
        rs.close();
        
        // Check records in currentstock
        rs = statement.executeQuery("SELECT * FROM currentstock");
        while (rs.next()) {
            System.out.println("currentstock record: code=" + rs.getString("productcode") + ", qty=" + rs.getInt("quantity"));
        }
        rs.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
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