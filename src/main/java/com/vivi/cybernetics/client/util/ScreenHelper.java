package com.vivi.cybernetics.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ScreenHelper {

    private static final Map<Screen, List<Animation>> animations = new HashMap<>();
    private static final Map<Screen, List<ScheduledTask>> tasks = new HashMap<>();
    private static long currentTime = 0;

    public static void addAnimation(Screen screen, Supplier<Float> getter, Consumer<Float> setter, float newValue, int duration) {
        addAnimation(screen, getter, setter, newValue, duration, Easing.LINEAR);
    }
    public static void addAnimation(Screen screen, Supplier<Float> getter, Consumer<Float> setter, float newValue, int duration, Easing easing) {
        animations.get(screen).add(new Animation(getter, setter, newValue, getGameTime(), duration, easing));
    }
    public static void clearAnimations(Screen screen) {
        animations.get(screen).clear();
    }

    public static void scheduleTask(Screen screen, int time, Runnable task) {
        tasks.get(screen).add(new ScheduledTask(currentTime + time, -1, task, false));
    }
    public static void scheduleTask(Screen screen, int time, Runnable task, boolean continuous) {
        tasks.get(screen).add(new ScheduledTask(currentTime + time, -1, task, continuous));
    }
    public static void scheduleTask(Screen screen, int time, int endTime, Runnable task) {
        tasks.get(screen).add(new ScheduledTask(currentTime + time, currentTime + endTime, task, true));
    }
    public static void clearTasks(Screen screen) {
        tasks.get(screen).clear();
    }

    public static long getGameTime() {
        return Minecraft.getInstance().level.getGameTime();
    }
    public static float getPartialTick() {
        return Minecraft.getInstance().getPartialTick();
    }



    private static void init(Screen screen) {
        animations.remove(screen);
        animations.put(screen, new ArrayList<>());
        tasks.remove(screen);
        tasks.put(screen, new ArrayList<>());
        currentTime = 0;
    }

    private static void tick(Screen screen) {
        currentTime++;
        List<ScheduledTask> taskList = tasks.get(screen);
        for(int i = 0; i < taskList.size(); i++) {
            ScheduledTask task = taskList.get(i);
            if(task.continuous() && currentTime >= task.startTime() && (task.endTime() == -1 || currentTime <= task.endTime())) {
                task.task().run();
            }
            else if(task.endTime() != -1 && currentTime > task.endTime()) {
                taskList.remove(i);
                i--;
            }
            else if(currentTime == task.startTime()) {
                task.task().run();
                taskList.remove(i);
                i--;
            }
        }
    }

    private static void render(Screen screen, PoseStack poseStack, int mouseX, int mouseY, float frameTimeDelta) {
        List<Animation> animationList = animations.get(screen);
        for(int i = 0; i < animationList.size(); i++) {
            Animation animation = animationList.get(i);
            animation.update(getGameTime(), getPartialTick());
            if(animation.isDone()) {
                animationList.remove(i);
                i--;
            }
        }
    }

    private static void close(Screen screen) {
        animations.remove(screen);
        tasks.remove(screen);
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Pre event) {
        init(event.getScreen());
    }

    @SubscribeEvent
    public static void onRenderScreenPost(ScreenEvent.Render.Post event) {
        render(event.getScreen(), event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        close(event.getScreen());
    }

    @SubscribeEvent
    public static void onClientTickPost(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        if(Minecraft.getInstance().screen != null) {
            tick(Minecraft.getInstance().screen);
        }
    }
}
