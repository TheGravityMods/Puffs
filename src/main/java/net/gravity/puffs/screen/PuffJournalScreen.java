package net.gravity.puffs.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.gravity.puffs.PuffsMain;
import net.gravity.puffs.entity.ModEntities;
import net.gravity.puffs.entity.client.PuffList;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PuffJournalScreen extends Screen {
    public static final ResourceLocation BOOK_LOCATION = new ResourceLocation(PuffsMain.MOD_ID,"textures/gui/puff_journal_screen.png");
    private int currentPage;
    /** Holds a copy of the page text, split into page width lines */
    private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();
    private int cachedPage = -1;
    private PageArrowButton forwardButton;
    private PageArrowButton backButton;
    private final List<PuffDisplayTile> puffDisplayTile = Lists.newArrayList();

    public PuffJournalScreen() {
        super(GameNarrator.NO_TITLE);
    }

    /**
     * Moves the book to the specified page and returns true if it exists, {@code false} otherwise.
     */
    public boolean setPage(int pPageNum) {
        int i = Mth.clamp(pPageNum, 0, 2);
        if (i != this.currentPage) {
            this.currentPage = i;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    /**
     * I'm not sure why this exists. The function it calls is public and does all the work.
     */
    protected boolean forcePage(int pPageNum) {
        return this.setPage(pPageNum);
    }

    protected void init() {
        this.createPageControlButtons();
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
        int y = 50;
        int x = 87;
        for(int i2 = 0; i2 < PuffList.getPuffEntityIcons().size(); i2++) {
            if(i2 != 0 && i2 % 5 == 0) {
                y += 30;
            }
            this.puffDisplayTile.add(this.addRenderableWidget((new PuffDisplayTile(PuffList.getPuffEntityIcons().get(i2), x + i2 % 5 * 30, y))));
        }
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
        int i = (this.width - 320) / 2 - 20;
        int j = (this.height - 181) / 2 - 10;
        pPoseStack.pushPose();
        pPoseStack.scale(1.1f, 1.1f, 0);
        blit(pPoseStack, i, j, 0, 0, 320, 181, 320, 320);
        pPoseStack.popPose();
        Style style = this.getClickedComponentStyleAt((double)pMouseX, (double)pMouseY);
        if (style != null) {
            this.renderComponentHoverEffect(pPoseStack, style, pMouseX, pMouseY);
        }

        for(PuffDisplayTile gamemodeswitcherscreen$gamemodeslot : this.puffDisplayTile) {
            gamemodeswitcherscreen$gamemodeslot.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
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
                return this.forcePage(i);
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
}
