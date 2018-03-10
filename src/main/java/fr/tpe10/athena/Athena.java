/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena;

import fr.tpe10.athena.gui.screens.TranslateScreenV4;
import fr.tpe10.athena.utils.AthenaLogger;
import fr.tpe10.athena.utils.AthenaUtils;
import fr.tpe10.athena.system.SynthesizerManager;
import org.apache.logging.log4j.LogManager;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.awt.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static org.fusesource.jansi.Ansi.ansi;

public class Athena
{
    public static final AthenaLogger LOGGER = new AthenaLogger(LogManager.getLogger("Athena"));
    private static Athena   ATHENA;
    private final  long     startTime;
    private final  String[] args;
    private SynthesizerManager synthesizerManager = new SynthesizerManager();
    private GraphicsDevice     device             = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    public Athena(String[] args)
    {
        ATHENA = this;
        startTime = System.currentTimeMillis();
        this.args = args;
    }

    // Initialise le programme
    public void load()
    {
        // Load ANSI console.
        AnsiConsole.systemInstall();
        LOGGER.print(ansi().fgBrightGreen().a("Loading ").fgBrightMagenta().a("Athena").fgBrightGreen().a("...").reset().toString());
        LOGGER.print(ansi().fgBrightGreen().a("Loading ").fgBrightBlue().a("Screen Manager").fgBrightGreen().a("...").reset().toString());
        AthenaUtils.useSystemLookAndFeel();
        synthesizerManager.init();

        // Création de la fenêtre
        JFrame frame = new JFrame();
        // Titre de la fenêtre
        frame.setTitle("Athena");
        // Quitte le programme si on ferme la fenêtre
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 480));
        // Petit code pour mettre la fenêtre en plein écran
        device.setFullScreenWindow(frame);

        frame.pack();
        // Interface graphique
        TranslateScreenV4 screen = new TranslateScreenV4();
        screen.init();
        frame.setContentPane(screen);
        frame.setVisible(true);
    }

    public @NotNull SynthesizerManager getSynthesizerManager()
    {
        return synthesizerManager;
    }

    // Point de départ du programme
    public static void main(String[] args)
    {
        Athena athena = new Athena(args);
        athena.load();
    }

    public static Athena getAthena()
    {
        return ATHENA;
    }
}