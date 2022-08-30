module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.common;
    exports org.openjfx;
    exports algorithms;
    exports automaton;
    exports org.openjfx.Controllers;
    opens org.openjfx.Controllers;
}