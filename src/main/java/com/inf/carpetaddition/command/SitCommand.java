package com.inf.carpetaddition.command;

import carpet.utils.CommandHelper;
import com.inf.carpetaddition.CarpetINFSettings;
import com.inf.carpetaddition.sit.SitEntity;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.Nullable;

public final class SitCommand {
    private SitCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sit")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandSit))
                .executes(context -> sitPlayer(context.getSource().getPlayer())));
    }

    public static int sitPlayer(@Nullable ServerPlayer player) {
        if (player == null || !player.onGround()) return 0;

        ServerLevel level = player.level();
        ArmorStand armorStand = new ArmorStand(level, player.getX(), player.getY(), player.getZ());
        armorStand.setPos(player.getX(), player.getY() - 0.16, player.getZ());
        ((SitEntity) armorStand).carpetInf$setSitEntity(true);
        level.addFreshEntity(armorStand);

        player.setShiftKeyDown(false);
        player.startRiding(armorStand, true, true);
        return 1;
    }
}
