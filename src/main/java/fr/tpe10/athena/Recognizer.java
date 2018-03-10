/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import net.sourceforge.javaflacencoder.FLACFileWriter;

public class Recognizer
{
    public static void main(String[] args)
    {
        /**
         * CODE DE TEST
         */
        Microphone mic = new Microphone(FLACFileWriter.FLAC);
        GSpeechDuplex duplex = new GSpeechDuplex(args[0]);
        duplex.setLanguage(com.darkprograms.speech.recognizer.Recognizer.Languages.FRENCH.toString());
        // Adds the listener
        duplex.addResponseListener(gr -> {
            System.out.println("Google pense que vous avez dit: " + gr.getResponse());
            System.out.println("avec " +
                    ((gr.getConfidence()!=null)?(Double.parseDouble(gr.getConfidence())*100):null)
                    + "% de confidence.");
            System.out.println("Google pense aussi que vous avez dit:"
                    + gr.getOtherPossibleResponses());
        });
        //mic.open();//Optional Duplex Will Open the Mic if not already open
        try{
            duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());//Recognition done here.
            //duplex.
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
