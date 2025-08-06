package BodyScan;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainPage extends Application {
    public static final Screen screen = Screen.getPrimary();
    public static final Rectangle2D bounds = screen.getBounds();
    public static final double width = bounds.getWidth(), height = bounds.getHeight() - 70;
    public static Scene scene;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🏠 Medical Classifier Home");

      
        Label welcomeLabel = new Label("Welcome to BodyScan AI");
        welcomeLabel.setFont(Font.font("Arial", 36));
        welcomeLabel.setTextFill(Color.DARKBLUE);

        
        StackPane titleContainer = new StackPane(welcomeLabel);
        titleContainer.setMaxWidth(600); 
        titleContainer.setPadding(new Insets(20));
        titleContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #1E90FF;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 15;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );

     
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(40);
        buttonGrid.setVgap(40);
        buttonGrid.setAlignment(Pos.CENTER);

    
        Button chestXrayButton = ButtonBuilder.build("Chest X-ray", "🫁");
        Button brainTumorButton = ButtonBuilder.build("Brain Tumor", "🧠");
        Button fractureButton = ButtonBuilder.build("Bone Fracture", "🦴");
        Button retinaButton = ButtonBuilder.build("Retina", "👁️");


        chestXrayButton.setOnAction(e -> new ChestXRay().start(primaryStage));
        brainTumorButton.setOnAction(e -> new BrainTumorPage().start(primaryStage));
        fractureButton.setOnAction(e -> new FractureDetection().start(primaryStage));
        retinaButton.setOnAction(e -> new DiabeticRetinopathyPage().start(primaryStage));

  
        buttonGrid.add(chestXrayButton, 0, 0);
        buttonGrid.add(brainTumorButton, 1, 0);
        buttonGrid.add(fractureButton, 0, 1);
        buttonGrid.add(retinaButton, 1, 1);


        VBox root = new VBox(50, titleContainer, buttonGrid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));


        scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
