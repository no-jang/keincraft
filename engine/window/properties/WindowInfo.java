package engine.window.properties;

public class WindowInfo {
    private final int width;
    private final int height;
    private final String title;

    public WindowInfo(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "WindowInfo[" +
                "width=" + width +
                ", height=" + height +
                ", title='" + title +
                ']';
    }

    public static class Builder {
        private int width;
        private int height;
        private String title;

        public Builder() {
            width = 900;
            height = 700;
            title = "";
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public WindowInfo build() {
            return new WindowInfo(width, height, title);
        }
    }
}
