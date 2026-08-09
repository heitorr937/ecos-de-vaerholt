package com.vaerholt.echoes.capability;

import com.vaerholt.echoes.EchoesMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EchoesMod.MODID)
public class DreadCapability {

    public static final Capability<IDread> DREAD_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    private static final ResourceLocation ID = new ResourceLocation(EchoesMod.MODID, "dread");

    public static LazyOptional<IDread> get(Player player) {
        return player.getCapability(DREAD_CAP);
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (!(event.getObject() instanceof Player)) return;

        event.addCapability(ID, new ICapabilitySerializable<CompoundTag>() {
            final IDread instance = new DreadImpl();
            final LazyOptional<IDread> optional = LazyOptional.of(() -> instance);

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                return cap == DREAD_CAP ? optional.cast() : LazyOptional.empty();
            }

            @Override
            public CompoundTag serializeNBT() {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("dread", instance.getDread());
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                instance.setDread(nbt.getFloat("dread"));
            }
        });
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        event.getOriginal().reviveCaps();
        get(event.getOriginal()).ifPresent(oldCap ->
                get(event.getEntity()).ifPresent(newCap -> newCap.copyFrom(oldCap)));
        event.getOriginal().invalidateCaps();
    }
}
