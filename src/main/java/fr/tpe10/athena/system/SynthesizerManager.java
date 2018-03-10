/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.system;

import com.darkprograms.speech.synthesiser.Synthesiser;
import fr.tpe10.athena.Athena;
import fr.tpe10.athena.utils.AthenaUtils;
import org.aperlambda.lambdacommon.utils.Optional;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import static org.fusesource.jansi.Ansi.ansi;

public class SynthesizerManager
{
    private final Synthesiser synth      = new Synthesiser();
    private final SynthThread asyncSynth = new SynthThread();
    private boolean init;

    public void init()
    {
        if (init)
            return;
        Athena.LOGGER.print(ansi().fgBrightGreen().a("Initializing ").fgCyan().a("Synthesizer Manager").fgBrightGreen().a("...").reset().toString());
        Thread thread = new Thread(asyncSynth);
        thread.setName("Async Synthesizer");
        thread.start();
        init = true;
    }

    // Permet de choisir la langue utilisée, c'est un code ISO en paramètre
    public void setLanguage(String lang)
    {
        if (lang == null)
        {
            if (synth.getLanguage() == null)
                synth.setLanguage("auto");
            return;
        }
        synth.setLanguage(lang);
    }

    // Permet de récupérer la ligne de donnée MP3 du texte choisi
    public Optional<InputStream> getSynth(String text)
    {
        try
        {
            return Optional.ofNullable(synth.getMP3Data(text));
        }
        catch (IOException e)
        {
            return Optional.empty();
        }
    }

    // Fait une synthèse du texte donnée
    public void doSynth(String text)
    {
        Optional<InputStream> oStream = getSynth(text);
        // SI le fichier a été récupérer alors on exécute..
        oStream.ifPresent(stream ->
                          {
                              try
                              {
                                  // La ligne audio encodée
                                  AudioInputStream encoded = AudioSystem.getAudioInputStream(new BufferedInputStream(stream));
                                  // Son format
                                  AudioFormat encodedFormat = encoded.getFormat();
                                  // Le même format audio mais décodée
                                  AudioFormat decodedFormat = AthenaUtils.decodeMP3Format(encodedFormat);

                                  // La ligne audio décodée
                                  AudioInputStream decoded = AudioSystem.getAudioInputStream(decodedFormat, encoded);

                                  // On utilise le moteur audio de Java pour jouer le son
                                  Clip clip = AudioSystem.getClip();
                                  clip.open(decoded);
                                  clip.start();
                              }
                              catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
                              {
                                  e.printStackTrace();
                              }
                          });
    }

    // Fait une synthèse mais on choisit en même temps la langue.
    public void doSynth(String lang, String text)
    {
        setLanguage(lang);
        doSynth(text);
    }

    // On fait une requête de synthèse asynchrone pour éviter de bloquer tout le programme.
    public void doSynthAsync(String lang, String text)
    {
        SynthState state = new SynthState(lang, text);
        asyncSynth.addSynth(state);
    }

    // On créé une pile d'éxécution de code supplémentaire pour éviter de bloquer l'exécution du programme lorsqu'on fait une synthèse.
    public class SynthThread implements Runnable
    {
        // La queue d'attente
        private final Queue<SynthState> synthQueue = new LinkedList<>();

        @Override
        public void run()
        {
            // C'est une boucle infinie
            while (true)
            {
                try
                {
                    synchronized (synthQueue)
                    {
                        SynthState state;

                        // Tant qu'on trouve quelque chose dans la queue, on l'utilise
                        while ((state = synthQueue.poll()) != null)
                        {
                            Athena.LOGGER.print(
                                    "[Async Synth] {\"lang\":\"" + state.lang + "\", \"text\":\"" + state.text + "\"}");
                            doSynth(state.lang, state.text);
                        }
                    }
                }
                catch (Exception e)
                {
                    // Une erreur est survenue, on recharge tout
                    Athena.LOGGER.error("[Async Synth] Error caught! (" + e.getMessage() + ")");
                    SynthesizerManager.this.init = false;
                    SynthesizerManager.this.init();
                }
            }
        }

        public void addSynth(SynthState text)
        {
            synchronized (synthQueue)
            {
                synthQueue.add(text);
            }
        }
    }

    private class SynthState
    {
        public String lang;
        public String text;

        public SynthState(String lang, String text)
        {
            this.lang = lang;
            this.text = text;
        }
    }
}