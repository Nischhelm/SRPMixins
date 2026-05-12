package srpmixins.util;

public interface IIsTicking {
    long srpmixins$getTick();
    void srpmixins$setTick(long tick);
    void srpmixins$tick(int by);
}
