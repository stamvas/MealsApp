package com.themealdb;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ViewByCategory extends JFrame {
    private JPanel Panel1;
    private JLabel lblHeader;
    private JList list1;
    private JTextArea textArea2;
    private JList list2;
    private JLabel lblCategory;
    private JLabel label;

    public ViewByCategory() {


        setTitle("Meal's Information Application");
        setPreferredSize(new Dimension(600, 500));
        setContentPane(Panel1);
        ImageIcon imageIcon = new ImageIcon("resources/images/mealDB.png");
        setIconImage(imageIcon.getImage());
        label.setIcon(imageIcon);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        list1.setPreferredSize(new Dimension(150, 250));
        list2.setPreferredSize(new Dimension(300, 500));

        list1.setListData(Main.myListCategories().toArray(new String[0]));
        list1.setSelectedIndex(0);

        textArea2.setText("Selection  " + String.valueOf(list1.getSelectedIndex())
                + "  ---> " + String.valueOf(list1.getSelectedValue()));

        list2.setListData(Main.myListMeals(String.valueOf(list1.getSelectedValue())).toArray(new String[0]));


        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged (ListSelectionEvent e){

                textArea2.setText("Selection  " + String.valueOf(list1.getSelectedIndex())
                        + "  ---> " + String.valueOf(list1.getSelectedValue()));

                list2.setListData(Main.myListMeals(String.valueOf(list1.getSelectedValue())).toArray(new String[0]));

            }
        });

    }
}
