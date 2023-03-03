package com.themealdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MealsStatisticsForm extends JFrame {
    private JPanel MealsStatisticsPanel1;
    private JButton printButton;
    private JLabel label;
    private JLabel label1;

    public MealsStatisticsForm() {

        setTitle("Meal's Information Application");
        setPreferredSize(new Dimension(750, 600));
        setContentPane(MealsStatisticsPanel1);
        ImageIcon imageIcon = new ImageIcon("resources/images/mealDB.png");
        setIconImage(imageIcon.getImage());
        label.setIcon(imageIcon);
        ImageIcon imageIcon1 = new ImageIcon("export chart/Statistics.png");
        label1.setIcon(imageIcon1);
        label1.setSize(700,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.printToPdf();
                if (Main.check == 1)
                    JOptionPane.showMessageDialog(null, "Επιτυχής εκτύπωση!!");
            }
        });
    }

}
