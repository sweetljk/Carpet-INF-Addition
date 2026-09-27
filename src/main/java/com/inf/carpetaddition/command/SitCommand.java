/*
 * This file is part of Carpet-INF-Addition.
 *
 * Portions of this file are derived from VulpeusCarpet:
 * https://github.com/Vulpeus-Server/vulpeus-carpet
 *
 * Copyright (C) 2024 VulpeusServer and contributors.
 * Licensed under the GNU Lesser General Public License v3.0.
 */

package com.inf.carpetaddition.command;

import carpet.utils.CommandHelper;
import com.inf.carpetaddition.CarpetINFSettings;
import com.inf.carpetaddition.sit.SitEntity;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public final class SitCommand {
    private SitCommand() {}

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sit")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandSit))
                .executes(context -> sitPlayer(context.getSource().getPlayer())));
    }

    public static int sitPlayer(@Nullable ServerPlayerEntity player) {
        if (player == null || !player.isOnGround()) {
            return 0;
        }

        ServerWorld world = player.getEntityWorld();
        ArmorStandEntity armorStand = new ArmorStandEntity(
                world,
                player.getX(),
                player.getY() - 0.16,
                player.getZ()
        );

        ((SitEntity) armorStand).carpetInf$setSitEntity(true);
        world.spawnEntity(armorStand);

        player.setSneaking(false);
        player.startRiding(armorStand);
        return 1;
    }
}
