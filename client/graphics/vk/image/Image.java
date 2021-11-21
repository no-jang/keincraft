package client.graphics.vk.image;

// TODO Finish
public class Image {
    protected final long image;
    protected final long sampler;
    protected final long view;

    protected Image(long image, long sampler, long view) {
        this.image = image;
        this.sampler = sampler;
        this.view = view;
    }

    public long getImage() {
        return image;
    }

    public long getSampler() {
        return sampler;
    }

    public long getView() {
        return view;
    }
}
