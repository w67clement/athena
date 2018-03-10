/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguageManager
{
    private static final LanguageManager INSTANCE = new LanguageManager();
    private HashMap<String, String> codeToLang = new HashMap<>();
    private HashMap<String, String> langToCode = new HashMap<>();

    private LanguageManager()
    {
        init();
    }

    public static LanguageManager getLanguageManager()
    {
        return INSTANCE;
    }

    // Récupère la liste des langues
    public List<String> getLanguages()
    {
        return new ArrayList<>(langToCode.keySet());
    }

    public String[] getLanguagesArray()
    {
        List<String> langs = getLanguages();
        String[] langsArray = new String[langs.size()];
        for (int i = 0; i < langs.size(); i++)
            langsArray[i] = langs.get(i);
        return langsArray;
    }

    private void init()
    {
        // Ajoute toutes les langues.
        addLang("fr", "Français");
        addLang("en-us", "Anglais");
        addLang("de", "Allemand");
        addLang("ja", "Japonais");
        addLang("es", "Espagnol");
        addLang("zh", "Chinois");
    }

    // Ajoute une langue à partir de son code ISO et du nom afficher sur l'interface graphique
    private void addLang(String code, String lang)
    {
        codeToLang.put(code, lang);
        langToCode.put(lang, code);
    }

    // Récupère le nom de la langue à partir du code ISO
    public String getLang(String code)
    {
        String lang = codeToLang.get(code);
        if (lang == null)
            return "Anglais";
        return lang;
    }

    // Récupère le code ISO à partir de la langue
    public String getCode(String lang)
    {
        if (lang.equalsIgnoreCase("Auto"))
            return "auto";
        String code = langToCode.get(lang);
        if (code == null)
            return "en-us";
        return code;
    }
}