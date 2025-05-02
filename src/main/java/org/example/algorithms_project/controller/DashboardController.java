package org.example.algorithms_project.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
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
    // Clear previous data
    barChart.getData().clear();

    // Create a new series for the bar chart
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Performance Distribution");

    // Count the number of students per performance category
    long excellent = sortedStudents.stream().filter(s -> s.getPerformance() == Student.Performance.EXCELLENT).count();
    long good = sortedStudents.stream().filter(s -> s.getPerformance() == Student.Performance.GOOD).count();
    long poor = sortedStudents.stream().filter(s -> s.getPerformance() == Student.Performance.POOR).count();

    // Create Data
    XYChart.Data<String, Number> excellentData = new XYChart.Data<>("EXCELLENT", excellent);
    XYChart.Data<String, Number> goodData = new XYChart.Data<>("GOOD", good);
    XYChart.Data<String, Number> poorData = new XYChart.Data<>("POOR", poor);

    // Add data to series
    series.getData().addAll(excellentData, goodData, poorData);
    barChart.getData().add(series);

    // Make columns thinner
    barChart.setBarGap(5);
    barChart.setCategoryGap(30);

    // Wait until the nodes are added to the scene
    Platform.runLater(() -> {
        if (excellentData.getNode() != null)
            excellentData.getNode().setStyle("-fx-bar-fill: #2953c7;"); // Blue

        if (goodData.getNode() != null)
            goodData.getNode().setStyle("-fx-bar-fill: #1c7d86;"); // Green

        if (poorData.getNode() != null)
            poorData.getNode().setStyle("-fx-bar-fill: #F44336;"); // Red
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
        
      // sortedStudents=SortResult.getQuickSortedList;
        SortResult sortResult = new SortResult();  
        sortedStudents = sortResult.getQuickSortedList(); 
        setStatistics();
        setTime();
        setupScatterChart();
        setupBarChart();
        setTable();
    }

}
