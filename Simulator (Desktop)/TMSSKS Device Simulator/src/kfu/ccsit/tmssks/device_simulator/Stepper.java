package kfu.ccsit.tmssks.device_simulator;

public class Stepper {

    public static interface OnEnterFrameListener {

        void onEnterFrame(int frame);
    }

    public static int DEFAULT_FPS = 60;

    private int fps;
    private int frame;
    private boolean running;
    private OnEnterFrameListener callback;

    private Thread thread;

    public int getFPS() {
        return fps;
    }

    public void setFPS(int fps) {
        this.fps = fps;
    }

    public OnEnterFrameListener getOnEnterFrameListener() {
        return callback;
    }

    public void setOnEnterFrameListener(OnEnterFrameListener callback) {
        this.callback = callback;
    }

    public Stepper() {
        this(DEFAULT_FPS);
    }

    public Stepper(int fps) {
        this.fps = fps;
    }

    public void play() {
        running = true;
        thread = new Thread(() -> {
            while (running) {
                if (callback != null) {
                    callback.onEnterFrame(frame++);
                }
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException ex) {
                    System.err.println("Interrupted: " + ex.getMessage());
                }
            }
        });
        thread.start();
    }

    public void stop() {
        frame = 0;
        running = false;
    }

    public void pause() {
        running = false;
    }
    
}
