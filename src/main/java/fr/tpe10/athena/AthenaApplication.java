/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena;

import javafx.application.Application;
import javafx.stage.Stage;

// Code mort utilisé avec JavaFX
public class AthenaApplication extends Application
{
    private static boolean useApplication = false;

    public static void main(String[] args)
    {
        useApplication = true;
        Athena.main(args);
        launch(args);
    }

    public static boolean useApplication()
    {
        return useApplication;
    }

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage)
    {
        /*Athena.getAthena().application = Optional.of(this);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Athena");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(new Pane(), 800, 480));
        primaryStage.getScene().getStylesheets().add(Athena.class.getResource("/themes/athena_dark_theme.css").toString());

        primaryStage.setOnCloseRequest(e -> Athena.getAthena().exit(0));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        Athena.getAthena().getScreenManager().show("loading_screen");
        //loadCrashScreen("GladOS crashed");

        primaryStage.show();

        String language = "fr";
        String text = "Bonjour, je suis l'assistante de Traduction Athena!";
        Athena.getAthena().getSynthesizerManager().doSynthAsync(language, text);
        Athena.getAthena().getScreenManager().show("translate_screen_v3");
        /*Synthesiser synth = new Synthesiser(language);
        try
        {
            InputStream is = synth.getMP3Data(text);

            AudioInputStream encoded = AudioSystem.getAudioInputStream(new BufferedInputStream(is));

            AudioFormat encodedFormat = encoded.getFormat();
            AudioFormat decodedFormat = AthenaUtils.decodeMP3Format(encodedFormat);

            AudioInputStream decoded = AudioSystem.getAudioInputStream(decodedFormat, encoded);

            Clip clip = AudioSystem.getClip();
            clip.open(decoded);
            clip.start();
            Athena.getAthena().getScreenManager().show("translate_screen_v3");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }
}