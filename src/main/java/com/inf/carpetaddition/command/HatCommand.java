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
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public final class HatCommand {
    private HatCommand() {}

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("hat")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandHat))
                .executes(context -> hatPlayer(context.getSource().getPlayer())));
    }

    public static int hatPlayer(@Nullable ServerPlayerEntity player) {
        if (player == null || player.isSpectator()) {
            return 0;
        }

        ItemStack hat = player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack stack = player.getMainHandStack();
        Item item = stack.getItem();

        if (hat.getEnchantments().getEnchantments().contains(Enchantments.BINDING_CURSE)) {
            player.sendMessage(net.minecraft.text.Text.literal(
                    "Already equipped with an Item with BINDING_CURSE"
            ));
            return 0;
        }

        if (item.equals(Items.TOTEM_OF_UNDYING)) {
            player.sendMessage(net.minecraft.text.Text.literal(
                    "Items that cannot be equipped: TOTEM_OF_UNDYING"
            ));
            return 0;
        }

        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof ShulkerBoxBlock) {
                ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
                if (container != null && !container.stream().toList().isEmpty()) {
                    player.sendMessage(net.minecraft.text.Text.literal(
                            "Items that cannot be equipped: SHULKER_BOX(notEmpty)"
                    ));
                    return 0;
                }
            }
        }

        ItemStack stackCopy = stack.copy();
        stackCopy.setCount(1);
        player.equipStack(EquipmentSlot.HEAD, stackCopy);

        if (!player.isCreative()) {
            stack.decrement(1);

            if (player.getInventory().getEmptySlot() < 0) {
                ServerWorld world = player.getEntityWorld();
                ItemEntity itemEntity = new ItemEntity(
                        world,
                        player.getX(),
                        player.getY() + 1.0,
                        player.getZ(),
                        hat.copy()
                );
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                player.getInventory().insertStack(hat.copy());
            }
        }

        player.playerScreenHandler.sendContentUpdates();
        return 1;
    }
}
