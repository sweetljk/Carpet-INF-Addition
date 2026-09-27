/*

This file is part of Carpet-INF-Addition.


Portions of this file are derived from VulpeusCarpet:
https://github.com/Vulpeus-Server/vulpeus-carpet


Copyright (C) 2024 VulpeusServer and contributors.
Licensed under the GNU Lesser General Public License v3.0.
*/

package com.inf.carpetaddition.mixin.visibleSpectators;

import com.inf.carpetaddition.CarpetINFSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity {

protected MixinServerPlayerEntity(
        World world,
        GameProfile gameProfile
) {
    super(world, gameProfile);
}

@Shadow
public abstract Entity getCameraEntity();

@Inject(
        method = "updatePotionVisibility",
        at = @At("HEAD"),
        cancellable = true
)
private void carpetInf$noInvisibleSpectators(CallbackInfo ci) {
    if (CarpetINFSettings.visibleSpectators) {
        if (isSpectator()) {
            clearPotionSwirls();
        } else {
            super.updatePotionVisibility();
        }

        ci.cancel();
    }
}

@Inject(
        method = "canBeSpectated",
        at = @At("HEAD"),
        cancellable = true
)
private void carpetInf$allowSpectatorsToBeSpectated(
        ServerPlayerEntity spectator,
        CallbackInfoReturnable<Boolean> cir
) {
    if (CarpetINFSettings.visibleSpectators) {
        if (spectator.isSpectator()) {
            cir.setReturnValue(getCameraEntity() == this);
        } else {
            cir.setReturnValue(super.canBeSpectated(spectator));
        }
    }
}

}