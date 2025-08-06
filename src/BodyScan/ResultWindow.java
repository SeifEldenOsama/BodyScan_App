package BodyScan;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ResultWindow {
    public static void show(String title, String resultText) {
        Stage resultStage = new Stage();
        resultStage.initModality(Modality.APPLICATION_MODAL);
        resultStage.setTitle(title);

        Label resultLabel = new Label(resultText);
        resultLabel.setFont(Font.font("Verdana", 28));
        resultLabel.setTextFill(Color.WHITE);
        resultLabel.setWrapText(true);

        Button closeButton = new Button("Close");
        closeButton.setFont(Font.font(18));
        closeButton.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-background-radius: 10;");
        closeButton.setOnAction(e -> resultStage.close());

        VBox layout = new VBox(30, resultLabel, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setEffect(new DropShadow(15, Color.BLACK));

        Scene scene = new Scene(layout, 500, 300);
        resultStage.setScene(scene);
        resultStage.showAndWait();
    }
}
