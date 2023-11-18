package net.gravity.puffs.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.custom.puff.Puff;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class PuffJournalDisplayScreen extends Screen {
    public static final ResourceLocation BOOK_LOCATION = new ResourceLocation(PuffsMain.MOD_ID,"textures/gui/puff_journal_screen.png");
    private int currentPage;
    /** Holds a copy of the page text, split into page width lines */
    private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();
    private int cachedPage = -1;
    private PageArrowButton forwardButton;
    private PageArrowButton backButton;
    private final Puff puff;
    /** Determines if a sound is played when the page is turned */

    public PuffJournalDisplayScreen(EntityType<? extends Puff> puff) {
        super(GameNarrator.NO_TITLE);
        this.puff = puff.create(Minecraft.getInstance().level);
    }

    /**
     * Moves the book to the specified page and returns true if it exists, {@code false} otherwise.
     */
    public boolean setPage(int pPageNum) {
        int i = 0;
        if (i != this.currentPage) {
            this.currentPage = i;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    protected void createPageControlButtons() {
        int i = (this.width - 192) / 2;
        int j = (this.height - 180) / 4;
        this.forwardButton = this.addRenderableWidget(new PageArrowButton(i + 116, j, true, (p_98297_) -> {
            this.pageForward();
        }));
        this.backButton = this.addRenderableWidget(new PageArrowButton(i + 43, j, false, (p_98287_) -> {
            this.pageBack();
        }));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return 1;
    }

    /**
     * Moves the display back one page
     */
    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    /**
     * Moves the display forward one page
     */
    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else {
            switch (pKeyCode) {
                case 266:
                    this.backButton.onPress();
                    return true;
                case 267:
                    this.forwardButton.onPress();
                    return true;
                default:
                    return false;
            }
        }
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BOOK_LOCATION);
        int i = (this.width - 192) / 2 - 85;
        int j = (this.height - 180) / 2 - 20;
        pPoseStack.pushPose();
        pPoseStack.scale(1.3f, 1.3f, 1.3f);
        this.blit(pPoseStack, i, j, 0, 0, 256, 180);
        pPoseStack.popPose();
        drawEntityOnScreen(pPoseStack, 175, 120, 55, true, 30, 225, 0, pMouseX, pMouseY, puff);
        font.draw(pPoseStack, puff.getType().getDescription(), 95, 47, 0XBAAC98);
        if (this.cachedPage != this.currentPage) {
            StringBuilder s1 = new StringBuilder();
            PuffsMain.LOGGER.info(puff.getDisplayName().getString().toLowerCase());
            for(String s : getLines(new ResourceLocation(PuffsMain.MOD_ID, "descs/" + puff.getDisplayName().getString().toLowerCase() + ".txt"))) {
                s1.append(s).append(" ");
            }
            FormattedText formattedtext = FormattedText.of(s1.toString());
            this.cachedPageComponents = this.font.split(formattedtext, 145);
        }


        this.cachedPage = this.currentPage;
        int k = Math.min(254 / 9, this.cachedPageComponents.size());

        for(int l = 0; l < k; ++l) {
            FormattedCharSequence formattedcharsequence = this.cachedPageComponents.get(l);
            if(l > 6) {
                this.font.draw(pPoseStack, formattedcharsequence, (float) (i + 190), (float) (-20 + l * 10), 0X303030);
            } else {
                this.font.draw(pPoseStack, formattedcharsequence, (float) (i + 36), (float) (140 + l * 10), 0X303030);
            }
        }

        Style style = this.getClickedComponentStyleAt((double)pMouseX, (double)pMouseY);
        if (style != null) {
            this.renderComponentHoverEffect(pPoseStack, style, pMouseX, pMouseY);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void drawEntityOnScreen(PoseStack stackIn, int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        float customYaw = posX - mouseX;
        float customPitch = posY - mouseY;
        float f = (float) Math.atan(customYaw / 45.0F);
        float f1 = (float) Math.atan(customPitch / 45.0F);

        if (follow) {
            float setX = f1 * 20.0F;
            float setY = f * 20.0F;
            entity.setXRot(setX);
            entity.setYRot(setY);
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).yBodyRot = setY;
                ((LivingEntity) entity).yBodyRotO = setY;
                ((LivingEntity) entity).yHeadRot = setY;
                ((LivingEntity) entity).yHeadRotO = setY;
            }
        } else {
            f = 0;
            f1 = 0;
        }

        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(posX, posY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        quaternion.mul(Vector3f.XN.rotationDegrees((float) xRot));
        quaternion.mul(Vector3f.YP.rotationDegrees((float) yRot));
        quaternion.mul(Vector3f.ZP.rotationDegrees((float) zRot));
        posestack1.mulPose(quaternion);

        Vector3f light0 = Util.make(new Vector3f(1, -1.0F, -1.0F), Vector3f::normalize);
        Vector3f light1 = Util.make(new Vector3f(-1, -1.0F, 1.0F), Vector3f::normalize);
        RenderSystem.setShaderLights(light0, light1);
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);

        entity.setYRot(0);
        entity.setXRot(0);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).yBodyRot = 0;
            ((LivingEntity) entity).yHeadRotO = 0;
            ((LivingEntity) entity).yHeadRot = 0;
        }


        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
    protected List<String> getLines(ResourceLocation location) {
        try {
            BufferedReader bufferedreader = Minecraft.getInstance().getResourceManager().openAsReader(location);

            List<String> list;
            try {
                list = bufferedreader.lines().map(String::trim).filter((p_118876_) -> {
                    return p_118876_.hashCode() != 125780783;
                }).collect(Collectors.toList());
            } catch (Throwable throwable1) {
                try {
                    bufferedreader.close();
                } catch (Throwable throwable) {
                    throwable1.addSuppressed(throwable);
                }

                throw throwable1;
            }

            bufferedreader.close();

            return list;
        } catch (IOException ioexception) {
            return Collections.emptyList();
        }
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            Style style = this.getClickedComponentStyleAt(pMouseX, pMouseY);
            if (style != null && this.handleComponentClicked(style)) {
                return true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean handleComponentClicked(Style pStyle) {
        ClickEvent clickevent = pStyle.getClickEvent();
        if (clickevent == null) {
            return false;
        } else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String s = clickevent.getValue();

            try {
                int i = Integer.parseInt(s) - 1;
                return this.setPage(i);
            } catch (Exception exception) {
                return false;
            }
        } else {
            boolean flag = super.handleComponentClicked(pStyle);
            if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.closeScreen();
            }

            return flag;
        }
    }

    protected void closeScreen() {
        this.minecraft.setScreen((Screen)null);
    }

    @Nullable
    public Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
        if (this.cachedPageComponents.isEmpty()) {
            return null;
        } else {
            int i = Mth.floor(pMouseX - (double)((this.width - 192) / 2) - 36.0D);
            int j = Mth.floor(pMouseY - 2.0D - 30.0D);
            if (i >= 0 && j >= 0) {
                int k = Math.min(128 / 9, this.cachedPageComponents.size());
                if (i <= 114 && j < 9 * k + k) {
                    int l = j / 9;
                    if (l >= 0 && l < this.cachedPageComponents.size()) {
                        FormattedCharSequence formattedcharsequence = this.cachedPageComponents.get(l);
                        return this.minecraft.font.getSplitter().componentStyleAtWidth(formattedcharsequence, i);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    static List<String> loadPages(CompoundTag pTag) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        loadPages(pTag, builder::add);
        return builder.build();
    }

    public static void loadPages(CompoundTag pTag, Consumer<String> pConsumer) {
        ListTag listtag = pTag.getList("pages", 8).copy();
        IntFunction<String> intfunction;
        if (Minecraft.getInstance().isTextFilteringEnabled() && pTag.contains("filtered_pages", 10)) {
            CompoundTag compoundtag = pTag.getCompound("filtered_pages");
            intfunction = (p_169702_) -> {
                String s = String.valueOf(p_169702_);
                return compoundtag.contains(s) ? compoundtag.getString(s) : listtag.getString(p_169702_);
            };
        } else {
            intfunction = listtag::getString;
        }

        for(int i = 0; i < listtag.size(); ++i) {
            pConsumer.accept(intfunction.apply(i));
        }

    }
}
