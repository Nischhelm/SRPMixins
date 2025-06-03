package srpmixins.network;

import com.dhanantry.scapeandrunparasites.SRPMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class SRPMixinsPacketBiomeChange implements IMessage {
    private final List<BlockPos> blockPos = new ArrayList<>();
    private boolean convertToParasite;
    private int type;

    public SRPMixinsPacketBiomeChange() {}

    public SRPMixinsPacketBiomeChange(boolean convert, int type) {
        this.convertToParasite = convert;
        this.type = type;
    }

    public void addBlockPos(BlockPos pos){
        this.blockPos.add(pos);
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.convertToParasite = byteBuf.readBoolean();
        this.type = byteBuf.readInt();
        int nPos = byteBuf.readInt();
        for(int i = 0; i < nPos; i++)
            this.blockPos.add(new BlockPos(byteBuf.readInt(), 0, byteBuf.readInt()));
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.convertToParasite);
        byteBuf.writeInt(this.type);
        byteBuf.writeInt(this.blockPos.size());
        for(BlockPos pos : this.blockPos) {
            byteBuf.writeInt(pos.getX());
            byteBuf.writeInt(pos.getZ());
        }
    }

    public static class Handler implements IMessageHandler<SRPMixinsPacketBiomeChange, IMessage> {
        @Override
        public IMessage onMessage(SRPMixinsPacketBiomeChange message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(SRPMixinsPacketBiomeChange message, MessageContext ctx) {
            for(BlockPos pos : message.blockPos)
                SRPMain.proxy.spreadBiome(pos, message.convertToParasite, message.type);
        }
    }
}