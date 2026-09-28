package com.inf.carpetaddition.command;

import carpet.utils.CommandHelper;
import com.inf.carpetaddition.CarpetINFSettings;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public final class ScaleCommand {
    private ScaleCommand() {}

    private static final double BIG = 1.5;
    private static final double BIG_PRO = 2.0;
    private static final double BIG_PRO_MAX = 3.0;
    private static final double SMALL = 0.3;
    private static final double SMALL_PRO = 0.2;
    private static final double SMALL_PRO_MAX = 0.1;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("normal")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandScale))
                .executes(context -> setScale(context.getSource().getPlayer(), 1.0, "normal")));

        dispatcher.register(Commands.literal("big")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandScale))
                .executes(context -> setScale(context.getSource().getPlayer(), BIG, "big"))
                .then(Commands.literal("pro")
                        .executes(context -> setScale(context.getSource().getPlayer(), BIG_PRO, "big pro"))
                        .then(Commands.literal("max")
                                .executes(context -> setScale(context.getSource().getPlayer(), BIG_PRO_MAX, "big pro max")))));

        dispatcher.register(Commands.literal("small")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandScale))
                .executes(context -> setScale(context.getSource().getPlayer(), SMALL, "small"))
                .then(Commands.literal("pro")
                        .executes(context -> setScale(context.getSource().getPlayer(), SMALL_PRO, "small pro"))
                        .then(Commands.literal("max")
                                .executes(context -> setScale(context.getSource().getPlayer(), SMALL_PRO_MAX, "small pro max")))));
    }

    private static int setScale(@Nullable ServerPlayer player, double scale, String preset) {
        if (player == null) return 0;
        AttributeInstance attribute = player.getAttribute(Attributes.SCALE);
        if (attribute == null) {
            player.sendSystemMessage(Component.literal("无法修改玩家大小：minecraft:scale 属性不存在。"));
            return 0;
        }
        attribute.setBaseValue(scale);
        player.sendSystemMessage(Component.literal("已设置大小为 " + scale + "x（" + preset + "）"));
        return 1;
    }
}
