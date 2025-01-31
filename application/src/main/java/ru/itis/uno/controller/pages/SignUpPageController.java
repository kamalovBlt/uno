package ru.itis.uno.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.SignUpRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.SignUpResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.client.SessionManager;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

import java.util.Optional;

public class SignUpPageController implements RootPaneAware {

    @FXML
    private PasswordField passwordConfirm;
    @FXML
    private Text error;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;

    private Pane rootPane;
    private Client client;


    @FXML
    public void initialize() {
        client = Client.getInstance();
    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

    @FXML
    public void handleSubmitButton() {
        if (username.getText().isEmpty() ||
                password.getText().isEmpty() ||
                passwordConfirm.getText().isEmpty()) {
            this.error.setText("Please fill all the fields");
        }
        else if (!password.getText().equals(passwordConfirm.getText())) {
            this.error.setText("Passwords do not match");
        }
        else {
            ClientProtocolService clientProtocolService = new ClientProtocolService();
            clientProtocolService.send(
                    new Request(
                            RequestType.SIGN_UP,
                            new SignUpRequestContent(username.getText(), password.getText())
                    ),
                    client.getSocket()
            );
            Optional<Response> optionalResponse = clientProtocolService.read(client.getSocket());
            if (optionalResponse.isPresent()) {
                Response response = optionalResponse.get();
                if (response.responseType().equals(ResponseType.SIGN_UP)) {
                    SignUpResponseContent signUpResponseContent = (SignUpResponseContent) response.content();
                    SessionManager.saveId(signUpResponseContent.getId());
                    SessionManager.saveUsername(username.getText());
                    Client.getInstance().setUsername(username.getText());
                    Client.getInstance().setId(signUpResponseContent.getId());

                    rootPane.getChildren().clear();
                    FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-profile.fxml", rootPane);

                }
                else {
                    this.error.setText("Sign up failed, please try again");
                }
            }
            else {
                this.error.setText("Sign in failed, please try again");
            }
        }
    }
}
