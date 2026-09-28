package com.inf.carpetaddition.command;

import carpet.utils.CommandHelper;
import com.inf.carpetaddition.CarpetINFSettings;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

public final class HatCommand {
    private HatCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("hat")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetINFSettings.commandHat))
                .executes(context -> hatPlayer(context.getSource().getPlayer())));
    }

    public static int hatPlayer(@Nullable ServerPlayer player) {
        if (player == null || player.isSpectator()) return 0;

        ItemStack hat = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack stack = player.getMainHandItem();

        if (EnchantmentHelper.has(hat, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
            player.sendSystemMessage(Component.literal("Already equipped with an Item with BINDING_CURSE"));
            return 0;
        }

        if (stack.is(Items.TOTEM_OF_UNDYING)) {
            player.sendSystemMessage(Component.literal("Items that cannot be equipped: TOTEM_OF_UNDYING"));
            return 0;
        }

        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
            ItemContainerContents container = stack.get(DataComponents.CONTAINER);
            if (container != null && container.nonEmptyItems().iterator().hasNext()) {
                player.sendSystemMessage(Component.literal("Items that cannot be equipped: SHULKER_BOX(notEmpty)"));
                return 0;
            }
        }

        ItemStack stackCopy = stack.copyWithCount(1);
        player.setItemSlot(EquipmentSlot.HEAD, stackCopy);

        if (!player.isCreative()) {
            stack.shrink(1);
            if (player.getInventory().getFreeSlot() < 0) {
                ServerLevel level = player.level();
                ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY() + 1.0, player.getZ(), hat.copy());
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            } else {
                player.getInventory().placeItemBackInInventory(hat.copy());
            }
        }

        player.containerMenu.broadcastChanges();
        return 1;
    }
}
