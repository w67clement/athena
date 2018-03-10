package fr.tpe10.athena.gui.screens;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import fr.tpe10.athena.system.translate.GoogleTranslateEngine;
import fr.tpe10.athena.system.translate.TranslateEngine;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import fr.tpe10.athena.Athena;
import fr.tpe10.athena.system.LanguageManager;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ItemEvent;
import java.io.IOException;

// Nouvelle interface graphique
public class TranslateScreenV4 extends Screen
{
    private static final LanguageManager LANGUAGE_MANAGER = LanguageManager.getLanguageManager();
    private static final TranslateEngine TRANSLATE_ENGINE = new GoogleTranslateEngine();
    private static final Microphone      MICROPHONE       = new Microphone(FLACFileWriter.FLAC);
    private static final GSpeechDuplex   GDUPLEX          = new GSpeechDuplex("AIzaSyC1iynke3p5GQHuRT_5eFnZwB1TvlW6HG4");
    private CustomGSpeechListener listener;
    // Popup, permet d'ajouter des options lors du clique droit
    private final        JPopupMenu      popupMenu        = new JPopupMenu();
    private final        JMenuItem       selectAllButton  = new JMenuItem("Select All Text");
    private final        JMenuItem       copyTextButton   = new JMenuItem("Copy All Text");
    // Combo box: les listes de langues à choisir
    private JComboBox<String> inputBox;
    private JComboBox<String> outputBox;
    // Text areas
    private JTextArea         inputText;
    private JTextArea         outputText;
    // Buttons
    private JButton           resetButton;
    private JButton           translateButton;
    private JToggleButton     recordButton;
    // Labels
    private JLabel            statusText;

    @Override
    public void init()
    {
        setLayout(new BorderLayout());
        JPanel up = new JPanel();
        up.add(getInputBox());
        up.add(getOutputBox());
        add(up, BorderLayout.NORTH);
        // Center
        JPanel center = new JPanel();
        JScrollPane inputText = new JScrollPane(getInputText(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //inputText.setPreferredSize(new Dimension(320, 120));
        center.add(inputText);
        JScrollPane outputText = new JScrollPane(getOutputText(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //inputText.setPreferredSize(new Dimension(320, 120));
        center.add(outputText);
        add(center, BorderLayout.CENTER);
        // Buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.add(getResetButton());
        buttons.add(getRecordButton());
        buttons.add(getTranslateButton());
        JPanel down = new JPanel();
        down.setLayout(new GridLayout(2, 1));
        down.add(buttons);
        down.add(getStatusText());
        add(down, BorderLayout.SOUTH);
    }

    public JComboBox<String> getInputBox()
    {
        if (inputBox == null)
        {
            String[] langs = new String[LANGUAGE_MANAGER.getLanguages().size() + 1];
            System.arraycopy(LANGUAGE_MANAGER.getLanguagesArray(), 0, langs, 0, LANGUAGE_MANAGER.getLanguages().size());
            langs[LANGUAGE_MANAGER.getLanguages().size()] = "Auto";
            inputBox = new JComboBox<>(langs);
            inputBox.setSelectedItem("Auto");
            inputBox.setPreferredSize(new Dimension(250, 40));
        }
        return inputBox;
    }

    public JComboBox<String> getOutputBox()
    {
        if (outputBox == null)
        {
            outputBox = new JComboBox<>(LANGUAGE_MANAGER.getLanguagesArray());
            outputBox.setSelectedItem("Anglais");
            outputBox.setPreferredSize(new Dimension(250, 40));
        }
        return outputBox;
    }

    public JTextArea getInputText()
    {
        if (inputText == null)
        {
            inputText = new JTextArea(15, 25);
            inputText.setFont(Font.getFont("Segoe UI"));
            inputText.setLineWrap(true);
            inputText.setWrapStyleWord(true);
            inputText.setPreferredSize(new Dimension(200, 200));
            JMenuItem select = selectAllButton;
            select.addActionListener(e -> {
                inputText.setSelectionStart(0);
                inputText.setSelectionEnd(inputText.getText().length());
            });
            JMenuItem copy = copyTextButton;
            copy.addActionListener(e -> {
                StringSelection ss = new StringSelection(inputText.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            });
            JPopupMenu menu = popupMenu;
            menu.add(select);
            menu.add(copy);
            inputText.setComponentPopupMenu(menu);
            listener = new CustomGSpeechListener();
            inputText.getDocument().addDocumentListener(new DocumentListener()
            {
                @Override
                public void insertUpdate(DocumentEvent e)
                {

                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {

                }

                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    listener.old_text = inputText.getText();
                }
            });
            GDUPLEX.addResponseListener(listener);
        }
        return inputText;
    }

    public JTextArea getOutputText()
    {
        if (outputText == null)
        {
            outputText = new JTextArea(15, 25);
            outputText.setFont(Font.getFont("Segoe UI"));
            outputText.setLineWrap(true);
            outputText.setWrapStyleWord(true);
            outputText.setEditable(false);
            //outputText.setPreferredSize(new Dimension(200, 200));
            JMenuItem select = selectAllButton;
            select.addActionListener(e -> {
                outputText.setSelectionStart(0);
                outputText.setSelectionEnd(outputText.getText().length());
            });
            JMenuItem copy = copyTextButton;
            copy.addActionListener(e -> {
                StringSelection ss = new StringSelection(outputText.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            });
            JPopupMenu menu = popupMenu;
            menu.add(select);
            menu.add(copy);
            outputText.setComponentPopupMenu(menu);
        }
        return outputText;
    }

    public JButton getResetButton()
    {
        if (resetButton == null)
        {
            resetButton = new JButton("Réinisialiser");
            resetButton.addActionListener(e -> {
                inputText.setText("");
                outputText.setText("");
                listener.old_text = "";
            });
        }
        return resetButton;
    }

    @SuppressWarnings("unchecked")
    public JButton getTranslateButton()
    {
        if (translateButton == null)
        {
            translateButton = new JButton("Traduire");
            translateButton.setToolTipText("Appuyez pour traduire le texte.");
            translateButton.addActionListener(e -> {
                String text = inputText.getText();
                String outputLangCode = LANGUAGE_MANAGER.getCode(outputBox.getSelectedItem().toString());
                TRANSLATE_ENGINE.translate(text, LANGUAGE_MANAGER.getCode(inputBox.getSelectedItem().toString()), outputLangCode).ifPresent(t -> {
                    outputText.setText(t);
                    Athena.getAthena().getSynthesizerManager().doSynthAsync(outputLangCode, t);
                });
            });
        }
        return translateButton;
    }

    public JToggleButton getRecordButton()
    {
        if (recordButton == null)
        {
            recordButton = new JToggleButton("Enregistrer");
            recordButton.setToolTipText("Appuyez pour activer la reconnaissance vocale.");
            recordButton.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    String inputLangCode = LANGUAGE_MANAGER.getCode(inputBox.getSelectedItem().toString());
                    if (inputLangCode.equalsIgnoreCase("auto"))
                    {
                        getStatusText().setForeground(Color.RED);
                        getStatusText().setText("Veuillez sélectionner une langue d'entrée...");
                        return;
                    }
                    GDUPLEX.setLanguage(inputLangCode);
                    try
                    {
                        GDUPLEX.recognize(MICROPHONE.getTargetDataLine(), MICROPHONE.getAudioFormat());
                    }
                    catch (IOException e1)
                    {
                        getStatusText().setForeground(Color.RED);
                        getStatusText().setText("Une erreur est survenue pendant la reconnaissance!");
                    }
                    catch (LineUnavailableException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                else if (e.getStateChange() == ItemEvent.DESELECTED)
                {
                    getStatusText().setText("");
                    MICROPHONE.close();
                }
            });
        }
        return recordButton;
    }

    public JLabel getStatusText()
    {
        if (statusText == null)
        {
            statusText = new JLabel();
            statusText.setForeground(Color.BLACK);
        }
        return statusText;
    }

    public class CustomGSpeechListener implements GSpeechResponseListener
    {
        public String old_text = "";

        @Override
        public void onResponse(GoogleResponse gr)
        {
            String output = "";
            output = gr.getResponse();
            if (gr.getResponse() == null)
            {
                old_text = inputText.getText();
                if (old_text.contains("("))
                    old_text = old_text.substring(0, old_text.indexOf('('));

                old_text = (inputText.getText() + "\n");
                old_text = old_text.replace(")", "").replace("( ", "");
                inputText.setText(old_text);
                return;
            }
            if (output.contains("("))
                output = output.substring(0, output.indexOf('('));

            inputText.setText("");
            inputText.append(old_text);
            inputText.append(output);
        }
    }
}