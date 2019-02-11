package accessibility.greate.com.cn.myhappyaccessibility;

import android.accessibilityservice.AccessibilityService;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;


public class GlobalAccessibility extends AccessibilityService {

	private static final String TAG = "GlobalAccessibility";
    private ScanKeyEventHelper mScanGun = null;


	@Override
	protected boolean onKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		/// 59 是 shift 键值，这个无论 按下还是抬起都需要传入，否则大于写会有错误，另外扫码枪，只在windows平台支持中文，android ios 不支持。如果要传入中文可以使用base64加密传入后解密
		if (event.getAction() == KeyEvent.ACTION_DOWN || event.getKeyCode()==59)
		{
			if (keyCode <= 6) {
				return false;
			}
			if (mScanGun.isMaybeScanning(keyCode, event)) {
				return true;
			}
		}
		return super.onKeyEvent(event);
	}
	
	@Override
	public void onInterrupt() {

	}
	
	@Override
	public void onCreate() {
		Log.e(TAG, "GlobalAccessibility::onCreate");
		mScanGun = new ScanKeyEventHelper(new ScanKeyEventHelper.ScanGunCallBack() {
			@Override
			public void onScanFinish(String scanResult) {
				if (!TextUtils.isEmpty(scanResult)) {
					Log.e(TAG,scanResult);
					Log.e(TAG,new String(Base64.decode(scanResult.getBytes(), Base64.DEFAULT)));

					Toast.makeText(GlobalAccessibility.this.getBaseContext(),
							"scan content:" + scanResult, Toast.LENGTH_SHORT).show();

				}
			}
		});
		mScanGun.setMaxKeysInterval(50);
		super.onCreate();
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
	//	Log.e(TAG, "onAccessibilityEvent:"+event.getEventType());
		int eventType = event.getEventType();
//	    Log.e(TAG, "eventType:" + eventType);
		if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			ComponentName componentName = new ComponentName(
					event.getPackageName().toString(),
					event.getClassName().toString()
			);

			ActivityInfo activityInfo = tryGetActivity(componentName);
//           获取 顶部ACTIIVTY
			boolean isActivity = activityInfo != null;
			if (isActivity)
			{
				Log.e("CurrentActivity", componentName.flattenToShortString());
			}
		}
	}

	private ActivityInfo tryGetActivity(ComponentName componentName) {
		try {
			return getPackageManager().getActivityInfo(componentName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}
}
