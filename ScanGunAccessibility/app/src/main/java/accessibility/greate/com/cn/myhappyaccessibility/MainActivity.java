package accessibility.greate.com.cn.myhappyaccessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.util.List;

import accessibility.ihappy.com.cn.myhappyaccessibility.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openService(View view)
    {
        openAccessibilityServiceSetting();
    }
    /*
  *  开启 AccessibilityService 用于监控最上层 Activity
  */
    private void openAccessibilityServiceSetting() {
        if(!AccessibilityServiceEnabled(GlobalAccessibility.class.getSimpleName()))
        {
            try {
                Intent intent = new Intent(
                        android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "找到按键监听辅助，开启即可", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "你已经开启了按键监听辅助", Toast.LENGTH_LONG).show();
        }

    }

    /*
      * 判断 AccessibilityService 是否开启
     */
    private boolean AccessibilityServiceEnabled(String name) {
        AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);

        for (AccessibilityServiceInfo info : serviceInfos) {
            Log.d("MainActivity", "all -->" + info.getId());
            if (info.getId().contains(name)) {
                return true;
            }
        }
        return false;
    }

}
