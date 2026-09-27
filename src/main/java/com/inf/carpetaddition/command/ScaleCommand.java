/*
 * This file is part of Carpet-INF-Addition.
 *
 * Copyright (C) 2024 VulpeusServer and contributors.
 * Licensed under the GNU Lesser General Public License v3.0.
 */

package com.inf.carpetaddition.command;

import carpet.utils.CommandHelper;
import com.inf.carpetaddition.CarpetINFSettings;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * Player size commands migrated from the MCDR small plugin.
 *
 * /big
 * /big pro
 * /big pro max
 * /small
 * /small pro
 * /small pro max
 * /normal
 */
public final class ScaleCommand {
    private ScaleCommand() {}

    // Size presets. 1.0 is normal size.
    private static final double BIG = 1.5;
    private static final double BIG_PRO = 2.0;
    private static final double BIG_PRO_MAX = 3.0;

    private static final double SMALL = 0.3;
    private static final double SMALL_PRO = 0.2;
    private static final double SMALL_PRO_MAX = 0.1;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("normal")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandScale))
                .executes(context -> setScale(context.getSource().getPlayer(), 1.0, "normal")));

        dispatcher.register(CommandManager.literal("big")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandScale))
                .executes(context -> setScale(context.getSource().getPlayer(), BIG, "big"))
                .then(CommandManager.literal("pro")
                        .executes(context -> setScale(context.getSource().getPlayer(), BIG_PRO, "big pro"))
                        .then(CommandManager.literal("max")
                                .executes(context -> setScale(context.getSource().getPlayer(), BIG_PRO_MAX, "big pro max")))));

        dispatcher.register(CommandManager.literal("small")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandScale))
                .executes(context -> setScale(context.getSource().getPlayer(), SMALL, "small"))
                .then(CommandManager.literal("pro")
                        .executes(context -> setScale(context.getSource().getPlayer(), SMALL_PRO, "small pro"))
                        .then(CommandManager.literal("max")
                                .executes(context -> setScale(context.getSource().getPlayer(), SMALL_PRO_MAX, "small pro max")))));
    }

    private static int setScale(@Nullable ServerPlayerEntity player, double scale, String preset) {
        if (player == null) {
            return 0;
        }

        EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.SCALE);
        if (attribute == null) {
            player.sendMessage(Text.literal("无法修改玩家大小：minecraft:scale 属性不存在。"));
            return 0;
        }

        attribute.setBaseValue(scale);
        player.sendMessage(Text.literal("已设置大小为 " + scale + "x（" + preset + "）"));
        return 1;
    }
}
