package com.skriptide.guis.createserver;

import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import com.skriptide.util.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Liz3ga on 28.07.2016.
 */
public class CreateServerGuiController {

	File serverCustomFile = null;
	File scriptPluginVersionFile = null;
	@FXML
	private TextField serverNameTextField;
	@FXML
	private TextField serverPortTextField;
	@FXML
	private TextField serverPathTextField;
	@FXML
	private ComboBox<String> serverVersionComboBox;
	@FXML
	private Button customServerVersionBtn;
	@FXML
	private ComboBox<String> scriptVersionComboBox;
	@FXML
	private Button customScriptPluginVersionBtn;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private CheckBox createSubFolderCheckBox;
	@FXML
	private Button cancelBtn;
	@FXML
	private Button createServerBtn;
	@FXML
	private TextField startParameterTextField;
	@FXML
	private Label infoLabel;
	@FXML
	private Button choosePathBtn;
	private String truePath;

	public void updatePath() {

		serverPathTextField.setText(truePath + "/" + serverNameTextField.getText());


	}

	public void setValues() {


		serverNameTextField.setOnKeyReleased(event -> updatePath());
		choosePathBtn.setOnAction(event -> choosePath());
		cancelBtn.setOnAction(event -> cancel());
		createServerBtn.setOnAction(event -> createServer());
		customServerVersionBtn.setOnAction(event -> chooseServerVersion());
		customScriptPluginVersionBtn.setOnAction(event -> chooseScriptVersion());
		truePath = ConfigManager.getServersPath().substring(0, ConfigManager.getServersPath().length() - 1);

		serverPathTextField.setText(ConfigManager.getServersPath());
		scriptVersionComboBox.getItems().clear();
		serverVersionComboBox.getItems().clear();

		ObservableList<Skript> skVersions = Skript.getSkriptVersions();
		ObservableList<ServerVersion> srvVersions = ServerVersion.getServerVersions();

		for (Skript sk : skVersions.sorted()) {

			scriptVersionComboBox.getItems().add(sk.getVersion());
		}

		for (ServerVersion srv : srvVersions.sorted()) {
			serverVersionComboBox.getItems().add(srv.getVersion());
		}
		if(Main.debugMode) {
			System.out.println("Loaded values for create server");
		}

	}

	public void createServer() {

		boolean canGo = true;

		String error = "";


		ObservableList<Skript> skVersions = Skript.getSkriptVersions();
		ObservableList<ServerVersion> srvVersions = ServerVersion.getServerVersions();

		Skript trueSkript = null;
		ServerVersion trueVer = null;
		String selectedSkript = scriptVersionComboBox.getSelectionModel().getSelectedItem();
		if (selectedSkript == null || selectedSkript == "") {
			canGo = false;
			error = error + "Please choose a Skript Version! ";
		} else {
			for (Skript sk : skVersions.sorted()) {


				if (sk.getVersion().equals(selectedSkript)) {
					trueSkript = sk;
				}

			}
		}
		String selectedServer = serverVersionComboBox.getSelectionModel().getSelectedItem();
		if (selectedServer == null || selectedServer == "") {
			canGo = false;
			error = error + "Please choose a Server Version! ";
		} else {

			for (ServerVersion srv : srvVersions.sorted()) {

				if (srv.getVersion().equals(selectedServer)) {
					trueVer = srv;
				}
			}
		}

		try {
			Long.parseLong(serverPortTextField.getText());
		} catch (NumberFormatException ex) {
			canGo = false;
			error = error + "Port has to be a number! ";

		}
		if (serverNameTextField.getText().equals("")) {
			canGo = false;
			error = error + "Please choose a project name! ";

		}
		for (ServerVersion pr : srvVersions.sorted()) {
			if (pr.getName().equalsIgnoreCase(serverNameTextField.getText())) {

				error = error + "Name already taken!";
				infoLabel.setText(error);
				canGo = false;
				break;


			}
		}


		if (canGo) {
			MCServer server = new MCServer(serverNameTextField.getText(), Long.parseLong(serverPortTextField.getText()),
					serverPathTextField.getText(), trueVer, trueSkript, notesTextArea.getText(),
					startParameterTextField.getText());


			server.createServer();

			SceneManager.runninServerList.getSelectionModel().select(server.getname());

			Stage stage = (Stage) createServerBtn.getScene().getWindow();
			// do what you have to do
			stage.close();
			if(Main.debugMode) {

				System.out.println("sucessfzlly created Server, starting to load");
			}

		} else {
			infoLabel.setText(error);
			if(Main.debugMode) {

				System.out.println("Could not create server because: " + error);
			}
		}

	}


	public void cancel() {

		Stage stage = (Stage) createServerBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		if(Main.debugMode) {
			System.out.println("Cancel create server");
		}
	}

	public void choosePath() {

		Stage fileChooserWindow = new Stage();
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose Path for Projects");
		File dir = dirChooser.showDialog(fileChooserWindow);

		serverPathTextField.setText(dir.getAbsolutePath());

		truePath = dir.getAbsolutePath();
		if(Main.debugMode) {
			System.out.println("changed the save path");
		}
	}

	public void chooseServerVersion() {

		Stage fileChooserWindow = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooserWindow.setTitle("Choose Path for the server File");
		serverCustomFile = fileChooser.showOpenDialog(fileChooserWindow);

		ServerVersion.addServerVersion(serverCustomFile.getName(), VersionReader.getVersionOfServer(serverCustomFile), serverCustomFile);


		ObservableList<ServerVersion> srvVersions = ServerVersion.getServerVersions();

		serverVersionComboBox.getItems().clear();
		for (ServerVersion srv : srvVersions.sorted()) {
			serverVersionComboBox.getItems().add(srv.getVersion());
			if (srv.getVersion().equals(VersionReader.getVersionOfServer(serverCustomFile))) {
				serverVersionComboBox.getSelectionModel().select(srv.getVersion());
			}
		}
		if(Main.debugMode) {

			System.out.println("Set custom server version");
		}

	}

	public void chooseScriptVersion() {

		Stage fileChooserWindow = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooserWindow.setTitle("Choose Path for the Plugin File");
		scriptPluginVersionFile = fileChooser.showOpenDialog(fileChooserWindow);

		Skript.addScript(VersionReader.getNameOfPlugin(scriptPluginVersionFile),
				VersionReader.getVersionOfPlugin(scriptPluginVersionFile), scriptPluginVersionFile);
		ObservableList<Skript> srvVersions = Skript.getSkriptVersions();

		scriptVersionComboBox.getItems().clear();
		for (Skript srv : srvVersions.sorted()) {
			scriptVersionComboBox.getItems().add(srv.getVersion());
			if (srv.getVersion().equals(VersionReader.getVersionOfPlugin(scriptPluginVersionFile))) {
				scriptVersionComboBox.getSelectionModel().select(srv.getVersion());
			}
		}
		if(Main.debugMode) {

			System.out.println("custom skript version");
		}
	}

}

