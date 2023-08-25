package com.sakurarealm.jmlandmark.common.utils;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.IOverlayListener;
import journeymap.client.api.display.MarkerOverlay;
import journeymap.client.api.model.MapImage;
import journeymap.client.api.util.UIState;
import net.minecraft.util.math.BlockPos;

import java.awt.geom.Point2D;

public class MarkerOverlayFactory {

    public static MarkerOverlay create(IClientAPI clientAPI, String name, String hoverText, BlockPos pos, MapImage mapImage) {
        mapImage.setAnchorX(mapImage.getDisplayWidth() / 2)
                .setAnchorY(mapImage.getAnchorY() / 2)
                .setRotation(0);

        MarkerOverlay markerOverlay = new MarkerOverlay(
                JMLandmarkMod.MODID,
                name,
                pos, mapImage);

        markerOverlay.setDimension(0).setTitle(hoverText).setLabel("");
        markerOverlay.setOverlayListener(new MarkerListener(clientAPI, markerOverlay));

        return markerOverlay;
    }

    public static class MarkerListener implements IOverlayListener {

        final IClientAPI clientAPI;
        final MarkerOverlay overlay;
        final int color;
        final double size;
        final String title;

        Runnable onClickListener;

        MarkerListener(IClientAPI clientAPI, MarkerOverlay overlay) {
            this.clientAPI = clientAPI;
            this.overlay = overlay;
            this.color = overlay.getIcon().getColor();
            this.title = overlay.getTitle();
            this.size = overlay.getIcon().getDisplayWidth();
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
            refresh(uiState);
        }

        @Override
        public void onMouseMove(UIState uiState, Point2D.Double aDouble, BlockPos blockPos) {
            double size = this.size * 1.2; // Scale the icon larger
            // Make the icon larger and display the text
            if (overlay.getIcon().getDisplayWidth() != size) {
                overlay.getIcon()
                        .setOpacity(1.f)
                        .setDisplayHeight(size)
                        .setDisplayWidth(size)
                        .setAnchorX(size / 2)
                        .setAnchorY(size / 2);

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

        private void refresh(UIState uiState) {
            // JourneyMapPluginMod.getLogger().info("Refreshing.!");

            overlay.getIcon()
                    .setColor(color)
                    .setOpacity(0.9f)
                    .setDisplayHeight(size)
                    .setDisplayWidth(size)
                    .setAnchorX(size / 2)
                    .setAnchorY(size / 2);;

            overlay.flagForRerender();
        }
    }

}
