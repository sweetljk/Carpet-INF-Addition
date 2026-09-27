/*
 * This 
file is part of Carpet-INF-Addition.
 *
 * Portions of this file are derived from VulpeusCarpet:
 * https://github.com/Vulpeus-Server/vulpeus-carpet
 *
 * Copyright (C) 2024 VulpeusServer and contributors.
 * Licensed under the GNU Lesser General Public License v3.0.
 */

package com.inf.carpetaddition.mixin.commandSit;

import com.inf.carpetaddition.sit.SitEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntity.class)
public abstract class MixinArmorStandEntity extends LivingEntity implements SitEntity {

    @Unique
    private boolean carpetInf$sitEntity = false;

    protected MixinArmorStandEntity(
            EntityType<? extends LivingEntity> entityType,
            World world
    ) {
        super(entityType, world);
    }

    @Shadow
    private void setMarker(boolean marker) {
    }

    @Override
    public boolean carpetInf$isSitEntity() {
        return this.carpetInf$sitEntity;
    }

    @Override
    public void carpetInf$setSitEntity(boolean sitEntity) {
        this.carpetInf$sitEntity = sitEntity;
        this.setMarker(sitEntity);
        this.setInvisible(sitEntity);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (this.carpetInf$isSitEntity()) {
            this.setPosition(this.getX(), this.getY() + 0.16, this.getZ());
            this.remove(RemovalReason.KILLED);
        }

        super.removePassenger(passenger);
    }

    @Inject(
            method = "writeCustomData",
            at = @At("RETURN")
    )
    private void carpetInf$postWriteCustomData(
            WriteView view,
            CallbackInfo ci
    ) {
        if (this.carpetInf$sitEntity) {
            view.putBoolean("CarpetINF_SitEntity", true);
        }
    }

    @Inject(
            method = "readCustomData",
            at = @At("RETURN")
    )
    private void carpetInf$postReadCustomData(
            ReadView view,
            CallbackInfo ci
    ) {
        this.carpetInf$sitEntity =
                view.getBoolean("CarpetINF_SitEntity", false);
    }
}