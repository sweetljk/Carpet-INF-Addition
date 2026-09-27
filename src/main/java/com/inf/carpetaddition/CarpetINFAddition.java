/*
 * This file is part of Carpet-INF-Addition.
 *
 * Portions of this project are derived from VulpeusCarpet:
 * https://github.com/Vulpeus-Server/vulpeus-carpet
 *
 * Copyright (C) 2024 VulpeusServer and contributors.
 * Licensed under the GNU Lesser General Public License v3.0.
 */

package com.inf.carpetaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.inf.carpetaddition.command.HatCommand;
import com.inf.carpetaddition.command.SitCommand;
import com.inf.carpetaddition.command.ScaleCommand;
import com.mojang.brigadier.CommandDispatcher;
import java.util.Map;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.fabricmc.api.ModInitializer;

public class CarpetINFAddition implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "carpet_inf_addition";
    public static final String ASSETS_PATH = "assets/" + MOD_ID;

    public static void loadExtension() {
        CarpetServer.manageExtension(new CarpetINFAddition());
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    @Override
    public void onInitialize() {
        loadExtension();
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetINFSettings.class);
    }

    @Override
    public void registerCommands(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess commandRegistryAccess
    ) {
        HatCommand.register(dispatcher);
        SitCommand.register(dispatcher);
        ScaleCommand.register(dispatcher);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Translations.getTranslationFromResourcePath(
                String.format("%s/lang/%s.json", ASSETS_PATH, lang)
        );
    }
}
