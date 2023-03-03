package com.themealdb;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MealDataViewForm extends JFrame{
    private JPanel MealDataViewPanel1;

    private  JTextField searchField;
    private JButton searchBtn;
    private JButton saveBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JTextArea viewData;
    private JButton savedList;
    private JTextField countMeals;
    private JLabel label;
    private ArrayList viewArray;
    private ArrayList<String> mealName =new ArrayList<>();

    public MealDataViewForm() {

        setTitle("Meal's Information Application");
        setPreferredSize(new Dimension(750, 500));
        setContentPane(MealDataViewPanel1);
        ImageIcon imageIcon = new ImageIcon("resources/images/mealDB.png");
        setIconImage(imageIcon.getImage());
        label.setIcon(imageIcon);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);



        //αναζήτηση γεύματος
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewArray = Main.searchView(searchField.getText()); //φέρνει τα δεδομένα της αναζήτησης
                if(!viewData.equals("")) //αν το text δεν είναι κενό το αδειάζει
                    viewData.setText("");
                if(viewArray.isEmpty())
                    JOptionPane.showMessageDialog(null, "Δεν υπάρχουν δεδομένα.\nΚάντε αναζήτηση ξανά");
                for(Object o: viewArray) { // εκτύπωση στο text area
                    viewData.append(o + "\n");
                }
                int count = 0;
                String s="";
                for (int i=0; i<viewArray.size(); i+=4) {
                    count++;
                    s=viewArray.get(i).toString();
                    mealName.add(s);
                }
                Main.saveToCountMeal(mealName);
                mealName.clear();
                if (count == 0) {
                    countMeals.setText("Δεν Βρέθηκαν γεύματα");
                }
                else if (count == 1) {
                    countMeals.setText("Βρέθηκε " + count + " γεύμα");
                }
                else {
                    countMeals.setText("Βρέθηκαν " + count + " γεύματα");
                }

            }
        });

        //αποθήκευση
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewArray.toString().split(",",4);
                if(viewArray.size()<5) {
                    Main.saveToDB(viewArray.get(0).toString(), viewArray.get(1).toString(), viewArray.get(2).toString(), viewArray.get(3).toString());
                    if (Main.check != 1) {
                        JOptionPane.showMessageDialog(null, "Η εγγραφή υπάρχει ήδη.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Επιτυχής αποθήκευση!!");
                    }
                    return;
                }
                JOptionPane.showMessageDialog(null, "Παρακαλώ αποθηκεύστε ένα γεύμα τη φορά.");
            }
        });

        //επεξεργασία λίστας
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    JTextField id = new JTextField(10);
                    JTextField meal = new JTextField(10);
                    JTextField cat = new JTextField(10);
                    JTextField area = new JTextField(10);
                    JTextField ins = new JTextField(10);
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("ID"));
                    id.setToolTipText("Το ID μπορείτε να το βρείτε πατώντας 'Εμφάνιση λίστας'.");
                    panel.add(id);
                    panel.add(Box.createHorizontalStrut(3));
                    panel.add(new JLabel("Όνομα"));
                    panel.add(meal);
                    panel.add(Box.createHorizontalStrut(3));
                    panel.add(new JLabel("Κατηγορία"));
                    panel.add(cat);
                    panel.add(Box.createHorizontalStrut(3));
                    panel.add(new JLabel("Περιοχή"));
                    panel.add(area);
                    panel.add(Box.createHorizontalStrut(3));
                    panel.add(new JLabel("Οδηγίες"));
                    panel.add(ins);
                    panel.add(Box.createHorizontalStrut(3));
                    int result = JOptionPane.showConfirmDialog(null, panel,
                            "Παρακαλώ συμπληρώστε τα πεδία για τροποποίηση. (Το πεδίο ID δεν μπορεί να είναι κενό!!!)", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        int intID = Integer.parseInt(id.getText());
                        String m = meal.getText();
                        String c = cat.getText();
                        String a = area.getText();
                        String i = ins.getText();
                        if (m.equals(""))
                            m=null;
                        if (c.equals(""))
                            c=null;
                        if (a.equals(""))
                            a=null;
                        if (i.equals(""))
                            i=null;
                        int rsl = JOptionPane.showConfirmDialog(null, "Θέλετε να συνεχίσετε;", "",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (rsl == JOptionPane.YES_OPTION) {
                            Main.editMeal(intID, m, c, a, i);
                            if (Main.check != 1)
                                JOptionPane.showMessageDialog(null, "Δεν έγινε κάποια επεξεργασία.");
                            else
                                JOptionPane.showMessageDialog(null, "Επιτυχής επεξεργασία.");
                        }
                    }
            }
        });

        //εμφάνιση λίστας
        savedList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = Main.selectAll("MEALS");
                viewData.setText(s+"\n");
                if (Objects.equals(s, ""))
                    JOptionPane.showMessageDialog(null, "Η λίστα είναι κενή.");
            }
        });

        //διαγραφή γεύματος
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField meal = new JTextField(58);
                JPanel panel = new JPanel();
                panel.add(new JLabel("Όνομα"));
                panel.add(meal);
                int result = JOptionPane.showConfirmDialog(null,panel,
                        "Παρακαλώ συμπληρώστε το όνομα του γεύματος που θέλετε να διαγράψετε όπως ακριβώς αυτό είναι αποθηκευμένο.", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION)
                    meal.getText();
                else return;
                int rsl = JOptionPane.showConfirmDialog(null, "Θέλετε να συνεχίσετε;", "",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (rsl == JOptionPane.YES_OPTION)
                    Main.deleteMeal(meal.getText());
                if (Main.check != 1)
                    JOptionPane.showMessageDialog(null, "Δεν έγινε κάποια διαγραφή.");
                else
                    JOptionPane.showMessageDialog(null, "Επιτυχής διαγραφή.");

            }

        });
    }
}
