package com.themealdb;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class MainMenuForm extends JFrame {
    private JPanel MainMenuForm1;
    private JLabel lblHeadLine;
    private JButton btnMainMenu3;
    private JButton btnMainMenu2;
    private JButton btnMainMenu1;
    private JButton btnMainMenu4;

    public static void main(String[] args) throws SQLException {
        Main.createCountMeal();
        Main.createTable();
        //Main.drop("MEALS_COUNT or MEALS");//για την διαγραφή ενός εκ των δυο πινάκων τρέξτε το Main.drop με όρισμα MEALS_COUNT or MEALS
        new MainMenuForm();
    }

    public MainMenuForm(){

        setTitle("Meal's Information Application");
        setPreferredSize(new Dimension(600, 250));
        setContentPane(MainMenuForm1);
        ImageIcon imageIcon = new ImageIcon("resources/images/mealDB.png");
        setIconImage(imageIcon.getImage());
        lblHeadLine.setIcon(imageIcon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


        btnMainMenu1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MealDataViewForm();
            }
        });
        btnMainMenu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewByCategory();

            }
        });

        btnMainMenu3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MealsStatisticsForm();
                try {
                    Main.createChart();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnMainMenu4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // dispose();
            }
        });
    }

}
