package org.example.algorithms_project.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.text.Font;
import org.example.algorithms_project.model.GradeStatistics;
import org.example.algorithms_project.model.Student;
import org.example.algorithms_project.model.algorithms.SortResult;
import org.example.algorithms_project.utils.CSVWriter;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button down;
    @FXML
    private Button back;

    @FXML
    private TableColumn<Student, Double> grade;

    @FXML
    private Text maxGrade;

    @FXML
    private Text mean;

    @FXML
    private Text median;

    @FXML
    private Text mergeMem;

    @FXML
    private Text mergeTime;

    @FXML
    private Text minGrade;

    @FXML
    private TableColumn<Student, String> name;

    @FXML
    private Text numOfStud;

    @FXML
    private TableColumn<Student, String> perfor;

    @FXML
    private Text quickMem;

    @FXML
    private Text quickTime;

    @FXML
    private Text radixMem;

    @FXML
    private Text radixTime;

    @FXML
    private ScatterChart<Number, Number> scatter;

    @FXML
    private Text stand;

    @FXML
    private TableView<Student> table;

    @FXML
    private Text var;

    @FXML
    private Text mode;

    List<Student> sortedStudents ;
    @FXML
    void backtomain(ActionEvent event) {
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Create a new instance of StudentGradeViewer and set the scene
        StudentGradeViewer studentView = new StudentGradeViewer(currentStage);
        Scene mainScene = studentView.createStudentScene();

        currentStage.setScene(mainScene);
        currentStage.setTitle("Student Grade Viewer");
    }

    @FXML
    void downLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fileChooser.setTitle("Choose file path");
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            CSVWriter.writeStudentsToCSV(file.getAbsolutePath(), sortedStudents);
            System.out.println("file exported successfully to : " + file.getAbsolutePath());
        }
    }

    public void setStatistics(){

        numOfStud.setText(String.valueOf(GradeStatistics.getTotalNumber(sortedStudents)));
        maxGrade.setText(String.valueOf(GradeStatistics.getMaxGrade(sortedStudents)));
        minGrade.setText(String.valueOf(GradeStatistics.getMinGrade(sortedStudents)));
        mean.setText(String.format("%.2f", GradeStatistics.getMean(sortedStudents)));
        median.setText(String.format("%.2f", GradeStatistics.getMedian(sortedStudents)));
        stand.setText(String.format("%.2f", GradeStatistics.getStandaredDeviation(sortedStudents)));
        var.setText(String.format("%.2f", GradeStatistics.getVariance(sortedStudents)));
        mode.setText(String.format("%.2f", GradeStatistics.getMode(sortedStudents)));
    }
    public void setTime(){
        quickMem.setText(String.valueOf(SortResult.getQuickMemoryTime()));
        quickTime.setText(String.valueOf(SortResult.getQuickSortTime()));
        mergeMem.setText(String.valueOf(SortResult.getMergeMemoryTime()));
        mergeTime.setText(String.valueOf(SortResult.getMergeSortTime()));
        radixMem.setText(String.valueOf(SortResult.getRadixMemoryTime()));
        radixTime.setText(String.valueOf(SortResult.getRadixSortTime()));
    }


    @FXML
    private void setupBarChart() {
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Grade Distribution");

        // Create 20 bins for 5-point intervals (0-5, 5-10, ..., 95-100)
        int[] gradeBins = new int[20];

        for (Student s : sortedStudents) {
            int grade = (int) s.getGrade();
            int binIndex = Math.min(grade / 5, 19); // Handle 100 as last bin
            gradeBins[binIndex]++;
        }

        // Create data points with proper range labels
        for (int i = 0; i < gradeBins.length; i++) {
            int lowerBound = i * 5;
            int upperBound = (i + 1) * 5;
            String rangeLabel = (i == 19) ? "95-100" :
                    String.format("%d-%d", lowerBound, upperBound);
            series.getData().add(new XYChart.Data<>(rangeLabel, gradeBins[i]));
        }

        // Configure chart appearance
        barChart.getData().add(series);
        barChart.setBarGap(0);       // Remove space between bars
        barChart.setCategoryGap(0);  // Remove space between categories

        // CSS Styling
        barChart.setStyle("-fx-category-gap: 0; -fx-bar-gap: 0;");

        Platform.runLater(() -> {
            // Apply uniform styling and width
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node bar = data.getNode();
                if (bar != null) {
                    bar.setStyle("-fx-bar-fill: #2953c7;"
                            + "-fx-background-radius: 0 0 0 0;" +"-fx-bar-width: 20px;\n" +
                            "    -fx-background-radius: 0;"); // Square edges
                }
            }

            barChart.widthProperty().addListener((obs, oldVal, newVal) -> {
                double barWidth = newVal.doubleValue() / gradeBins.length - 2;
                barChart.lookupAll(".chart-bar").forEach(node ->
                        node.setStyle("-fx-max-width: " + barWidth + "px;"));
            });

            // Configure axis labels
            CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
            xAxis.setTickLabelRotation(-45);  // Diagonal labels
            xAxis.setTickLabelFont(Font.font(8)); // Smaller font
        });
    }

    
        
private void setupScatterChart() {
    // Set axis labels
    NumberAxis xAxis = (NumberAxis) scatter.getXAxis();
    xAxis.setLabel("Student Index");

    NumberAxis yAxis = (NumberAxis) scatter.getYAxis();
    yAxis.setLabel("Grade");

    // Prepare data series
    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName("Student Grades");

    for (int i = 0; i < sortedStudents.size(); i++) {
        Student student = sortedStudents.get(i);
        XYChart.Data<Number, Number> data = new XYChart.Data<>(i + 1, student.getGrade());
        series.getData().add(data);
    }

    scatter.getData().add(series);

    // Reduce dot size after nodes are rendered
    Platform.runLater(() -> {
        for (XYChart.Data<Number, Number> data : series.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-background-radius: .5px; -fx-padding: .5px;");
            }
        }
    });
}

        private void setTable(){
            name.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
            grade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getGrade()).asObject());
            perfor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPerformance().toString()));
            setStudentList(sortedStudents);
        }
        public void setStudentList(List<Student> sorStudents){
            ObservableList<Student > observableList=FXCollections.observableArrayList(sorStudents);
            table.setItems(observableList);
        }
      
      
       
    @FXML
    public void initialize() {

        sortedStudents = SortResult.getQuickSortedList();
        if (sortedStudents == null) {
            sortedStudents = new ArrayList<>();
        }

        setStatistics();
        setTime();
        setupScatterChart();
        setupBarChart();
        setTable();
    }

}
