/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.utils;

import org.apache.logging.log4j.Logger;

import static org.fusesource.jansi.Ansi.ansi;

public class AthenaLogger
{
    private final Logger logger;

    public AthenaLogger(Logger logger)
    {
        this.logger = logger;
    }

    public void print(String message)
    {
        logger.info(message);
    }

    public void print(Object message)
    {
        logger.info(message);
    }

    public void error(String message)
    {
        error(message, new Object[] {});
    }

    public void error(String message, Object ... objects)
    {
        logger.error(ansi().fgBrightRed().a(message).reset().toString(), objects);
    }

    public Logger getLogger()
    {
        return logger;
    }
}