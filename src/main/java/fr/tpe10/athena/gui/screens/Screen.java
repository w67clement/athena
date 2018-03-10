/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.gui.screens;

import org.aperlambda.lambdacommon.utils.Nameable;

import javax.swing.*;

// Code pour l'ancienne interface graphique

public abstract class Screen extends JPanel implements Nameable
{
    public abstract void init();
}