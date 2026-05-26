/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.UI;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.inventory.DTO.UserDTO;
import javax.swing.*;

/**
 *
 * @author GKV
 */
public class TestDashboard {
    public static void main(String[] args) {
        // Set look and feel
        try {
            javax.swing.UIManager.setLookAndFeel(new FlatLightFlatIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create test user
        UserDTO testUser = new UserDTO();
        testUser.setUsername("admin");
        testUser.setFullName("Test Administrator");
        
        // Launch dashboard
        SwingUtilities.invokeLater(() -> {
            new Dashboard("admin", "ADMINISTRATOR", testUser);
        });
    }
}
