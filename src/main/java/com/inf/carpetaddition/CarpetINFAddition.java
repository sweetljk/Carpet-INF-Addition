package com.inf.carpetaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.inf.carpetaddition.command.HatCommand;
import com.inf.carpetaddition.command.SitCommand;
import com.inf.carpetaddition.command.ScaleCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class CarpetINFAddition implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "carpet_inf_addition";
    public static final String ASSETS_PATH = "assets/" + MOD_ID;

    public static void loadExtension() {
        CarpetServer.manageExtension(new CarpetINFAddition());
    }

    @Override
    public String version() { return MOD_ID; }

    @Override
    public void onInitialize() { loadExtension(); }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetINFSettings.class);
        CarpetServer.settingsManager.registerRuleObserver((source, rule, value) -> {
            if (!"visibleSpectators".equals(rule.name())) {
                return;
            }
            MinecraftServer server = source.getServer();
            if (server == null) {
                return;
            }
            // Re-run entity tracking so spectators become visible (or hidden) without waiting
            // for them to move. Execute next tick so the new rule value is already in effect.
            server.execute(() -> {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (!player.isSpectator()) {
                        continue;
                    }
                    player.setInvisible(!CarpetINFSettings.visibleSpectators);
                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.getChunkSource().move(player);
                    }
                }
            });
        });
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher,
                                 CommandBuildContext commandBuildContext) {
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
