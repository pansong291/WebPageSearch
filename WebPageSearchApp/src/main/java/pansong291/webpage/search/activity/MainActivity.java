package pansong291.webpage.search.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import pansong291.webpage.search.R;
import pansong291.webpage.search.other.HtmlCache;
import pansong291.webpage.search.task.WebSearchTask;
import pansong291.webpage.search.view.LineEditText;

public class MainActivity extends Zactivity 
{
 EditText edt_website, edt_searchKey, edt_range1, edt_range2;
 Button btn_start, btn_stop;
 public LineEditText edt_progress;
 
 AlertDialog alertDlg_help, alertDlg_chache_rules;
 
 View dialog_cache_rules;
 EditText edt_cache_name, edt_cache_params;
 
 WebSearchTask wsTask;
 
 public HtmlCache htmlCache;
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  
  initView();
  initEvent();
  initValue();
 }
 
 private void initView()
 {
  edt_website = findViewById(R.id.edt_website);
  edt_searchKey = findViewById(R.id.edt_searchkey);
  edt_range1 = findViewById(R.id.edt_range1);
  edt_range2 = findViewById(R.id.edt_range2);
  edt_progress = findViewById(R.id.edt_progress);
  btn_start = findViewById(R.id.btn_start);
  btn_stop = findViewById(R.id.btn_stop);
  
  dialog_cache_rules = LayoutInflater.from(this).inflate(R.layout.dialog_cache_rules, null);
  edt_cache_name = dialog_cache_rules.findViewById(R.id.edt_cache_name);
  edt_cache_params = dialog_cache_rules.findViewById(R.id.edt_cache_params);
 }
 
 private void initEvent(){}
 
 private void initValue()
 {
  edt_website.setText(sp.getString(Z_WEB_URL, ""));
  edt_searchKey.setText(sp.getString(Z_SEARCH_KEY, ""));
  edt_range1.setText(String.valueOf(sp.getInt(Z_RANGE1, 1)));
  edt_range2.setText(String.valueOf(sp.getInt(Z_RANGE2, 2)));
  edt_progress.setText(sp.getString(Z_PROGRESS, ""));
  
  htmlCache = new HtmlCache(sp);
  
  edt_cache_name.setText(sp.getString(Z_CACHE_NAME, ""));
  edt_cache_params.setText(sp.getString(Z_CACHE_PARAMS, ""));
 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu)
 {
  menu.add(0, 1, 0, "帮助");
  menu.add(0, 2, 0, "设置缓存规则");
  return super.onCreateOptionsMenu(menu);
 }

 @Override
 public boolean onOptionsItemSelected(MenuItem item)
 {
  switch(item.getItemId())
  {
   case 1:
    if(alertDlg_help == null)
     alertDlg_help = new AlertDialog.Builder(this)
      .setMessage("将网址中表示页面数的数字替换成\"页数\"二字，再设置页数范围即可搜索内容。\n\n例如 http://xxx.com/pages-57.html \n改成 http://xxx.com/pages-页数.html\n\n适用情形：例如某些论坛未登录时不可搜索内容，而注册需要购买其邀请码，此时可用本软件进行搜索。\n页数范围前面的数字大于后面的数字时可进行逆序搜索。")
      .setPositiveButton("确定", null)
      .create();
    alertDlg_help.show();
    break;
    
   case 2:
    if(alertDlg_chache_rules == null)
     alertDlg_chache_rules = new AlertDialog.Builder(this)
      .setView(dialog_cache_rules)
      .setPositiveButton("确定", null)
      .create();
    alertDlg_chache_rules.show();
    break;
  }
  
  return super.onOptionsItemSelected(item);
 }
 
 public void onStartClick(View v)
 {
  if(edt_website.length() <= 0 || edt_searchKey.length() <= 0)
  {
   toast("输入不能为空");
   return;
  }
  int range1 = getWebPageCount(edt_range1);
  int range2 = getWebPageCount(edt_range2);
  if(range1 <= 0 || range2 <= 0)
  {
   toast("页数不正确");
   return;
  }

  String web = edt_website.getText().toString();
  String key = edt_searchKey.getText().toString();
  String name = edt_cache_name.getText().toString();
  String params = edt_cache_params.getText().toString();
  
  edt_progress.setSelection(0);
  sp.edit().putString(Z_WEB_URL, web)
  .putString(Z_SEARCH_KEY, key)
  .putInt(Z_RANGE1, range1).putInt(Z_RANGE2, range2)
  .putString(Z_CACHE_NAME, name)
  .putString(Z_CACHE_PARAMS, params)
  .commit();
  
  wsTask = new WebSearchTask(this);
  wsTask.execute(web, key, range1, range2, name, params.split("\n"));
 }
 
 public void onStopClick(View v)
 {  
  if(!wsTask.isCancelled())
   wsTask.cancel(false); // 停止操作时不停止线程
 }
 
 public void onClearCacheClick(View v)
 {
  htmlCache.clear(sp);
 }
 
 public void onClearClick(View v)
 {
  edt_progress.getText().clear();
 }
 
 public void setStartButtonEnable(boolean b)
 {
  btn_start.setEnabled(b);
  btn_stop.setEnabled(!b);
 }

 public void saveHtmlCache()
 {
  htmlCache.save(sp);
 }
 
 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  sp.edit().putString(Z_PROGRESS, edt_progress.getText().toString())
  .commit();
 }
 
 private int getWebPageCount(EditText edt)
 {
  if(edt.length() <= 0) return 0;
  return Integer.parseInt(edt.getText().toString());
 }
 
}
