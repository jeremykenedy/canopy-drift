package com.jeremykenedy.canopydrift;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Random;

final class CanopySceneView extends View {
    private static final int MAX_LEAVES = 170;
    private static final int[] AUTUMN = {0xffbf5630, 0xffd68a31, 0xffc6a24f, 0xff95422d};
    private static final int[] SPRING = {0xff688a4b, 0xff8ba258, 0xffb8b477, 0xff456c3b};
    private static final int[] SUMMER = {0xff2d6339, 0xff3f7841, 0xff578c48, 0xff244f35};
    private static final int[] WINTER = {0xff8b6e59, 0xffa98f75, 0xffd3c4a4, 0xff705643};
    private static final int FOLIAGE_COUNT = 110;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random(920134L);
    private final float[] x = new float[MAX_LEAVES];
    private final float[] y = new float[MAX_LEAVES];
    private final float[] phase = new float[MAX_LEAVES];
    private final float[] size = new float[MAX_LEAVES];
    private final float[] spin = new float[MAX_LEAVES];
    private final float[] foliageX = new float[FOLIAGE_COUNT];
    private final float[] foliageY = new float[FOLIAGE_COUNT];
    private final float[] foliageSize = new float[FOLIAGE_COUNT];
    private final float[] foliageAngle = new float[FOLIAGE_COUNT];
    private final Path leaf = new Path();
    private final Path crown = new Path();
    private CanopyOptions options;
    private LinearGradient sky;
    private RadialGradient sunlight;
    private long startedAt;
    private boolean running;

    CanopySceneView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        for (int i = 0; i < MAX_LEAVES; i++) {
            x[i] = random.nextFloat();
            y[i] = random.nextFloat();
            phase[i] = random.nextFloat() * 6.283f;
            size[i] = 5f + random.nextFloat() * 13f;
            spin[i] = (random.nextFloat() - 0.5f) * 180f;
        }
        for (int i = 0; i < FOLIAGE_COUNT; i++) {
            float zone = random.nextFloat();
            if (zone < 0.37f) {
                foliageX[i] = random.nextFloat() * 0.34f;
                foliageY[i] = random.nextFloat() * 0.65f;
            } else if (zone < 0.74f) {
                foliageX[i] = 0.66f + random.nextFloat() * 0.34f;
                foliageY[i] = random.nextFloat() * 0.65f;
            } else {
                foliageX[i] = 0.18f + random.nextFloat() * 0.64f;
                foliageY[i] = random.nextFloat() * 0.26f;
            }
            foliageSize[i] = 0.008f + random.nextFloat() * 0.009f;
            foliageAngle[i] = (random.nextFloat() - 0.5f) * 70f;
        }
        loadOptions();
    }

    void start() {
        if (!running) {
            running = true;
            startedAt = SystemClock.uptimeMillis();
            postInvalidateOnAnimation();
        }
    }

    void stop() {
        running = false;
        removeCallbacks(invalidator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() <= 0 || getHeight() <= 0) return;
        float time = (SystemClock.uptimeMillis() - startedAt) / 1000f;
        drawSky(canvas, time);
        drawDistantForest(canvas, time);
        drawCanopy(canvas, time);
        drawFallingLeaves(canvas, time);
        if (running) postDelayed(invalidator, 33L);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        int top = options.light == 2 ? 0xff08131b : options.light == 1 ? 0xff634b55 : 0xff4f92bb;
        int bottom = options.light == 2 ? 0xff18232c : options.light == 1 ? 0xffe7a56e : 0xffc5d8c5;
        sky = new LinearGradient(0, 0, 0, Math.max(height, 1), top, bottom, Shader.TileMode.CLAMP);
        int ray = options.light == 0 ? 0x20fff2c1 : options.light == 1 ? 0x17ffb87e : 0x0babc8d7;
        sunlight = new RadialGradient(width * 0.5f, height * 0.2f, Math.max(height * 0.38f, 1),
                new int[] {ray, alpha(ray, 0)}, null, Shader.TileMode.CLAMP);
    }

    private final Runnable invalidator = new Runnable() {
        @Override public void run() { if (running) invalidate(); }
    };

    private void loadOptions() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getContext());
        options = CanopyOptions.resolve(p.getString("density", "handful"),
                p.getString("wind_strength", "breeze"), p.getString("wind_direction", "variable"),
                p.getString("species", "maple"), p.getString("season", "autumn"),
                p.getString("environment", "forest"), p.getString("light", "day"),
                p.getBoolean("rotate_scenes", true), p.getBoolean("randomize_all", false),
                new Random(System.currentTimeMillis()));
    }

    private void drawSky(Canvas canvas, float time) {
        paint.setShader(sky);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);
        paint.setShader(sunlight);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);
    }

    private void drawDistantForest(Canvas canvas, float time) {
        int environment = sceneEnvironment(time);
        int horizon = (int) (getHeight() * (environment == 2 ? 0.74f : 0.83f));
        int forestColor = options.light == 2 ? 0xff15251f : options.season == 3 ? 0xff687466 : 0xff304734;
        for (int i = 0; i < 15; i++) {
            float center = i * getWidth() / 14f;
            float offset = (float) Math.sin(i * 7.13f) * getWidth() * 0.018f;
            float radiusX = getWidth() * (0.035f + (i % 4) * 0.009f);
            float radiusY = getHeight() * (0.08f + (i % 5) * 0.025f);
            float top = horizon - radiusY * (0.7f + (i % 3) * 0.12f);
            crown.reset();
            crown.moveTo(center - radiusX, horizon);
            crown.cubicTo(center - radiusX * 0.98f, top + radiusY * 0.08f,
                    center - radiusX * 0.43f, top - radiusY * 0.22f, center + offset, top);
            crown.cubicTo(center + radiusX * 0.46f, top - radiusY * 0.16f,
                    center + radiusX * 0.98f, top + radiusY * 0.13f, center + radiusX, horizon);
            crown.lineTo(center + radiusX, getHeight());
            crown.lineTo(center - radiusX, getHeight());
            crown.close();
            paint.setColor(forestColor);
            canvas.drawPath(crown, paint);
        }
        if (environment == 1) {
            paint.setColor(options.light == 2 ? 0x553c7b83 : 0x6689c4bd);
            canvas.drawOval(getWidth() * 0.3f, getHeight() * 0.87f,
                    getWidth() * 0.93f, getHeight() * 1.14f, paint);
        } else if (environment == 2) {
            paint.setColor(options.light == 2 ? 0xff19261e : 0xff7b8c58);
            canvas.drawOval(-getWidth() * 0.1f, getHeight() * 0.91f,
                    getWidth() * 1.1f, getHeight() * 1.2f, paint);
        }
    }

    private void drawCanopy(Canvas canvas, float time) {
        int[] colors = colorsForSeason();
        float sway = (float) Math.sin(time * 0.22f) * options.wind * 12f;
        paint.setColor(options.species == 2 ? 0xff584b3e : 0xff392a20);
        paint.setStrokeWidth(getWidth() * (options.species == 1 ? 0.027f : 0.02f));
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(-getWidth() * 0.04f, getHeight() * 0.88f, getWidth() * 0.12f + sway,
                getHeight() * 0.2f, paint);
        canvas.drawLine(getWidth() * 1.04f, getHeight() * 0.92f, getWidth() * 0.83f - sway,
                getHeight() * 0.16f, paint);
        paint.setStrokeWidth(Math.max(3f, getWidth() * 0.006f));
        canvas.drawLine(getWidth() * 0.09f + sway, getHeight() * 0.5f,
                getWidth() * 0.35f + sway, getHeight() * 0.2f, paint);
        canvas.drawLine(getWidth() * 0.2f + sway, getHeight() * 0.36f,
                getWidth() * 0.38f + sway, getHeight() * 0.13f, paint);
        canvas.drawLine(getWidth() * 0.91f - sway, getHeight() * 0.5f,
                getWidth() * 0.66f - sway, getHeight() * 0.19f, paint);
        canvas.drawLine(getWidth() * 0.8f - sway, getHeight() * 0.34f,
                getWidth() * 0.62f - sway, getHeight() * 0.12f, paint);
        for (int i = 0; i < FOLIAGE_COUNT; i++) {
            float scale = getWidth() * foliageSize[i];
            float cx = foliageX[i] * getWidth() + sway * (foliageX[i] < 0.35f ? 1 : -1);
            float cy = foliageY[i] * getHeight();
            for (int j = 0; j < 5; j++) {
                float angle = foliageAngle[i] + j * 72f;
                float offset = scale * 0.34f;
                canvas.save();
                canvas.translate(cx + (float) Math.cos(Math.toRadians(angle)) * offset,
                        cy + (float) Math.sin(Math.toRadians(angle)) * offset * 0.65f);
                canvas.rotate(angle);
                float leafScale = scale * (0.58f + (j % 3) * 0.12f);
                paint.setColor(alpha(colors[(i + j) % colors.length], options.light == 2 ? 224 : 255));
                drawLeafShape(canvas, leafScale);
                paint.setColor(alpha(0xfff2d29a, 115));
                paint.setStrokeWidth(Math.max(0.8f, leafScale * 0.045f));
                canvas.drawLine(0, leafScale * 0.72f, 0, -leafScale * 0.72f, paint);
                for (int vein = -1; vein <= 1; vein += 2) {
                    canvas.drawLine(0, leafScale * vein * 0.3f, leafScale * 0.48f, leafScale * vein * 0.04f, paint);
                }
                canvas.restore();
            }
        }
    }

    private void drawFallingLeaves(Canvas canvas, float time) {
        int[] colors = colorsForSeason();
        for (int i = 0; i < options.leafCount; i++) {
            float phaseTime = time * (0.14f + options.wind * 0.28f) + phase[i];
            float travel = (y[i] + phaseTime * (0.11f + options.wind * 0.07f)) % 1.12f;
            float lateral = options.direction == 0f ? (float) Math.sin(phaseTime * 0.8f + phase[i]) * 0.09f
                    : options.direction * phaseTime * 0.025f;
            lateral += (float) Math.sin(phaseTime * 1.7f + phase[i]) * (0.025f + options.wind * 0.04f);
            float px = (x[i] + lateral + 1.2f) % 1f;
            float py = travel - 0.06f;
            float wobble = (float) Math.sin(phaseTime * 1.6f + phase[i]) * 34f;
            float rotation = time * spin[i] * (0.12f + options.wind * 0.25f) + phase[i] * 57.3f;
            float leafSize = size[i] * (0.7f + options.wind * 0.35f);
            canvas.save();
            canvas.translate(px * getWidth() + wobble, py * getHeight());
            canvas.rotate(rotation);
            paint.setColor(colors[i % colors.length]);
            drawLeafShape(canvas, leafSize);
            paint.setColor(alpha(0xfff5cf7c, 120));
            paint.setStrokeWidth(Math.max(1f, leafSize * 0.07f));
            canvas.drawLine(0, leafSize * 0.67f, 0, -leafSize * 0.67f, paint);
            canvas.restore();
        }
    }

    private void drawLeafShape(Canvas canvas, float s) {
        leaf.reset();
        if (options.species == 0) {
            leaf.moveTo(0, s);
            leaf.cubicTo(-s * 0.1f, s * 0.66f, -s * 0.42f, s * 0.62f, -s * 0.3f, s * 0.36f);
            leaf.cubicTo(-s * 0.88f, s * 0.42f, -s * 0.96f, s * 0.18f, -s * 0.64f, s * 0.04f);
            leaf.cubicTo(-s * 0.98f, -s * 0.12f, -s * 0.82f, -s * 0.46f, -s * 0.43f, -s * 0.37f);
            leaf.cubicTo(-s * 0.56f, -s * 0.75f, -s * 0.21f, -s * 0.78f, -s * 0.1f, -s * 0.43f);
            leaf.cubicTo(-s * 0.07f, -s * 0.72f, -s * 0.03f, -s * 0.91f, 0, -s);
            leaf.cubicTo(s * 0.13f, -s * 0.62f, s * 0.48f, -s * 0.76f, s * 0.48f, -s * 0.39f);
            leaf.cubicTo(s * 0.88f, -s * 0.44f, s * 0.98f, -s * 0.1f, s * 0.64f, s * 0.04f);
            leaf.cubicTo(s * 0.96f, s * 0.2f, s * 0.82f, s * 0.42f, s * 0.3f, s * 0.36f);
            leaf.cubicTo(s * 0.42f, s * 0.62f, s * 0.1f, s * 0.66f, 0, s);
        } else if (options.species == 1) {
            leaf.moveTo(0, s);
            leaf.cubicTo(-s * 0.38f, s * 0.62f, -s * 0.95f, s * 0.52f, -s * 0.74f, s * 0.22f);
            leaf.cubicTo(-s * 0.98f, -s * 0.02f, -s * 0.66f, -s * 0.22f, -s * 0.77f, -s * 0.48f);
            leaf.cubicTo(-s * 0.42f, -s * 0.75f, -s * 0.18f, -s * 0.57f, 0, -s);
            leaf.cubicTo(s * 0.18f, -s * 0.57f, s * 0.42f, -s * 0.75f, s * 0.77f, -s * 0.48f);
            leaf.cubicTo(s * 0.66f, -s * 0.22f, s * 0.98f, -s * 0.02f, s * 0.74f, s * 0.22f);
            leaf.cubicTo(s * 0.95f, s * 0.52f, s * 0.38f, s * 0.62f, 0, s);
        } else {
            leaf.moveTo(0, s);
            leaf.cubicTo(-s * 0.9f, s * 0.42f, -s * 0.7f, -s * 0.16f, 0, -s);
            leaf.cubicTo(s * 0.7f, -s * 0.16f, s * 0.9f, s * 0.42f, 0, s);
        }
        leaf.close();
        canvas.drawPath(leaf, paint);
    }

    private int[] colorsForSeason() {
        if (options.season == 0) return SPRING;
        if (options.season == 1) return SUMMER;
        if (options.season == 3) return WINTER;
        return AUTUMN;
    }

    private int sceneEnvironment(float time) {
        if (!options.rotateScenes) return options.environment;
        int turn = (int) (time / 90f);
        return (options.environment + turn) % 3;
    }

    private static int alpha(int color, int amount) {
        return (color & 0x00ffffff) | (Math.max(0, Math.min(255, amount)) << 24);
    }
}
