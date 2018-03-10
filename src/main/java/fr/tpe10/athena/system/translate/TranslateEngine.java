/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.system.translate;

import org.aperlambda.lambdacommon.utils.OptionalString;
import org.jetbrains.annotations.NotNull;

public interface TranslateEngine
{
    @NotNull
    default OptionalString translate(String text, String outputLang)
    {
        return translate(text, "auto", outputLang);
    }

    @NotNull OptionalString translate(String text, String inputLang, String outputLang);

    @NotNull OptionalString detectLanguage(String text);
}