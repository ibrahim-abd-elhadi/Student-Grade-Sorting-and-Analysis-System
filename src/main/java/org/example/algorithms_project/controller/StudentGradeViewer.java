
package org.example.algorithms_project.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import javafx.stage.FileChooser;
import org.example.algorithms_project.HelloApplication;
import org.example.algorithms_project.model.Student;
import org.example.algorithms_project.model.algorithms.SortingManager;
import org.example.algorithms_project.utils.CSVReader;

import static org.example.algorithms_project.model.algorithms.SortResult.sortingProcess;


public class StudentGradeViewer {
    private final Stage primaryStage;
    private CriteriaType currentCriteria;

    // Modern blue color scheme
    private final Color PRIMARY_COLOR = Color.web("#2C3E50");
    private final Color SECONDARY_COLOR = Color.web("#1ABC9C");
    private final Color ACCENT_COLOR = Color.web("#E67E22");
    private final Color CARD_COLOR = Color.web("#FFFFFF");
    private final Color TABLE_ROW_EVEN = Color.web("#FFFFFF");
    private final Color TABLE_ROW_ODD = Color.web("#F5F9FF");
    private final Color TEXT_COLOR = Color.web("#2D3748");
    private final Color SUBTEXT_COLOR = Color.web("#5A6A85");

    private final TableView<Student> studentTable = new TableView<>();
    private final Button importButton = new Button("IMPORT");
    private final Button sortButton = new Button("SORT DATA");
    private final Label titleLabel = new Label("STUDENT GRADE VIEWER");
    private final Label statusLabel = new Label("Ready to import student data");
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private final Slider criteriaSlider;
    private final Label criteriaLabel;
    private final Button applyButton;
    private final CriteriaType[] criteriaValues;
    private File csvFile;

    public enum CriteriaType {
        NAME_ASCENDING("Name (A-Z)"),
        NAME_DESCENDING("Name (Z-A)"),
        GRADE_ASCENDING("Grade (Low to High)"),
        GRADE_DESCENDING("Grade (High to Low)"),
        PERFORMANCE_ASCENDING("Performance (Worst to Best)"),
        PERFORMANCE_DESCENDING("Performance (Best to Worst)");

        private final String displayName;

        CriteriaType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public StudentGradeViewer(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.criteriaValues = CriteriaType.values();

        // Initialize Criteria Selector components
        criteriaSlider = new Slider(0, criteriaValues.length - 1, 0);
        criteriaLabel = new Label(criteriaValues[0].toString());
        applyButton = new Button("APPLY CRITERIA");

        setupCriteriaSelector();
    }

    private void setupCriteriaSelector() {
        criteriaSlider.setMajorTickUnit(1);
        criteriaSlider.setMinorTickCount(0);
        criteriaSlider.setSnapToTicks(true);
        criteriaSlider.setShowTickMarks(true);
        criteriaSlider.setShowTickLabels(true);
        criteriaSlider.setPrefWidth(180);
        criteriaSlider.setStyle("-fx-control-inner-background: #E0E7FF; " +
                "-fx-tick-label-fill: " + toHex(TEXT_COLOR) + ";");

        criteriaLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; " +
                "-fx-font-size: 14; -fx-text-fill: " + toHex(TEXT_COLOR) + ";");

        applyButton.setStyle(
                "-fx-background-color: " + toHex(SECONDARY_COLOR) + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Segoe UI'; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13; " +
                        "-fx-padding: 8 16; " +
                        "-fx-background-radius: 20;"
        );

        applyButton.setOnMouseEntered(e -> {
            applyButton.setStyle(applyButton.getStyle().replace(
                    toHex(SECONDARY_COLOR), toHex(SECONDARY_COLOR.darker())));
        });
        applyButton.setOnMouseExited(e -> {
            applyButton.setStyle(applyButton.getStyle().replace(
                    toHex(SECONDARY_COLOR.darker()), toHex(SECONDARY_COLOR)));
        });

        criteriaSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            criteriaLabel.setText(criteriaValues[index].toString());
        });
    }

    public Scene createStudentScene() {
        setupUI();
        setupEventHandlers();
        BorderPane root = createMainLayout();
        Scene scene = new Scene(root, 1100, 750);
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }

    private void setupUI() {
        Font titleFont = Font.font("Segoe UI", FontWeight.BOLD, 28);
        Font headerFont = Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14);
        Font bodyFont = Font.font("Segoe UI", 14);

        titleLabel.setFont(titleFont);
        titleLabel.setTextFill(PRIMARY_COLOR);

        statusLabel.setFont(bodyFont);
        statusLabel.setTextFill(SUBTEXT_COLOR);

        setupTable();
        setupButtons();
        progressIndicator.setVisible(false);
    }

    private void setupTable() {
        TableColumn<Student, String> nameColumn = new TableColumn<>("STUDENT NAME");
        TableColumn<Student, Double> gradeColumn = new TableColumn<>("GRADE");
        TableColumn<Student, String> performanceColumn = new TableColumn<>("PERFORMANCE");

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty().asObject());
        performanceColumn.setCellValueFactory(cellData -> cellData.getValue().performanceProperty());

        String headerStyle = "-fx-font-family: 'Segoe UI'; -fx-font-size: 14; -fx-font-weight: bold; " +
                "-fx-text-fill: " + toHex(TEXT_COLOR) + "; -fx-alignment: CENTER_LEFT;";
        String cellStyle = "-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: " +
                toHex(TEXT_COLOR) + "; -fx-alignment: CENTER_LEFT;";

        nameColumn.setStyle(headerStyle);
        gradeColumn.setStyle(headerStyle);
        performanceColumn.setStyle(headerStyle);

        studentTable.getColumns().addAll(nameColumn, gradeColumn, performanceColumn);
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        studentTable.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>() {
                @Override
                protected void updateItem(Student item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setBackground(Background.EMPTY);
                    } else {
                        Color rowColor = getIndex() % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD;
                        setBackground(new Background(new BackgroundFill(
                                rowColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            };

            row.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered && !row.isEmpty()) {
                    row.setBackground(new Background(new BackgroundFill(
                            Color.web("#E3F2FD"), CornerRadii.EMPTY, Insets.EMPTY)));
                } else if (!row.isEmpty()) {
                    Color rowColor = row.getIndex() % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD;
                    row.setBackground(new Background(new BackgroundFill(
                            rowColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });

            return row;
        });

        Label placeholder = new Label("No student data available");
        placeholder.setFont(Font.font("Segoe UI", 14));
        placeholder.setTextFill(SUBTEXT_COLOR);
        studentTable.setPlaceholder(placeholder);
    }

    private Comparator<Student> getComparatorForCriteria(CriteriaType criteria) {
        switch (criteria) {
            case NAME_ASCENDING:
                return SortingManager.NAME_COMPARATOR;
            case NAME_DESCENDING:
                return SortingManager.NAME_COMPARATOR.reversed();
            case GRADE_ASCENDING:
                return SortingManager.GRADE_COMPARATOR;
            case GRADE_DESCENDING:
                return SortingManager.GRADE_COMPARATOR.reversed();
            case PERFORMANCE_ASCENDING:
                return SortingManager.PERFORMANCE_COMPARATOR;
            case PERFORMANCE_DESCENDING:
                return SortingManager.PERFORMANCE_COMPARATOR.reversed();
            default:
                return SortingManager.NAME_COMPARATOR; // Default case
        }
    }


    private void setupButtons() {
        String baseStyle =
                "-fx-font-family: 'Segoe UI'; " +
                        "-fx-font-weight: 600; " +
                        "-fx-font-size: 13px; " +
                        "-fx-padding: 8 16; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-min-width: 100; " +
                        "-fx-max-width: 100; " +
                        "-fx-min-height: 36; " +
                        "-fx-max-height: 36;";

        importButton.setStyle(baseStyle + "-fx-background-color: " + toHex(PRIMARY_COLOR) + "; -fx-text-fill: white;");
        sortButton.setStyle(baseStyle + "-fx-background-color: " + toHex(SECONDARY_COLOR) + "; -fx-text-fill: white;");


        setHoverEffect(importButton, PRIMARY_COLOR);
        setHoverEffect(sortButton, SECONDARY_COLOR);

    }

    private void setHoverEffect(Button button, Color baseColor) {
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(toHex(baseColor), toHex(baseColor.darker())));
            button.setEffect(new DropShadow(5, baseColor.darker()));
        });
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(toHex(baseColor.darker()), toHex(baseColor)));
            button.setEffect(null);
        });
    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E6F0FF, #B3D1FF);");
        root.setPadding(new Insets(20));

        // Header Section
        VBox header = new VBox(8, titleLabel, statusLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: " + toHex(CARD_COLOR) + "; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");

        // Left Panel (Sort Criteria)
        VBox leftPanel = new VBox(10);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(15));
        leftPanel.setPrefWidth(200);
        leftPanel.setStyle("-fx-background-color: " + toHex(CARD_COLOR) + "; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");

        Label criteriaTitle = new Label("SORT CRITERIA");
        criteriaTitle.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; " +
                "-fx-font-size: 16; -fx-text-fill: " + toHex(PRIMARY_COLOR) + ";");

        leftPanel.getChildren().addAll(criteriaTitle, criteriaSlider, criteriaLabel, applyButton);

        // Center Content (Table)
        StackPane centerPane = new StackPane();
        centerPane.setPadding(new Insets(0, 20, 20, 20));
        centerPane.getChildren().addAll(studentTable, progressIndicator);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        // Bottom Panel (Buttons)
        VBox bottomPanel = new VBox(20);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(20));
        bottomPanel.setStyle("-fx-background-color: " + toHex(CARD_COLOR) + "; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");

        HBox actionButtons = new HBox(20, importButton, sortButton);
        actionButtons.setAlignment(Pos.CENTER);

        bottomPanel.getChildren().addAll(actionButtons);

        root.setTop(header);
        root.setCenter(centerPane);
        root.setLeft(leftPanel);
        root.setBottom(bottomPanel);

        BorderPane.setMargin(leftPanel, new Insets(0, 0, 0, 20));
        BorderPane.setMargin(bottomPanel, new Insets(20, 0, 0, 0));

        return root;
    }


    private void setupEventHandlers() {
        importButton.setOnAction(event -> handleFileImport());
        sortButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/algorithms_project/DashboardView.fxml"));
                Parent dashboardRoot = loader.load();
                DashboardController controller = loader.getController();
                Scene dashboardScene = new Scene(dashboardRoot);
                primaryStage.setScene(dashboardScene);
                primaryStage.setTitle("Dashboard");

                CriteriaType selectedCriteria = getSelectedCriteria();
                Comparator<Student> comparator = getComparatorForCriteria(selectedCriteria);
                sortingProcess(csvFile, comparator);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        applyButton.setOnAction(event -> {
            currentCriteria = getSelectedCriteria();
            statusLabel.setText("Sort criteria set to: " + currentCriteria);
            statusLabel.setTextFill(PRIMARY_COLOR);
        });
    }

    private CriteriaType getSelectedCriteria() {
        int index = (int)criteriaSlider.getValue();
        return criteriaValues[index];
    }

    private void handleFileImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Student Data File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            progressIndicator.setVisible(true);
            statusLabel.setText("Processing: " + selectedFile.getName());

            new Thread(() -> {
                try {
                    List<Student> students = CSVReader.readStudents(selectedFile);

                    Platform.runLater(() -> {
                        studentTable.getItems().setAll(students);
                        statusLabel.setText("Loaded " + students.size() + " records");
                        progressIndicator.setVisible(false);
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Error loading file");
                        statusLabel.setTextFill(ACCENT_COLOR);
                        showErrorDialog(e.getMessage());
                        progressIndicator.setVisible(false);
                    });
                }
            }).start();
        }
    }

    private void showEmptySortPage() {
        VBox emptyPage = new VBox(20);
        emptyPage.setAlignment(Pos.CENTER);
        emptyPage.setPadding(new Insets(40));
        emptyPage.setStyle("-fx-background-color: linear-gradient(to bottom, #E6F0FF, #B3D1FF);");

        Label message = new Label("This page is reserved for future sorting functionality");
        message.setFont(Font.font("Segoe UI", 16));
        message.setTextFill(SUBTEXT_COLOR);


        emptyPage.getChildren().addAll(message);

        Scene emptyScene = new Scene(emptyPage, 1100, 750);
        primaryStage.setScene(emptyScene);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }
}