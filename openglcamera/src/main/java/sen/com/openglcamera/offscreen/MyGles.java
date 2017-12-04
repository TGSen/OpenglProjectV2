package sen.com.openglcamera.offscreen;

public class MyGles {
	static
	{
		System.loadLibrary("sgles");
	}
	public static native void init();
	public static native void draw();
	public static native void release();
}
