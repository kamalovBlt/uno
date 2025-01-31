package ru.itis.uno.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.SignInRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.SignInResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.client.SessionManager;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

import java.util.Optional;


public class SignInPageController implements RootPaneAware {

    private Pane rootPane;
    private Client client;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Text error;

    @FXML
    public void handleSubmitButton() {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            this.error.setText("Username or password is empty");
        }

        ClientProtocolService clientProtocolService = client.getClientProtocolService();
        clientProtocolService.send(
                new Request(
                        RequestType.SIGN_IN,
                        new SignInRequestContent(username.getText(), password.getText())),
                client.getSocket());
        Optional<Response> optionalResponse = clientProtocolService.read(client.getSocket());
        if (optionalResponse.isPresent()) {
            Response response = optionalResponse.get();
            if (response.responseType().equals(ResponseType.SIGN_IN)) {
                SignInResponseContent content = (SignInResponseContent) response.content();

                SessionManager.saveId(content.getId());
                SessionManager.saveUsername(username.getText());
                Client.getInstance().setUsername(username.getText());
                Client.getInstance().setId(content.getId());

                rootPane.getChildren().clear();
                FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-profile.fxml", rootPane);

            }
            else {
                this.error.setText("Sign in failed, please try again");
            }
        }
        else {
            this.error.setText("Sign in Failed, please try again later");
        }
    }

    @FXML
    public void initialize() {
        this.client = Client.getInstance();
    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }
}
