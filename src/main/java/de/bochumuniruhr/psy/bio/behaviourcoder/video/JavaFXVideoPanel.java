package de.bochumuniruhr.psy.bio.behaviourcoder.video;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.media.*;
import javafx.stage.Stage;

public class JavaFXVideoPanel extends JFXPanel {

	public void start() throws Exception {
		final MediaPlayer oracleVid = new MediaPlayer(
				new Media("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv"));
		
		setScene(new Scene(new Group(new MediaView(oracleVid)), 540, 208));
		//stage.show();
		oracleVid.play();
	}

}
