package com.vaerholt.echoes.capability;

import net.minecraft.core.BlockPos;

public class DreadImpl implements IDread {
    private float dread = 0f;
    private boolean hiding = false;
    private BlockPos hidingPos = null;
    private int huntFocusTicks = 0;

    @Override public float getDread() { return dread; }
    @Override public void setDread(float value) { this.dread = value; }

    @Override public boolean isHiding() { return hiding; }
    @Override public void setHiding(boolean hiding, BlockPos at) {
        this.hiding = hiding;
        this.hidingPos = hiding ? at : null;
    }
    @Override public BlockPos getHidingPos() { return hidingPos; }

    @Override public int getHuntFocusTicks() { return huntFocusTicks; }
    @Override public void setHuntFocusTicks(int ticks) { this.huntFocusTicks = ticks; }

    @Override
    public void copyFrom(IDread other) {
        this.dread = other.getDread();
    }
}
