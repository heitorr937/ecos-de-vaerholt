package com.vaerholt.echoes.capability;

import net.minecraft.core.BlockPos;

/** Estado de Pavor (sanidade) de um jogador. 0 = calmo, 100 = ruptura total. */
public interface IDread {
    float getDread();
    void setDread(float value);
    default void addDread(float amount) {
        setDread(Math.max(0f, Math.min(100f, getDread() + amount)));
    }

    boolean isHiding();
    void setHiding(boolean hiding, BlockPos at);
    BlockPos getHidingPos();

    /** Ticks restantes de "atenção" do Descarnado nesse jogador (usado para IA de perseguição). */
    int getHuntFocusTicks();
    void setHuntFocusTicks(int ticks);

    void copyFrom(IDread other);
}
