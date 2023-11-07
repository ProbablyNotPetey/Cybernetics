package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.util.Easing;
import com.vivi.cybernetics.common.cyberware.CyberwareSection;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.C2SSwitchActiveSlotPacket;
import com.vivi.cybernetics.server.network.packet.C2SSwitchPagePacket;
import net.minecraft.network.chat.Component;

import java.util.Iterator;

public abstract class State {

    protected final CyberwareScreen<?> screen;
    protected long time;
    public State(CyberwareScreen<?> screen) {
        this.screen = screen;
    }
    public void enter() {

    }
    public void exit() {
        screen.clearAll();
    }
    public void render(PoseStack poseStack, float partialTick) {

    }
    public void tick(long time) {
        this.time = time;
    }



    public static class Main extends State {

        private final Iterator<SectionButton> sectionButtonIterator;
        public Main(CyberwareScreen<?> screen) {
            super(screen);
            sectionButtonIterator = screen.getSectionButtons().iterator();
        }

        @Override
        public void enter() {
            screen.scheduleTask(15, 16 + (2 * screen.getSectionButtons().size()), this::initSectionButtons);
            screen.getTextWidget().setText(Component.translatable("tooltip.cybernetics.section"));
        }

        @Override
        public void exit() {
            super.exit();
            screen.getSectionButtons().forEach(button -> {
                screen.alphaWidget(button, 0.0f, 10);
                screen.scheduleTask(15, () -> {
                    button.y = screen.getGuiTop() + button.getSection().getType().getY() + 20;
                });
            });
        }

        private void initSectionButtons() {
            if(time % 2 == 0 && sectionButtonIterator.hasNext()) {
                SectionButton button = sectionButtonIterator.next();
                screen.moveWidget(button, button.x, button.y - 20, 15, Easing.CUBIC_OUT);
                screen.alphaWidget(button, 1.0f, 15);
            }
        }
    }



    public static class Section extends State {

        private final int x;
        private final int y;
        private final CyberwareSection section;
        private final Iterator<MaskWidget> maskWidgetIterator;
        private boolean canGoBack;
        public Section(CyberwareScreen<?> screen, CyberwareSection section, int x, int y) {
            super(screen);
            this.section = section;
            this.x = x;
            this.y = y;
            maskWidgetIterator = screen.getItemMasks().iterator();
        }

        @Override
        public void enter() {
            super.enter();
            EntityWidget entityWidget = screen.getEntityWidget();
            screen.rotateEntity(entityWidget, -45, 20, Easing.CUBIC_IN_OUT);
            screen.scaleWidget(entityWidget, 120, 20, Easing.CUBIC_IN_OUT);
            screen.moveWidget(entityWidget, screen.getGuiLeft() + x, screen.getGuiTop() + y, 20, Easing.CUBIC_IN_OUT);

            screen.scheduleTask(15, this::showMasks);
            screen.scheduleTask(20, 21 + (2 * screen.getItemMasks().size()), this::fadeOutMasks);
            screen.scheduleTask(15, () -> {
                screen.getMenu().switchActiveSlots(section.getType());
                CybPackets.sendToServer(new C2SSwitchActiveSlotPacket(section.getType()));
                screen.getMenu().switchInventoryPage(0);
                CybPackets.sendToServer(new C2SSwitchPagePacket(0));
            });

            screen.getTextWidget().setText(Component.translatable("tooltip." + section.getId().getNamespace() + ".section." + section.getId().getPath()));

            screen.scheduleTask(20, () -> {
                canGoBack = true;
//                if(section.getSlots() <= 4) {
//                    screen.getPageButtonLeft().y -= 10;
//                    screen.getPageButtonRight().y -= 10;
//                    screen.getArrowWidget().y -= 10;
//                }

                if(screen.canEdit()) {
                    screen.alphaWidget(screen.getPageButtonLeft(), 1.0f, 15);
                    screen.alphaWidget(screen.getPageButtonRight(), 1.0f, 15);
                    screen.alphaWidget(screen.getArrowWidget(), 1.0f, 15);
                }
            });
        }

        @Override
        public void exit() {
            super.exit();
            EntityWidget entityWidget = screen.getEntityWidget();
            screen.rotateEntity(entityWidget, 0, 20, Easing.CUBIC_IN_OUT);
            screen.scaleWidget(entityWidget, 60, 20, Easing.CUBIC_IN_OUT);
            screen.moveWidget(entityWidget, screen.getGuiLeft() + 91, screen.getGuiTop() + 16, 20, Easing.CUBIC_IN_OUT);
            screen.getTextWidget().setText(Component.translatable("tooltip.cybernetics.section"));
            fadeInMasks();
            if(screen.canEdit()) {
                screen.alphaWidget(screen.getPageButtonLeft(), 0.0f, 5);
                screen.alphaWidget(screen.getPageButtonRight(), 0.0f, 5);
                screen.alphaWidget(screen.getArrowWidget(), 0.0f, 5);
            }


            screen.scheduleTask(6, () -> {
                hideMasks();
                screen.getMenu().switchActiveSlots(null);
                CybPackets.sendToServer(new C2SSwitchActiveSlotPacket());
                screen.getMenu().switchInventoryPage(-1);
                CybPackets.sendToServer(new C2SSwitchPagePacket());

//                screen.getPageButtonLeft().y = screen.getGuiTop() + 67;
//                screen.getPageButtonRight().y = screen.getGuiTop() + 67;
//                screen.getArrowWidget().y = screen.getGuiTop() + 62;
                screen.getArrowWidget().setMode(ArrowWidget.Mode.NONE);
            });
        }

        public boolean canGoBack() {
            return canGoBack;
        }

        private void showMasks() {
            screen.getItemMasks().forEach(mask -> {
                mask.setAlpha(1.0f);
            });
        }
        private void hideMasks() {
            screen.getItemMasks().forEach(mask -> {
                mask.setAlpha(0.0f);
            });
        }
        private void fadeInMasks() {
            screen.getItemMasks().forEach(widget -> {
                screen.alphaWidget(widget, 1.0f, 5);
            });
        }
        private void fadeOutMasks() {
            if(maskWidgetIterator.hasNext()) {
                MaskWidget widget = maskWidgetIterator.next();
                screen.alphaWidget(widget, 0.0f, 15);
            }
        }
    }
}
