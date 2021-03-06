package uk.ac.cam.gr3.weather.gui.controllers;

import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import uk.ac.cam.gr3.weather.Util;
import uk.ac.cam.gr3.weather.data.WeatherService;
import uk.ac.cam.gr3.weather.gui.util.FXMLController;
import uk.ac.cam.gr3.weather.gui.util.SwipeContainer;

public class FrameController extends FXMLController {

    private SwipeContainer container;

    private RotateTransition refreshSpinAnimation;

    public FrameController(WeatherService service) {
        super(service);
    }

    @FXML
    private AnchorPane swipeAnchor;

    @FXML
    private Button refreshButton;

    @FXML
    private SVGPath refreshSpinner;

    @FXML
    private Pane bottomNavigationSelection;

    @Override
    protected void initialize() {

        // An animation to make the refresh graphic spin around while it's running.
        refreshSpinAnimation = new RotateTransition(Duration.millis(500), refreshSpinner);
        refreshSpinAnimation.setByAngle(360);

        refreshSpinAnimation.setInterpolator(Interpolator.LINEAR);
    }

    public void setSwipeContainer(SwipeContainer container) {

        this.container = container;
        swipeAnchor.getChildren().setAll(this.container);

        Util.fitToAnchorPane(container);

        // Makes the bottom navigation bar indicator follow the swipe progress.
        bottomNavigationSelection.translateXProperty().bind(container.displayingNormalisedProperty().subtract(1).multiply(70));
    }

    @FXML
    private void showSnowReport() {

        container.setDisplaying(SwipeContainer.SNOW_REPORT);
    }

    @FXML
    private void showHomeScreen() {

        container.setDisplaying(SwipeContainer.HOME_SCREEN);
    }

    @FXML
    private void showWeeklyReport() {

        container.setDisplaying(SwipeContainer.WEEKLY_REPORT);
    }

    @FXML
    private void refresh() {

        refreshButton.setDisable(true);

        // Loops the animation when it finishes
        refreshSpinAnimation.setOnFinished(event -> refreshSpinAnimation.play());
        refreshSpinAnimation.playFromStart();

        // Create a task to be run in a worker thread on which the IO requests are made
        Task<Void> refresh = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                service.refresh();
                return null;
            }
        };

        // When the task finishes, wait until the animation finishes its cycle and re-enable the button.
        // Also, don't replay the animation, effectively stop it.
        refresh.setOnSucceeded(event -> {
            refreshSpinAnimation.setOnFinished(event1 -> refreshButton.setDisable(false));
        });
        // If it fails, print the exception and display an error dialog to the user.
        refresh.setOnFailed(event -> {
            refreshSpinAnimation.setOnFinished(event1 -> refreshButton.setDisable(false));
            Throwable exception = event.getSource().getException();
            System.err.println("Exception while refreshing");
            exception.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't refresh the data; got " + exception.getMessage());
            alert.showAndWait();
        });

        new Thread(refresh).start();
    }

    @Override
    public void update() {

    }
}
