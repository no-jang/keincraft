package client.graphics.vk.pipeline;

import client.graphics.vk.device.Device;

public class Pipeline {
    private final long pipelineHandle;
    private final long pipelineLayoutHandle;

    public Pipeline() {
        pipelineHandle = 0L;
        pipelineLayoutHandle = 0L;
    }

    public void destroy(Device device) {

    }

    public long getHandle() {
        return pipelineHandle;
    }

    public long getLayoutHandle() {
        return pipelineLayoutHandle;
    }
}
