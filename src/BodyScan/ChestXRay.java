package BodyScan;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChestXRay extends Application {

    private File selectedImageFile;
    private final String API_URL = "http://127.0.0.1:5000/predict";

    @Override
    public void start(Stage stage) {
        stage.setTitle("🫁 Chest X-Ray Detection");

        Label titleLabel = new Label("🫁 Chest X-Ray Detection");
        titleLabel.setFont(Font.font("Segoe UI Emoji", 30));
        titleLabel.setTextFill(Color.DARKBLUE);

        Button uploadBtn = createButton("📁 Upload Image", "#8e44ad", "#732d91");
        Button predictBtn = createButton("🔍 Predict", "#2ecc71", "#27ae60");
        Button backBtn = createButton("⬅ Back", "#e67e22", "#d35400");

        HBox buttonBox = new HBox(20, uploadBtn, predictBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);

        uploadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select an Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            selectedImageFile = fileChooser.showOpenDialog(stage);
            if (selectedImageFile != null) {
                Image image = new Image(selectedImageFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300);
                imageView.setPreserveRatio(true);
                imageContainer.getChildren().clear();
                imageContainer.getChildren().add(imageView);
            }
        });

        predictBtn.setOnAction(e -> {
            if (selectedImageFile != null) {
                String result = sendImageForPrediction(selectedImageFile);
                if (result != null) {
                    showResultStage("Prediction Result", result);
                } else {
                    showResultStage("Error", "❌ Could not connect to server or invalid response.");
                }
            } else {
                showResultStage("No Image", "⚠ Please upload an image first.");
            }
        });

        backBtn.setOnAction(e -> new MainPage().start(stage));

        VBox layout = new VBox(30, titleLabel, buttonBox, imageContainer, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setEffect(new javafx.scene.effect.DropShadow(5, Color.LIGHTGRAY));
        layout.setBackground(createBackground());

        Scene scene = new Scene(layout, MainPage.width, MainPage.height);
        stage.setScene(scene);
        stage.show();
    }

    private Background createBackground() {
        return new Background(new BackgroundFill(
                new Color(0.95, 0.95, 1.0, 1), CornerRadii.EMPTY, Insets.EMPTY));
    }

    private Button createButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 16));
        button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-background-radius: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-background-radius: 10;"));
        return button;
    }

    private String sendImageForPrediction(File imageFile) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String boundary = "Boundary-" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream outputStream = conn.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(imageFile.getName()).append("\"\r\n");
            writer.append("Content-Type: image/jpeg\r\n\r\n");
            writer.flush();

            FileInputStream inputStream = new FileInputStream(imageFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append("\r\n").flush();
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString().replace("\"", "").replace("{", "").replace("}", "").replace("prediction:", "").trim();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showResultStage(String title, String message) {
        Stage resultStage = new Stage();
        resultStage.initStyle(StageStyle.TRANSPARENT);

        Label msgLabel = new Label(message);
        msgLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        msgLabel.setTextFill(Color.DARKGREEN);

        Button closeBtn = new Button("Close");
        closeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        closeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        closeBtn.setOnAction(e -> resultStage.close());

        VBox vbox = new VBox(20, msgLabel, closeBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: white; -fx-border-color: #2ecc71; -fx-border-width: 2px; -fx-padding: 20; -fx-background-radius: 10;");
        vbox.setOpacity(0);

        Scene scene = new Scene(vbox);
        scene.setFill(Color.TRANSPARENT);
        resultStage.setScene(scene);
        resultStage.centerOnScreen();

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), vbox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        resultStage.show();
    }
}
