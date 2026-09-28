package com.inf.carpetaddition.mixin.commandSit;

import com.inf.carpetaddition.sit.SitEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class MixinArmorStandEntity extends LivingEntity implements SitEntity {
    @Unique private boolean carpetInf$sitEntity = false;

    protected MixinArmorStandEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow private void setMarker(boolean marker) {}

    @Override public boolean carpetInf$isSitEntity() { return carpetInf$sitEntity; }

    @Override public void carpetInf$setSitEntity(boolean sitEntity) {
        carpetInf$sitEntity = sitEntity;
        setMarker(sitEntity);
        setInvisible(sitEntity);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (carpetInf$isSitEntity()) {
            setPos(getX(), getY() + 0.16, getZ());
            kill((net.minecraft.server.level.ServerLevel) level());
        }
        super.removePassenger(passenger);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void carpetInf$postWriteCustomData(ValueOutput output, CallbackInfo ci) {
        if (carpetInf$sitEntity) output.putBoolean("CarpetINF_SitEntity", true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void carpetInf$postReadCustomData(ValueInput input, CallbackInfo ci) {
        carpetInf$sitEntity = input.getBooleanOr("CarpetINF_SitEntity", false);
    }
}
