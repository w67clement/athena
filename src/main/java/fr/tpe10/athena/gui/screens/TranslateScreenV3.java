/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.gui.screens;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import fr.tpe10.athena.system.translate.GoogleTranslateEngine;
import fr.tpe10.athena.system.translate.TranslateEngine;
import javafx.fxml.FXML;
import fr.tpe10.athena.Athena;
import fr.tpe10.athena.system.LanguageManager;
import org.jetbrains.annotations.NotNull;

// Ancienne interface graphique

public class TranslateScreenV3 //extends Screen<BorderPane>
{
    private static final LanguageManager LANGUAGE_MANAGER = LanguageManager.getLanguageManager();
    private static final String[]        LANGUAGES        = new String[]{"fr", "en-us", "de", "ja"};
    private static final TranslateEngine TRANSLATE_ENGINE = new GoogleTranslateEngine();

    @FXML
    private JFXButton repeatButton;
    @FXML
    private JFXButton resetButton;
    @FXML
    private JFXButton translateButton;

    @FXML
    private JFXComboBox<String> inputBox;
    @FXML
    private JFXComboBox<String> outputBox;

    @FXML
    private JFXTextArea inputText;
    @FXML
    private JFXTextArea outputText;

    @FXML
    private JFXProgressBar progressBar;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize()
    {
        repeatButton.setText("\uD83D\uDD01 Répéter");

        progressBar.setProgress(0.0);

        //@TODO Put the lang manager
        String[] langs = new String[LANGUAGE_MANAGER.getLanguages().size() + 1];
        System.arraycopy(LANGUAGE_MANAGER.getLanguagesArray(), 0, langs, 0, LANGUAGE_MANAGER.getLanguages().size());
        langs[LANGUAGE_MANAGER.getLanguages().size()] = "Auto";
        inputBox.getItems().addAll(langs);
        inputBox.setValue("Auto");
        outputBox.getItems().addAll(LANGUAGE_MANAGER.getLanguagesArray());
        outputBox.setValue("Anglais");

        // We don't want make the output text area editable.
        outputText.setEditable(false);

        resetButton.setOnMouseClicked(e ->
                                      {
                                          inputText.setText("");
                                          outputText.setText("");
                                      });

        translateButton.setOnMouseClicked(e ->
                                          {
                                              progressBar.setProgress(-1);
                                              String text = inputText.getText();
                                              String outputLangCode = LANGUAGE_MANAGER.getCode(outputBox.getValue());
                                              TRANSLATE_ENGINE.translate(text, LANGUAGE_MANAGER.getCode(inputBox.getValue()), outputLangCode).ifPresent(t ->
                                                                                                                                                        {
                                                                                                                                                            progressBar.setProgress(100);
                                                                                                                                                            outputText.setText(t);
                                                                                                                                                            Athena.getAthena().getSynthesizerManager().doSynthAsync(outputLangCode, t);
                                                                                                                                                            progressBar.setProgress(-1);
                                                                                                                                                        });
                                              progressBar.setProgress(0.0);
                                          });

        repeatButton.setOnMouseClicked(e -> {
        });
    }

    //@Override
    public @NotNull String getName()
    {
        return "translate_screen_v3";
    }
}