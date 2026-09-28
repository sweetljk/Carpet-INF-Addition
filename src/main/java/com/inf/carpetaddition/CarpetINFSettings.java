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

import static carpet.api.settings.RuleCategory.COMMAND;
import static carpet.api.settings.RuleCategory.SURVIVAL;

import carpet.api.settings.Rule;

public class CarpetINFSettings {
    private static final String INF = "inf";

    @Rule(categories = {SURVIVAL, COMMAND, INF})
    public static String commandHat = "ops";

    @Rule(categories = {SURVIVAL, COMMAND, INF})
    public static String commandSit = "ops";

    @Rule(categories = {SURVIVAL, COMMAND, INF})
    public static String commandScale = "ops";

    @Rule(categories = {SURVIVAL, INF})
    public static boolean visibleSpectators = false;
}
