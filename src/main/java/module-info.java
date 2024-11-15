module com.example.Musicplus {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.musicplus to javafx.fxml;
    exports com.example.musicplus;


    opens com.example.musicplus.Controllers to javafx.fxml;
    exports com.example.musicplus.Controllers;
    //exports com.example.musicplus.Model;
    //exports com.example.musicplus.Controllers.Dashboard;
    opens com.example.musicplus.Database  to javafx.fxml;
    exports com.example.musicplus.Model;
    opens com.example.musicplus.Model to javafx.base, javafx.fxml;


}