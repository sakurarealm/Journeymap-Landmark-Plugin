package com.sakurarealm.jmlandmark.common.utils;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.IOverlayListener;
import journeymap.client.api.display.ImageOverlay;
import journeymap.client.api.model.MapImage;
import journeymap.client.api.util.UIState;
import net.minecraft.util.math.BlockPos;

import java.awt.geom.Point2D;

public class MarkerOverlayFactory {

    public final static double SIZE = 32;

    public static void calculateDisplayRange(ImageOverlay overlay, BlockPos center, double size, double blockSize) {
        double scaledSize = size / blockSize;

        BlockPos northWest = new BlockPos(Math.floor(center.getX() - scaledSize / 2), center.getY(), Math.floor(center.getZ() - scaledSize / 2));
        BlockPos southEast = new BlockPos(Math.ceil(center.getX() + scaledSize / 2), center.getY(), Math.ceil(center.getZ() + scaledSize / 2));

        overlay.setNorthWestPoint(northWest).setSouthEastPoint(southEast);
    }

    public static ImageOverlay create(IClientAPI clientAPI, String name, String hoverText, BlockPos pos, MapImage mapImage) {
        ImageOverlay imageOverlay = new ImageOverlay(JMLandmarkMod.MODID, name, pos, pos, mapImage);

        calculateDisplayRange(imageOverlay, pos, SIZE, 8.0);
        imageOverlay.setDimension(0).setTitle(hoverText).setLabel("");
        imageOverlay.setDisplayOrder(102);
        imageOverlay.setOverlayListener(new MarkerListener(clientAPI, imageOverlay, SIZE, pos));

        return imageOverlay;
    }

    public static class MarkerListener implements IOverlayListener {
        final IClientAPI clientAPI;
        final ImageOverlay overlay;
        final int color;
        final double size;
        final String title;
        final BlockPos center;

        Runnable onClickListener;

        MarkerListener(IClientAPI clientAPI, ImageOverlay overlay, double size, BlockPos center) {
            this.clientAPI = clientAPI;
            this.overlay = overlay;
            this.color = overlay.getImage().getColor();
            this.title = overlay.getTitle();
            this.size = size;
            this.center = center;
        }

        public void setOnClickListener(Runnable runnable) {
            onClickListener = runnable;
        }

        @Override
        public void onActivate(UIState uiState) {
            refresh(uiState);
        }

        @Override
        public void onDeactivate(UIState uiState) {

        }

        @Override
        public void onMouseMove(UIState uiState, Point2D.Double aDouble, BlockPos blockPos) {
            double size = this.size * 1.2; // Scale the icon larger
            // Make the icon larger and display the text
            if (overlay.getImage().getDisplayWidth() != size) {

                calculateDisplayRange(overlay, center, size, uiState.blockSize);
                overlay.getImage()
                        .setOpacity(1.f);
                overlay.getTextProperties()
                        .setColor(0xffffff)
                        .setBackgroundOpacity(.5f)
                        .setOpacity(0.f);

                overlay.flagForRerender();
            }
        }

        @Override
        public void onMouseOut(UIState uiState, Point2D.Double aDouble, BlockPos blockPos) {
            refresh(uiState);
        }

        @Override
        public boolean onMouseClick(UIState uiState, Point2D.Double aDouble, BlockPos blockPos, int i, boolean b) {
            if (onClickListener != null) {
                onClickListener.run();
            }

            return false;
        }

        public void refresh(UIState uiState) {
            // JourneyMapPluginMod.getLogger().info("Refreshing.!");
            JMLandmarkMod.getLogger().warn(overlay.getImage().getAnchorX() + " " + overlay.getImage().getAnchorY() + " " + overlay.getImage().getTextureX());
            overlay.getImage()
                    .setColor(color)
                    .setOpacity(0.9f);

            calculateDisplayRange(overlay, center, size, uiState.blockSize);

            overlay.flagForRerender();
        }
    }
}
