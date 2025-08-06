package BodyScan;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ButtonBuilder {
    public static Button build(String text, String emoji) {
        Button button = new Button(emoji + "\n" + text);
        Font emojiFont;
        try {
            emojiFont = Font.font("Segoe UI Emoji", 32);
        } catch (Exception e) {
            emojiFont = Font.font("Arial", 32);
        }

        button.setFont(emojiFont);
        button.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 16; " +
            "-fx-padding: 40;" 
        );
        button.setMinSize(300, 220);
        button.setWrapText(true);
        button.setAlignment(Pos.CENTER);
        
        button.setOnMouseEntered(ActionEvent -> {
        	button.setStyle(
                    "-fx-background-color: #3558db; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 16; " +
                    "-fx-padding: 40;" 
                );
        	MainPage.scene.setCursor(Cursor.HAND);
        });
        button.setOnMouseExited(ActionEvent -> {
        	button.setStyle(
                    "-fx-background-color: #3498db; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 16; " +
                    "-fx-padding: 40;" 
                );
        	MainPage.scene.setCursor(Cursor.DEFAULT);
        });

        return button;
    }
}
