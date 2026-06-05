package srpmixins.client.renderer.tileentity;

import net.minecraft.client.model.ModelBed;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import srpmixins.SRPMixins;
import srpmixins.registry.ModItems;
import srpmixins.tileentity.TileEntityEvolutionBed;

@SideOnly(Side.CLIENT)
public class TESREvolutionBed extends TileEntitySpecialRenderer<TileEntityEvolutionBed> {

    private static final ResourceLocation[] TEXTURES;
    private ModelBed model = new ModelBed();
    private int version;

    static {
        TEXTURES = new ResourceLocation[10];

        for(int phase = 0; phase < 10; phase++) {
            TEXTURES[phase] = new ResourceLocation(SRPMixins.MODID, "textures/entity/evolution_bed/" + phase + ".png");
        }
    }

    public TESREvolutionBed() {
        this.version = this.model.getModelVersion();
    }

    public void render(TileEntityEvolutionBed te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (this.version != this.model.getModelVersion()) {
            this.model = new ModelBed();
            this.version = this.model.getModelVersion();
        }

        boolean worldLoaded = te.getWorld() != null;
        boolean headPiece = worldLoaded ? te.isHeadPiece() : true;
        int facing = worldLoaded ? te.getBlockMetadata() & 3 : 0;

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else {
            ResourceLocation resourcelocation = this.getTexture(te.getRespectivePhase());

            if (resourcelocation != null) {
                this.bindTexture(resourcelocation);
            }
        }

        if (worldLoaded) {
            this.renderPiece(headPiece, x, y, z, facing, alpha);
        }
        else {
            GlStateManager.pushMatrix();
            this.renderPiece(true, x, y, z, facing, alpha);
            this.renderPiece(false, x, y, z - 1.0D, facing, alpha);
            GlStateManager.popMatrix();
        }

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private void renderPiece(boolean headPiece, double x, double y, double z, int facing, float alpha) {
        this.model.preparePiece(headPiece);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        if (facing == EnumFacing.NORTH.getHorizontalIndex()) {
            f = 0.0F;
        }
        else if (facing == EnumFacing.SOUTH.getHorizontalIndex()) {
            f = 180.0F;
            f1 = 1.0F;
            f2 = 1.0F;
        }
        else if (facing == EnumFacing.WEST.getHorizontalIndex()) {
            f = -90.0F;
            f2 = 1.0F;
        }
        else if (facing == EnumFacing.EAST.getHorizontalIndex()) {
            f = 90.0F;
            f1 = 1.0F;
        }

        GlStateManager.translate((float)x + f1, (float)y + 0.5625F, (float)z + f2);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        this.model.render();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.popMatrix();
    }

    public ResourceLocation getTexture(int phase) {
        phase = MathHelper.clamp(phase, 0, TEXTURES.length - 1);
        return TEXTURES[phase];
    }

    public static class TEISREvolutionBed extends TileEntityItemStackRenderer {

        private final TileEntityEvolutionBed bed = new TileEntityEvolutionBed();

        @Override
        public void renderByItem(ItemStack itemStackIn, float partialTicks) {
            Item item = itemStackIn.getItem();

            if (item == ModItems.evolutionBed) {
                this.bed.setItemValues(itemStackIn);
                TileEntityRendererDispatcher.instance.render(this.bed, 0.0D, 0.0D, 0.0D, 0.0F);
            }
        }
    }
}