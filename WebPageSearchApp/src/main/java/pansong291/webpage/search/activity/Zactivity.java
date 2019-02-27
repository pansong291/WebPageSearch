package pansong291.webpage.search.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import pansong291.crash.ASControl;
import android.content.SharedPreferences;

public class Zactivity extends Activity
{
 public static final String Z_WEB_URL = "web_url",
  Z_SEARCH_KEY = "search_key", Z_RANGE1 = "range1",
  Z_RANGE2 = "range2", Z_PROGRESS = "progress";
 SharedPreferences sp;

 @Override
 protected void onResume()
 {
  super.onResume();
  
 }
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  ASControl.getASControl().addActivity(this);
  sp = getSharedPreferences(getPackageName() + "_preferences", 0);
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ASControl.getASControl().removeActivity(this);
 }
 
 public void toast(String s)
 {
  toast(s, 0);
 }
 
 public void toast(String s, int i)
 {
  Toast.makeText(this, s, i).show();
 }
 
}
