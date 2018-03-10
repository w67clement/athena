/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.utils;

import fr.tpe10.athena.Athena;
import org.aperlambda.lambdacommon.utils.OptionalString;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class AthenaUtils
{
    // Créé le format audio décodé à partir du format encodé
    public static AudioFormat decodeMP3Format(AudioFormat format)
    {
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,  // Encoding to use
                format.getSampleRate(),           // sample rate (same as base format)
                16,               // sample size in bits (thx to Javazoom)
                format.getChannels(),             // # of Channels
                format.getChannels() * 2,           // Frame Size
                format.getSampleRate(),           // Frame Rate
                false                 // Big Endian
        );
    }

    // Récupère le contenu d'une page web à partir d'une URL
    public static OptionalString getTextFromURL(URL url)
    {
        StringBuilder result = new StringBuilder();
        try
        {
            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent",
                                       "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            Reader reader = new InputStreamReader(urlConn.getInputStream(),
                                                  "utf-8");
            BufferedReader br = new BufferedReader(reader);
            int byteRead;
            while ((byteRead = br.read()) != -1)
            {
                result.append((char) byteRead);
            }
            return OptionalString.of(result.toString());
        }
        catch (IOException e)
        {
            return OptionalString.empty();
        }
    }

    // Demande à Java "Swing" d'utiliser le style graphique du système.
    public static void useSystemLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Throwable ignored)
        {
            try
            {
                Athena.LOGGER.error("Your java failed to provide normal look and feel, trying the old fallback now.. ");
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                System.out.println("Done.");
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }
}