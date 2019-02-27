package pansong291.webpage.search.task;

import android.os.AsyncTask;
import android.os.Message;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import pansong291.webpage.search.MyApplication;
import pansong291.webpage.search.activity.MainActivity;
import pansong291.webpage.search.view.LineEditText;


public class WebSearchTask extends AsyncTask
{ 
 MainActivity ma;

 @Override
 protected void onPreExecute()
 {
  ma.setStartButtonEnable(false);
 }

 @Override
 protected void onCancelled(Object result)
 {
  ma.setStartButtonEnable(true);
  ma.edt_progress.setText(LineEditText.TEXT_APPEND_NEW_LINE, "已停止搜索，" + result);
 }

 @Override
 protected String doInBackground(Object[] p1)
 {
  int i1 = p1[2], i2 = p1[3];
  int count[] = new int[2];
  if(i1 < i2) for(int i = i1; i <= i2; i++)
  {
   searchWebSourceCode((String)p1[0], i, (String)p1[1], count);
  }else for(int i = i1; i >= i2; i--)
  {
   searchWebSourceCode((String)p1[0], i, (String)p1[1], count);
  }
  return "共搜到" + count[0] + "个结果，" + count[1] + "个页面超时";
 }

 @Override
 protected void onProgressUpdate(Object[] values)
 {
  ma.edt_progress.setText(values[0], (String)values[1]);
 }

 @Override
 protected void onPostExecute(Object result)
 {
  ma.edt_progress.setText(LineEditText.TEXT_APPEND_NEW_LINE, "搜索完毕，" + result);
  ma.setStartButtonEnable(true);
 }
 
 private void searchWebSourceCode(String url, int page, String key, int[] count)
 {
  String res = null;
  int rCount = 0, index = -1;
  if(!isCancelled()) try
  {
   publishProgress(LineEditText.TEXT_APPEND_NEW_LINE, "正在获取第" + page + "页");
   
   HttpGet hg = new HttpGet(getURL(url, page));
   HttpConnectionParams.setConnectionTimeout(hg.getParams(), 3000);
   HttpConnectionParams.setSoTimeout(hg.getParams(), 3000);
   HttpClient client = new DefaultHttpClient();
   HttpResponse response = client.execute(hg);
   res = EntityUtils.toString(response.getEntity(), "utf-8");
   
   for(;;)
   {
    index = res.indexOf(key, index + 1);
    if(index < 0) break;
    rCount++;
   }
   
   if(rCount > 0)
   {
    publishProgress(LineEditText.TEXT_REPLACE_LAST_LINE, "已在第" + page + "页找到" + rCount + "个");
    count[0] += rCount;
   }else
    publishProgress(LineEditText.TEXT_REPLACE_LAST_LINE, "");
  }catch(Exception e)
  {
   publishProgress(LineEditText.TEXT_REPLACE_LAST_LINE, "第" + page + "页获取超时");
   count[1]++;
   System.err.println(MyApplication.TAG_LOG_PACKAGE_ERROR);
   e.printStackTrace();
  }
 }
 
 private String getURL(String url, int page)
 {
  return url.replace("页数", String.valueOf(page));
 }
 
 public WebSearchTask(MainActivity m)
 {
  ma = m;
 }
 
}
