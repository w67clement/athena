/*
 * Copyright © 2018 AperLambda <aper.entertainment@gmail.com>
 *
 * This file is part of athena.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package fr.tpe10.athena.gui;


// CODE MORT

public class ScreenManager
{
    /*private final HashMap<String, Screen<?>> SCREENS = new HashMap<>();

    public void init()
    {
        addScreen("crash_screen", CrashScreen.class);
        addScreen("loading_screen", LoadingScreen.class);
        //addScreen("translate_screen_v3", TranslateScreenV3.class);
    }

    public <T extends Screen> boolean addScreen(String name, Class<T> screen)
    {
        if (SCREENS.containsKey(name))
            return false;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Athena.class.getResource("/screens/" + name + ".fxml"));
        try {
            loader.load();
        }
        catch (IOException e) {
            Athena.LOGGER.error(e.toString());
            return false;
        }
        T controller = loader.getController();
        if (!controller.getClass().equals(screen))
            return false;
        SCREENS.put(name, controller);
        return true;
    }

    public Optional<Screen<?>> show(String name)
    {
        Optional<Screen<?>> oScreen = Optional.ofNullable(SCREENS.get(name));
        oScreen.ifPresent(screen -> Athena.getAthena().getApplication().ifPresent(app -> app.getPrimaryStage().getScene().setRoot(screen.getRoot())));
        return oScreen;
    }*/
}