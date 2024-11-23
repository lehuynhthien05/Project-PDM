package com.example.parkingbookingsystems;

import com.example.parkingbookingsystems.Controller.Statistics;
import com.example.parkingbookingsystems.Controller.StatisticsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StatisticsController {

    @FXML
    private ImageView adminImage;

    @FXML
    private AnchorPane analyticsBar;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private AnchorPane earningTrend;

    @FXML
    private Label greetAdmin;

    @FXML
    private HBox headerAnalytic;

    @FXML
    private Label nameAdmin;

    @FXML
    private AnchorPane orderTrend;

    @FXML
    private Button returnHome;

    @FXML
    private Label revenueEarning;

    @FXML
    private Label revenueOrders;

    @FXML
    private Label revenueUsers;

    @FXML
    private Label totalEarning;

    @FXML
    private Label totalOrders;

    @FXML
    private Label totalUsers;

    @FXML
    private HBox trend;

    @FXML
    private AnchorPane userTrend;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private StatisticsService statisticsService = new StatisticsService();

    @FXML
    public void initialize() {
        xAxis.setLabel("Category");
        yAxis.setLabel("Value");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Statistics");

        for (Statistics stats : statisticsService.getStatisticsData()) {
            series.getData().add(new XYChart.Data<>("Total Order", statisticsService.countTotalBookings()));
            series.getData().add(new XYChart.Data<>("Total Price", statisticsService.SumOfPrice()));
            series.getData().add(new XYChart.Data<>("Total User",  statisticsService.TotalUser()));
        }

        barChart.getData().add(series);

        initializeTotalOrder();
        initializeEarning();
        initializeTotalUser();
    }


    @FXML
    public void initializeTotalOrder() {
        int total = statisticsService.countTotalBookings();
        totalOrders.setText(String.valueOf(total));
    }
    @FXML
    public void initializeEarning() {
        float total = statisticsService.SumOfPrice();
        totalEarning.setText(String.valueOf(total));
    }

    @FXML
    public void initializeTotalUser() {
        int total = statisticsService.TotalUser();
        totalUsers.setText(String.valueOf(total));
    }

    public void returnToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminProfile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) returnHome.getScene().getWindow();
            currentStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
