/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collaborative.todo.list;

import DAOController.StarategyController;
import DAOController.ToDoController;
import DAOController.UserController;
import Entities.ToDoEntity;
import Entities.UserEntity;
import Utils.Constants;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Abd-Elmalek
 */
public class CollaborativeTodoList extends Application {

    Connection con;
    Statement stmt;
    PreparedStatement pStatment;
    ResultSet rs;
    String queryString;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        DriverManager.registerDriver(new SQLServerDriver());
        con = DriverManager.getConnection(Constants.DBUrl);
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
        UserEntity entity = new UserEntity();
        ToDoEntity doEntity = new ToDoEntity();
        UserController controller = new UserController();
        ToDoController doController = new ToDoController();
        StarategyController starategyController = new StarategyController(controller, entity);
        StarategyController starategyController2 = new StarategyController(doController, doEntity);
        starategyController.insert(entity);
        starategyController2.insert(doEntity);
       /*
         PreparedStatement stmt = con.prepareStatement("insert into " + Constants.TableNames.userTable + " values (?, ?, ?, ?, ?, ?)");
        stmt.setString(1, "125324");
        stmt.setString(2, "toto");
        stmt.setString(3, "soso");
        stmt.setString(4, "sosototo95");
        stmt.setString(5, "o95@gmail.com");
        stmt.setString(6, "52147895");
        stmt.executeUpdate();
        */
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
