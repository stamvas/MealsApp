package com.themealdb;

import com.google.gson.*;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.*;
import java.io.File;
import java.util.List;


public class Main {
    public static int check = 0;


    public static String getJsonString(String url) {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            try {
                HttpResponse<String> response
                        = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //αναζήτηση κατηγορίας
        public static List<String> myListCategories() {
            String returnedDataCategories = getJsonString("https://www.themealdb.com/api/json/v1/1/categories.php");
            JsonObject jsonObjectCategories = JsonParser.parseString(returnedDataCategories).getAsJsonObject();

            List<String> arrayList1 = new ArrayList<>();

            if (jsonObjectCategories.get("categories").isJsonArray()) {
                JsonArray jsonArrayCategories = jsonObjectCategories.get("categories").getAsJsonArray();

                for (int i = 0; i < jsonArrayCategories.size(); i++) {
                    if (jsonArrayCategories.get(i).isJsonObject()) {
                        JsonObject jsonObjectCategories2 = jsonArrayCategories.get(i).getAsJsonObject();
                        String category = jsonObjectCategories2.get("strCategory").getAsString();
                        arrayList1.add(category);
                    }
                }
            }
            return arrayList1;
        }

        //αναζήτηση λίστας
        public static List<String> myListMeals(String cat) {
            String returnedDataMeals = getJsonString("https://www.themealdb.com/api/json/v1/1/filter.php?c=" + cat);
            JsonObject jsonObjectMeals = JsonParser.parseString(returnedDataMeals).getAsJsonObject();

            List<String> arrayList2 = new ArrayList<>();

            if (jsonObjectMeals.get("meals").isJsonArray()) {
                JsonArray jsonArrayMeals = jsonObjectMeals.get("meals").getAsJsonArray();

                for (int y = 0; y < jsonArrayMeals.size(); y++) {
                    if (jsonArrayMeals.get(y).isJsonObject()) {
                        JsonObject jsonObjectMeals2 = jsonArrayMeals.get(y).getAsJsonObject();
                        String meal = jsonObjectMeals2.get("strMeal").getAsString();
                        arrayList2.add(meal);
                    }
                }
            }

            return arrayList2;
        }

        //αναζήτηση γεύματος
    public static ArrayList<String> searchView(String search) {
        String returnedData = getJsonString("https://www.themealdb.com/api/json/v1/1/search.php?s=" + URLEncoder.encode(search));
        JsonObject jsonObject = JsonParser.parseString(returnedData).getAsJsonObject();
        ArrayList<String> arrayList = new ArrayList<>();
        if (jsonObject.get("meals").isJsonArray()) {
            JsonArray jsonArray = jsonObject.get("meals").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.get(i).isJsonObject()) {
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    String mealName = jsonObject1.get("strMeal").getAsString();
                    arrayList.add(mealName);
                    String mealCat = jsonObject1.get("strCategory").getAsString();
                    arrayList.add(mealCat);
                    String mealArea = jsonObject1.get("strArea").getAsString();
                    arrayList.add(mealArea);
                    String mealIns = jsonObject1.get("strInstructions").getAsString();
                    arrayList.add(mealIns);
                }
            }
        }
        return arrayList;
    }

    //αποθήκευση στη βάση
    public static void saveToDB(String name, String category, String area, String instructions){
        try {
            Connection con = connect();
            String insertSQL = "INSERT INTO MEALS(meal, category, area, instructions) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, category);
            preparedStatement.setString(3, area);
            preparedStatement.setString(4, instructions);
            int count = preparedStatement.executeUpdate();
            if(count>0){
                check = count;
                System.out.println(count+" saved");
            }
            else
                check = count;
            preparedStatement.close();
            con.close();
        } catch (SQLException ex) {

        }
    }

    //καταμέτρηση και αποθήκευση των γευμάτων
    public static void saveToCountMeal(ArrayList<String> st){
        try {
            Connection con = connect();
            String insertSQL = "INSERT INTO MEALS_COUNT(MEAL) VALUES(?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
            for (String s : st) {
                preparedStatement.setString(1, s);
                int count = preparedStatement.executeUpdate();

                if (count > 0) {
                    check = count;
                    System.out.println(count + " saved");
                } else
                    check = count;
            }
            preparedStatement.close();
            con.close();
        } catch (SQLException ex) {

        }
    }

    //βοηθητικό select

    /*public static String selectCountMeal() {
        String s ="";
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            String selectSQL = "SELECT * FROM MEALS_COUNT";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                s+=resultSet.getString("MEAL");
            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {

        }
        return s;
    }*/

    //βοηθητικό select

    /*public static String select() {
        String s ="";//SELECT tb1.CATEGORY, tb1.AREA, tb1.INSTRUCTIONS, tb2.MEAL FROM MEALS tb1 JOIN MEALS_COUNT tb2 ON tb1.MEAL = tb2.MEAL
        try {//SELECT MEAL, COUNT(*) AS total FROM MEALS_COUNT WHERE MEAL IN(SELECT MEAL FROM MEALS) GROUP BY MEAL ORDER BY total DESC
            Connection connection = connect();
            Statement statement = connection.createStatement();
            String selectSQL = "SELECT( SELECT COUNT(*) AS total " +
                    "FROM MEALS_COUNT mc " +
                    "WHERE mc.MEAL = tb1.MEAL) " +
                    "total , tb1.MEAL, tb1.CATEGORY, tb1.AREA, tb1.INSTRUCTIONS " +
                    "FROM MEALS tb1 ";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                s+=resultSet.getString("total")+resultSet.getString("MEAL")+"\n\n"+resultSet.getString("CATEGORY")+
                        resultSet.getString("AREA")+resultSet.getString("INSTRUCTIONS")+"\n";
            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {

        }
        return s;
    }*/




    //εμφάνιση της βάσης
    public static String selectAll(String st) {
        String s ="";
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            String selectSQL = "SELECT * FROM "+st;
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                s += ("["+resultSet.getInt("ID")+"]"+"\n"+resultSet.getString("MEAL")+"\n"+resultSet.getString("CATEGORY")
                        +"\n"+resultSet.getString("AREA") +"\n"+resultSet.getString("INSTRUCTIONS")+"\n\n\n\n");
            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {

        }
        return s;
    }

    //διαγραφή απο τη βάση
    public static void deleteMeal(String meal){
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM MEALS WHERE MEAL = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, meal);
            int count = preparedStatement.executeUpdate();
            if(count>0) {
                check = 1;
                System.out.println(count + " record deleted");
            }
            else
                check = 0;
            preparedStatement.close();
            connection.close();
            System.out.println("deleting");
        } catch (SQLException ex) {

        }
    }

    //επεξεργασία απο τη βάση
    public static void editMeal(int id, String meal, String cat, String area, String ins){
        try {
            Connection connection = connect();
            String update = "UPDATE MEALS SET " +
                    "MEAL = COALESCE(?,MEAL) , CATEGORY = COALESCE(?,CATEGORY) , AREA = COALESCE(?,AREA), INSTRUCTIONS = COALESCE(?,INSTRUCTIONS) WHERE ID = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, meal);
            preparedStatement.setString(2, cat);
            preparedStatement.setString(3, area);
            preparedStatement.setString(4, ins);
            preparedStatement.setInt(5, id);

            int count = preparedStatement.executeUpdate();
            if (count > 0) {
                check = 1;
                System.out.println(count + " editing");
            }
            else
                check = 0;
            preparedStatement.close();
            connection.close();
        }catch (SQLException ex){

        }
    }

    //εκτύπωση pdf
    public static void printToPdf(){
        try {
            String destFile = "printPDF/Meals.pdf";
            String destFont = "resources/fonts/ARIALUNI.TTF";
            File file = new File(destFile);
            file.getParentFile().mkdirs();

            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(destFile));
            Document doc = new Document(pdfDoc, PageSize.A4.rotate());

            float[] columnWidths = {1, 1, 5, 5, 5, 10};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));

            PdfFont f = PdfFontFactory.createFont(destFont, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            Cell cell = new Cell(1, 6)
                    .add(new Paragraph("ΣΤΑΤΙΣΤΙΚΑ ΓΕΥΜΑΤΩΝ"))
                    .setFont(f)
                    .setFontSize(15)
                    .setFontColor(DeviceGray.BLACK)
                    .setBackgroundColor(DeviceGray.WHITE)
                    .setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(cell);
            for (int i = 0; i < 1; i++) {
                Cell[] headerFooter = new Cell[]{
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("#"))
                                .setFont(f).setFontSize(14).setTextAlignment(TextAlignment.CENTER),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Δημοφιλή"))
                                .setFont(f).setFontSize(14).setTextAlignment(TextAlignment.CENTER),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Όνομα"))
                                .setFont(f).setFontSize(14).setTextAlignment(TextAlignment.CENTER),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Κατηγορία"))
                                .setFont(f).setFontSize(14).setTextAlignment(TextAlignment.CENTER),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Περιοχή"))
                                .setFont(f).setFontSize(14).setTextAlignment(TextAlignment.CENTER),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Οδηγίες"))
                                .setFont(f).setFontSize(14).setTextAlignment(TextAlignment.CENTER)
                };
                for (Cell hfCell : headerFooter) {
                    table.addHeaderCell(hfCell);
                }
            }

            Connection connection = connect();
            Statement statement = connection.createStatement();//join των 2 πινάκων
            String selectSQL = "SELECT tb2.total , tb1.MEAL, tb1.CATEGORY, tb1.AREA, tb1.INSTRUCTIONS " +
                    "FROM MEALS tb1 " +
                    "JOIN ( SELECT MEAL, COUNT(*) AS total " +
                    "FROM MEALS_COUNT " +
                    "GROUP BY MEAL) tb2 " +
                    "ON tb1.MEAL = tb2.MEAL " +
                    "ORDER BY total DESC ";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            int i=1;
            while (resultSet.next()) {
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(i++))));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(resultSet.getString("total"))))
                        .setFont(f);
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(resultSet.getString("MEAL"))))
                        .setFont(f);
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(resultSet.getString("CATEGORY"))))
                        .setFont(f);
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(resultSet.getString("AREA"))))
                        .setFont(f);
                table.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).add(new Paragraph(resultSet.getString("INSTRUCTIONS"))))
                        .setFont(f);
            }
            statement.close();
            connection.close();
            doc.add(table);
            doc.close();
            check = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //κατασκευή chart
    public static void createChart() throws SQLException {
        DefaultCategoryDataset chart_dataset = new DefaultCategoryDataset();
        Connection connection = connect();
        Statement statement = connection.createStatement();
        String sql = "SELECT tb2.total , tb1.MEAL, tb1.CATEGORY, tb1.AREA, tb1.INSTRUCTIONS " +
                "FROM MEALS tb1 " +
                "JOIN ( SELECT MEAL, COUNT(*) AS total " +
                "FROM MEALS_COUNT " +
                "GROUP BY MEAL) tb2 " +
                "ON tb1.MEAL = tb2.MEAL " +
                "ORDER BY total DESC ";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String category = resultSet.getString("MEAL");
                int marks = resultSet.getInt("total");
                chart_dataset.addValue(marks,"Γεύματα",category);
            }
            JFreeChart BarChartObject = ChartFactory.createBarChart
                    ("Στατιστικά γευμάτων","Γεύματα","Συχνότητα",
                            chart_dataset, PlotOrientation.VERTICAL,true,true,false);
            resultSet.close();
            statement.close();
            connection.close();
            int width=700; /* Width of the image */
            int height=500; /* Height of the image */
            File BarChart=new File("export chart/Statistics.png");
            ChartUtils ChartUtilities = null;
            ChartUtilities.saveChartAsPNG(BarChart,BarChartObject,width,height);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    //βοηθητικό drop

    public static void drop(String st) throws SQLException {
        try{
            Connection connection = connect();
            Statement statement = connection.createStatement();
            String delete = "DROP TABLE "+st;
            int count = statement.executeUpdate(delete);
            if(count>0){
                System.out.println(count+" record deleted");
            }
            statement.close();
            connection.close();
            System.out.println("Done drop");
        }
        catch (SQLException e){

        }
    }

    //δημιουργία πίνακα για καταμέτρηση γευμάτων
    public static void createCountMeal(){
        try {
            Connection connection = connect();
            String create = "CREATE TABLE MEALS_COUNT"
                    + "(MEAL VARCHAR(255))";
            Statement statement = connection.createStatement();
            statement.executeUpdate(create);
            statement.close();
            connection.close();
            System.out.println("Done countMEal");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    //δημιουργία πίνακα γευμάτων
    public static void createTable(){
        try {
            Connection connection = connect();
            String createTableSQL = "CREATE TABLE MEALS"
                    + "(ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
                    + "MEAL VARCHAR(255),"
                    + "CATEGORY VARCHAR(255),"
                    + "AREA VARCHAR(255),"
                    + "INSTRUCTIONS VARCHAR(15000))";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.close();
            connection.close();
            System.out.println("Done createTable");
            unique();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //διατηρεί στον πίνακα των γευμάτων τη μοναδικότητα του meal
    public static void unique(){
        try{
            Connection connection = connect();
            String uni = "CREATE UNIQUE INDEX MEAL ON MEALS (MEAL)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(uni);
            statement.close();
            connection.close();
            System.out.println("Done unique");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //σύνδεση και δημιουργία βάσης
    public static Connection connect() {
        String connectionString = "jdbc:derby:THEMEALDB;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex){

        }
        return connection;
    }
}


