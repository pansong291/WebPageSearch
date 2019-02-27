package pansong291.webpage.search;

import pansong291.webpage.search.activity.CrashActivity;
import pansong291.crash.CrashApplication;

public class MyApplication extends CrashApplication
{
 public static final String TAG_LOG_PACKAGE_ERROR = "pansong291.webpage.search error";
 
 @Override
 public Class<?> getPackageActivity()
 {
  setShouldShowDeviceInfo(false);
  return CrashActivity.class;
 }

}
