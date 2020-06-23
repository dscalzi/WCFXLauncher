module wcfxlauncher {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires authlib;
    requires java.logging;
    requires gson;
    requires java.desktop;

    exports com.westeroscraft.nodes;
    exports com.westeroscraft.lib.borderless;
    opens com.westeroscraft.lib.borderless;
    opens com.westeroscraft;
}