package srpmixins.capability;

public interface ICapabilityEvoPoints {
    //Only for NBT storage
    void setEvoPointsFromNBT(int points);
    int getEvoPoints();
    void setEvoPointsBufferFromNBT(int pointsBuffer);
    int getEvoPointsBuffer();
    void setEvoPhaseFromNBT(byte phase);
    byte getEvoPhase();
    void setCooldownUntilFromNBT(long cooldownUntilWorldTime);
    long getCooldownUntil();

    //Update nearby chunks
    boolean addEvoPoints(int points, long currentTime, boolean canChangePhase);
    void setCooldown(int cooldown, long currentTime, boolean doUpdateNearby);

    void addCooldownTicks(int cooldown, long currentTime);
    int getCooldownTicks(long currentTime);

    void setEvoPhase(byte phase);

    void setPointsToPhaseStart();
}
