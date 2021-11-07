package client.render;

import static org.lwjgl.bgfx.BGFX.*;

public class RenderManager {
    public RenderManager() {
        bgfx_set_debug(BGFX_DEBUG_TEXT);

        bgfx_set_view_clear(0, BGFX_CLEAR_COLOR | BGFX_CLEAR_DEPTH, 0x303030ff, 1.0f, 0);
    }

    public void render(Window window) {
        bgfx_set_view_rect(0, 0, 0, window.getWidth(), window.getHeight());

        bgfx_touch(0);

        bgfx_dbg_text_clear(0, false);

        bgfx_dbg_text_printf(0, 1, 0x1f, "bgfx/examples/25-c99");
        bgfx_dbg_text_printf(0, 2, 0x3f, "Description: Initialization and debug text with C99 API.");

        bgfx_dbg_text_printf(0, 3, 0x0f, "Color can be changed with ANSI \u001b[9;me\u001b[10;ms\u001b[11;mc\u001b[12;ma\u001b[13;mp\u001b[14;me\u001b[0m code too.");

        bgfx_dbg_text_printf(80, 4, 0x0f, "\u001b[;0m    \u001b[;1m    \u001b[; 2m    \u001b[; 3m    \u001b[; 4m    \u001b[; 5m    \u001b[; 6m    \u001b[; 7m    \u001b[0m");
        bgfx_dbg_text_printf(80, 5, 0x0f, "\u001b[;8m    \u001b[;9m    \u001b[;10m    \u001b[;11m    \u001b[;12m    \u001b[;13m    \u001b[;14m    \u001b[;15m    \u001b[0m");

        bgfx_frame(false);
    }

    public void destroy() {

    }
}
