/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.system.translate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.tpe10.athena.utils.AthenaUtils;
import org.aperlambda.lambdacommon.utils.Optional;
import org.aperlambda.lambdacommon.utils.OptionalString;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GoogleTranslateEngine implements TranslateEngine
{
    private static final String TRANSLATE_URL = "https://translate.googleapis.com/translate_a/single";
    private static final JsonParser JSON_PARSER = new JsonParser();

    @Override
    // Permet de traduire un texte dans une langue pour une autre
    public @NotNull OptionalString translate(String text, String inputLang, String outputLang)
    {
        Optional<URL> url = getURL(text.replace('"', '\''), inputLang, outputLang);
        if (!url.isPresent())
            return OptionalString.empty();
        OptionalString string = AthenaUtils.getTextFromURL(url.get());
        if (!string.isPresent())
            return OptionalString.empty();
        JsonElement json =  JSON_PARSER.parse(string.get());
        StringBuilder result = new StringBuilder();
        if (json instanceof JsonArray)
        {
            JsonArray uselessArray = json.getAsJsonArray();
            JsonArray jArray = uselessArray.get(0).getAsJsonArray();
            if (jArray == null)
                return OptionalString.empty();
            for (int i = 0; i < jArray.size(); i++)
            {
                JsonArray a = jArray.get(i).getAsJsonArray();
                result.append(a.get(0).getAsString());
            }
        }
        //String[] split = string.get().replace("[", "").replace("]", "").replace("\\n", "\n").split("\"");
        return OptionalString.ofNullable(result.toString());
    }

    @Override
    // permet de détecter la langue utilisé dans une texte
    public @NotNull OptionalString detectLanguage(String text)
    {
        return OptionalString.empty();
    }

    private Optional<URL> getURL(String text, String inputLang, String outputLang)
    {
        try
        {
            String encoded = URLEncoder.encode(text, "UTF-8");
            String string = TRANSLATE_URL +
                    "?client=gtx" +
                    "&sl=" + inputLang +
                    "&tl=" + outputLang +
                    "&dt=t" +
                    "&q=" + encoded;
            return Optional.of(new URL(string));
        }
        catch (UnsupportedEncodingException | MalformedURLException e)
        {
            return Optional.empty();
        }
    }
}