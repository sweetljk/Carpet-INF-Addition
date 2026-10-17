package com.inf.carpetaddition.mixin.visibleSpectators;

import com.inf.carpetaddition.CarpetINFSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
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

    @Inject(
        method = "broadcastToPlayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private void carpetInf$broadcastVisibleSpectator(ServerPlayer viewer, CallbackInfoReturnable<Boolean> cir) {
        // Vanilla rejects spectator entities for non-spectator viewers. When this rule is
        // enabled, allow the spectator entity to be tracked so its model/head can be sent
        // to other players. Keep vanilla's spectator-camera logic unchanged.
        if (CarpetINFSettings.visibleSpectators && this.isSpectator() && !viewer.isSpectator()) {
            cir.setReturnValue(true);
        }
    }
}
