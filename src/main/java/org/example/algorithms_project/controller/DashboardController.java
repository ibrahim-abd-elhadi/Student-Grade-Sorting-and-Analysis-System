package org.example.algorithms_project.controller;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.example.algorithms_project.model.Student;
import org.example.algorithms_project.model.algorithms.SortResult;
import org.example.algorithms_project.model.GradeStatistics;

import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.algorithms_project.utils.CSVWriter;

public class DashboardController {

    // FXML Fields
    @FXML private Text numOfStud;
    @FXML private Text maxGrade;
    @FXML private Text minGrade;
    @FXML private Text mean;
    @FXML private Text median;
    @FXML private Text stand;
    @FXML private Text var;

    @FXML private Text quickMem;
    @FXML private Text quickTime;
    @FXML private Text mergeMem;
    @FXML private Text mergeTime;
    @FXML private Text radixMem;
    @FXML private Text radixTime;

    @FXML private Button down;

    @FXML private ScatterChart<Number, Number> scatter;
    @FXML private BarChart<String, Number> barChart;

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, String> name;
    @FXML private TableColumn<Student, Double> grade;
    @FXML private TableColumn<Student, String> perfor;

    private List<Student> sortedStudents;

    // Initialize method
    @FXML
    private void initialize() {
        // Set up TableView column bindings
        name.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        grade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getGrade()).asObject());
        perfor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPerformance().toString()));

        // Prepare charts to avoid FXML animation glitches
        scatter.getData().clear();
        barChart.getData().clear();
        scatter.setAnimated(false);
        barChart.setAnimated(false);
    }

    // Download button action
    @FXML
    private void downLoad(ActionEvent event) {
       FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fileChooser.setTitle("Choose file path");
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            CSVWriter.writeStudentsToCSV(file.getAbsolutePath(), sortedStudents);
            System.out.println("file exported successfully to : " + file.getAbsolutePath());
        }
    }

    // Setup the dashboard with sorting result and statistics
    public void setupDashboard(SortResult sortResult, GradeStatistics stats) {
        this.sortedStudents = sortResult.getQuickSortedList();

        setStatistics(stats);
        setupScatterChart();
        setupBarChart();
        populateTable();
        setSortMetrics(sortResult);
    }

    // Set statistics data
    private void setStatistics(GradeStatistics stats) {
        numOfStud.setText(String.valueOf(stats.getTotalNumber(sortedStudents)));
        maxGrade.setText(String.valueOf(stats.getMaxGrade(sortedStudents)));
        minGrade.setText(String.valueOf(stats.getMinGrade(sortedStudents)));
        mean.setText(String.format("%.2f", stats.getMean(sortedStudents)));
        median.setText(String.format("%.2f", stats.getMedian(sortedStudents)));
        stand.setText(String.format("%.2f", stats.getStandaredDeviation(sortedStudents)));
        var.setText(String.format("%.2f", stats.getVariance(sortedStudents)));
    }

    // Set sorting metrics (memory/time for sorting algorithms)
    private void setSortMetrics(SortResult sortResult) {
        // Uncomment and implement once memory/time values are available
        
        quickMem.setText(String.valueOf(sortResult.getQuickMemoryTime()));
        quickTime.setText(String.valueOf(sortResult.getQuickSortTime()));
        mergeMem.setText(String.valueOf(sortResult.getMergeMemoryTime()));
        mergeTime.setText(String.valueOf(sortResult.getMergeSortTime()));
        radixMem.setText(String.valueOf(sortResult.getRadixMemoryTime()));
        radixTime.setText(String.valueOf(sortResult.getRadixSortTime()));
        
    }

    // Setup scatter chart with student performance
    private void setupScatterChart() {
        NumberAxis xAxis = (NumberAxis) scatter.getXAxis();
        xAxis.setLabel("Grade");

        NumberAxis yAxis = (NumberAxis) scatter.getYAxis();
        yAxis.setLabel("Performance");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (Student student : sortedStudents) {
            int performanceY = student.getPerformance().ordinal(); // EXCELLENT=0, GOOD=1, POOR=2
            series.getData().add(new XYChart.Data<>(student.getGrade(), performanceY));
        }

        scatter.getData().add(series);
    }

    // Setup bar chart to visualize performance distribution
    private void setupBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        long excellent = sortedStudents.stream().filter(s -> s.getPerformance() == Student.Performance.EXCELLENT).count();
        long good = sortedStudents.stream().filter(s -> s.getPerformance() == Student.Performance.GOOD).count();
        long poor = sortedStudents.stream().filter(s -> s.getPerformance() == Student.Performance.POOR).count();

        series.getData().add(new XYChart.Data<>("EXCELLENT", excellent));
        series.getData().add(new XYChart.Data<>("GOOD", good));
        series.getData().add(new XYChart.Data<>("POOR", poor));

        barChart.getData().add(series);
    }

    // Populate the TableView with sorted students
    private void populateTable() {
        ObservableList<Student> studentData = FXCollections.observableArrayList(sortedStudents);
        table.setItems(studentData);
    }
}
