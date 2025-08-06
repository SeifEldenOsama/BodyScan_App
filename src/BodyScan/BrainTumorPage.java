package BodyScan;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BrainTumorPage extends Application {

    private ImageView imageView = new ImageView();
    private File selectedFile;
    private final String API_URL = "http://127.0.0.1:5001/predict";
    @Override
    public void start(Stage stage) {
        stage.setTitle("🧠 Brain Tumor Detection");

        Label titleLabel = new Label("🧠 Brain Tumor Detection");
        titleLabel.setFont(Font.font("Segoe UI Emoji", 30));
        titleLabel.setTextFill(Color.DARKRED);

        Button uploadBtn = createButton("📁 Upload Image", "#8e44ad", "#732d91");
        uploadBtn.setOnAction(e -> chooseImage(stage));

        Button predictBtn = createButton("🔍 Predict", "#27ae60", "#1e8449");
        predictBtn.setOnAction(e -> {
            if (selectedFile != null) {
            	String message = sendImageForPrediction(selectedFile);
            	showResultStage("Prediction", message);
            } else {
                showResultStage("❗ Error", "Please upload an image first.");
            }
        });

        Button backBtn = createButton("⬅ Back", "#34495e", "#2c3e50");
        backBtn.setOnAction(e -> {
            try {
                new MainPage().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(20, uploadBtn, predictBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30, titleLabel, buttonBox, imageView, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setBackground(createBackground());
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        Scene scene = new Scene(layout, MainPage.width, MainPage.height);
        stage.setScene(scene);
        stage.show();
    }

    private void chooseImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Brain Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        }
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
        resultStage.setTitle(title);

        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font("Arial", 20));
        messageLabel.setTextFill(Color.DARKGREEN);

        Button closeBtn = createButton("❌ Close", "#e74c3c", "#c0392b");
        closeBtn.setOnAction(e -> resultStage.close());

        VBox layout = new VBox(20, messageLabel, closeBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, new CornerRadii(12), Insets.EMPTY)));
        layout.setBorder(new Border(new BorderStroke(Color.DARKORANGE, BorderStrokeStyle.SOLID, new CornerRadii(12), BorderWidths.DEFAULT)));

        Scene scene = new Scene(layout, 400, 200);
        resultStage.setScene(scene);

        FadeTransition ft = new FadeTransition(Duration.millis(700), layout);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        resultStage.show();
    }

    private Button createButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 16));
        button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-background-radius: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-background-radius: 10;"));
        return button;
    }

    private Background createBackground() {
        return new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY));
    }
}
