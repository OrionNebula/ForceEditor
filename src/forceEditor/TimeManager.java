package forceEditor;

public class TimeManager {
	private static long lastTime;
	private static long delta;
	
	public static long getTime() {
		return System.currentTimeMillis();
	}
	
	public static void initialize() {
		lastTime = getTime();
	}
	
	public static void update() {
		long currentTime = getTime();
		delta = currentTime - lastTime;
		lastTime = currentTime;
	}
	
	public static double getDelta() {
		return delta / 1000.0;
	}
}
