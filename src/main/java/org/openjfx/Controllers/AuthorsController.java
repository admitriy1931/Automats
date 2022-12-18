package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;

public class AuthorsController {

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        setupButtonAsReturnToStart(backButton);
    }

}
