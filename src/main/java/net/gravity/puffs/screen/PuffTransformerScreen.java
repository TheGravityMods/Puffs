package net.gravity.puffs.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.puff.Flowerpuff;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.gravity.puffs.item.custom.FlowerPuffRootItem;
import net.gravity.puffs.item.custom.PuffRootItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class PuffTransformerScreen extends Screen implements MenuAccess<PuffTransformerMenu> {
    private final PuffTransformerMenu menu;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PuffsMain.MOD_ID, "textures/gui/puff_transformer_gui.png");
    private final int imageWidth;
    private final int imageHeight;
    private int guiLeft;
    private int guiTop;
    private boolean isDragging = false;
    private int dragX, dragY;
    private int currentLine = 0;
    private final TerminalLineBox[] lines = new TerminalLineBox[16];
    private final String prefix = "> ";

    public PuffTransformerScreen(PuffTransformerMenu pMenu, Inventory inventory, Component component) {
        super(GameNarrator.NO_TITLE);
        this.menu = pMenu;
        this.imageWidth = 220;
        this.imageHeight = 155;
    }

    public PuffTransformerMenu getMenu() {
        return this.menu;
    }

    public void tick() {
        super.tick();
        for(TerminalLineBox editBox : lines) {
            if (editBox != null) {
                editBox.tick();
            }
        }
    }
    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        for (int i = 0; i < 16; i++) {
            this.lines[i] = new TerminalLineBox(this.font, this.guiLeft + 123, this.guiTop + 30 + i * 10, 90, 9, Component.empty());
            this.lines[i].setMaxLength(120);
            this.lines[i].setBordered(false);
            this.lines[i].setVisible(true);
            this.lines[i].setTextColor(4711204);
            this.lines[i].setValue("");
            this.lines[i].setWidth(91);
            this.addWidget(this.lines[i]);
        }
        lines[0].setFocus(true);
        lines[0].setValue(prefix);
        lines[0].moveCursorToEnd();
        guiLeft = (width - imageWidth) / 2;
        guiTop = (height - imageHeight) / 2;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean charTyped(char pCodePoint, int pModifiers) {
        this.lines[currentLine].charTyped(pCodePoint, pModifiers);
        return true;
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 257) {
            if (currentLine + 1 <= 13) {
                this.lines[currentLine].setCanLoseFocus(true);
                this.lines[currentLine].setEditable(false);
                this.lines[currentLine].setFocus(false);
                this.currentLine = this.currentLine + 1;
                PuffsMain.LOGGER.info(String.valueOf(currentLine));
                this.lines[currentLine].setValue(prefix);
                this.lines[currentLine].setFocus(true);
                this.lines[currentLine].setCanLoseFocus(false);
            }
            if (currentLine + 1 == 15) {
                this.lines[currentLine].setCanLoseFocus(true);
                this.lines[currentLine].setEditable(false);
                this.lines[currentLine].setFocus(false);
                this.currentLine = this.currentLine + 1;
                this.lines[currentLine].setValue("");
                this.lines[currentLine].setFocus(false);
                this.lines[currentLine].setCanLoseFocus(true);
            }
            return true;
        } else if (pKeyCode == 259 && lines[currentLine].getValue().startsWith(prefix) && lines[currentLine].getCursorPosition() - 1 <= 1) {
            return false;
        } else {
            return this.lines[currentLine].keyPressed(pKeyCode, pScanCode, pModifiers) || super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    @Override
    public void renderBackground(PoseStack pPoseStack) {
        super.renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pPoseStack, guiLeft, guiTop, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (mouseX >= guiLeft && mouseX < guiLeft + 220 && mouseY >= guiTop && mouseY < guiTop + 155) {
                isDragging = true;
                dragX = (int) mouseX - guiLeft;
                dragY = (int) mouseY - guiTop;
            }
        }
        if (this.lines[currentLine].mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            guiLeft = (int) mouseX - dragX;
            guiTop = (int) mouseY - dragY;
            for (int i = 0; i < 16; i++) {
                this.lines[i].setX(guiLeft + 123);
                this.lines[i].y = guiTop + 30 + i * 10;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }


    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
//        for (int i = 0; i < lines.length; i++) {
//            if (Objects.equals(lines[i].getValue(), "")) {
//                lines[i].getValue();
//            }
//            drawString(pPoseStack, Minecraft.getInstance().font, lines[i].getValue(), guiLeft + 10, guiTop + (i + 1) * this.font.wordWrapHeight(lines[i].getValue(), this.font.width(lines[i].getValue())), 4711204);
//            // 4711204
//        }
        for (TerminalLineBox editBox : lines) {
            editBox.render(pPoseStack, mouseX, mouseY, delta);
        }
        if (this.menu.getRoot().getItem() instanceof PuffRootItem puffRootItem) {
            Puff puff = puffRootItem.getAssociatedPuff().create(this.minecraft.level);
            if (puff instanceof Flowerpuff flowerpuff && puffRootItem instanceof FlowerPuffRootItem flowerPuffRootItem) {
                flowerpuff.setFlowerType(flowerPuffRootItem.getAssociatedFlowerType());
            }
            drawCenteredString(pPoseStack, Minecraft.getInstance().font, puff.getName(), guiLeft + 165, guiTop + 13, 16777215);
            renderEntityInInventory(guiLeft + 159, guiTop + 55, 35, mouseX, mouseY, puff);
            renderGuiItem(new ItemStack(puffRootItem), guiLeft + 174, guiTop + 35, 30);
        }
    }

    public void renderGuiItem(ItemStack pStack, int pX, int pY, float scale) {
        this.renderGuiItemWithScale(pStack, pX, pY, scale, this.itemRenderer.getModel(pStack, (Level) null, (LivingEntity) null, 0));
    }

    protected void renderGuiItemWithScale(ItemStack pStack, int pX, int pY, float scale, BakedModel pBakedModel) {
        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double) pX, (double) pY, (double) (100.0F + this.itemRenderer.blitOffset));
        posestack.translate(8.0D, 8.0D, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(scale, scale, scale);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !pBakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        this.itemRenderer.render(pStack, ItemTransforms.TransformType.GUI, false, posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, pBakedModel);
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            Lighting.setupFor3DItems();
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    public static void renderEntityInInventory(int pPosX, int pPosY, int pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        float f = (float) Math.atan((double) (-115 / 40.0F));
        float f1 = (float) Math.atan((double) (-115 / 40.0F));
        renderEntityInInventoryRaw(pPosX, pPosY, pScale, f, f1, pLivingEntity);
    }

    public static void renderEntityInInventoryRaw(int pPosX, int pPosY, int pScale, float angleXComponent, float angleYComponent, LivingEntity pLivingEntity) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double) pPosX, (double) pPosY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float) pScale, (float) pScale, (float) pScale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);
        float f2 = pLivingEntity.yBodyRot;
        float f3 = pLivingEntity.getYRot();
        float f4 = pLivingEntity.getXRot();
        float f5 = pLivingEntity.yHeadRotO;
        float f6 = pLivingEntity.yHeadRot;
        pLivingEntity.yBodyRot = 180.0F + f * 20.0F;
        pLivingEntity.setYRot(180.0F + f * 40.0F);
        pLivingEntity.setXRot(-f1 * 20.0F);
        pLivingEntity.yHeadRot = pLivingEntity.getYRot();
        pLivingEntity.yHeadRotO = pLivingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pLivingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        pLivingEntity.yBodyRot = f2;
        pLivingEntity.setYRot(f3);
        pLivingEntity.setXRot(f4);
        pLivingEntity.yHeadRotO = f5;
        pLivingEntity.yHeadRot = f6;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
}