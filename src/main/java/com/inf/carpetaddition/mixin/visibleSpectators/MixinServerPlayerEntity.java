package com.inf.carpetaddition.mixin.visibleSpectators;

import com.inf.carpetaddition.CarpetINFSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayerEntity extends Player {

    protected MixinServerPlayerEntity(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    /**
     * Keeps the spectator's invisible flag cleared so the client renders the (head-only) avatar.
     */
    @Inject(
        method = "updateInvisibilityStatus",
        at = @At("HEAD"),
        cancellable = true
    )
    private void carpetInf$visibleSpectator(CallbackInfo ci) {
        if (CarpetINFSettings.visibleSpectators && this.isSpectator()) {
            this.removeEffectParticles();
            this.setInvisible(false);
            ci.cancel();
        }
    }

    /**
     * 26.3 stopped broadcasting spectator player entities to non-spectator players. When the rule is
     * enabled we restore the old behaviour so spectators are tracked (and their heads rendered).
     */
    @Inject(
        method = "broadcastToPlayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private void carpetInf$broadcastSpectator(ServerPlayer viewer, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetINFSettings.visibleSpectators && this.isSpectator()) {
            cir.setReturnValue(true);
        }
    }

    /**
     * A game mode change does not by itself re-run entity tracking for already connected players,
     * so force a tracking refresh after the change. This also makes rule toggles take effect when a
     * spectator later changes game mode again.
     */
    @Inject(
        method = "setGameMode",
        at = @At("TAIL")
    )
    private void carpetInf$refreshSpectatorTracking(GameType gameType, CallbackInfoReturnable<Boolean> cir) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().execute(() -> {
                if (this.level() == serverLevel) {
                    serverLevel.getChunkSource().move((ServerPlayer) (Object) this);
                }
            });
        }
    }
}
