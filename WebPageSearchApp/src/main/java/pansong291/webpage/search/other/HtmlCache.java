package pansong291.webpage.search.other;

import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HtmlCache
{
 private static final String URL = "url", PAGES = "pages";
 String url;
 HashMap<Integer, String> pages;
 
 public HtmlCache(String u)
 {
  url = u;
  pages = new HashMap<>();
 }
 
 public HtmlCache(SharedPreferences sp)
 {
  url = sp.getString(URL, "");
  pages = new HashMap<>();
  Set<String> set = sp.getStringSet(PAGES, null);
  if(set != null && set.size() > 0)
  {
   for(String s : set)
   {
    int i = s.indexOf("=");
    set(Integer.parseInt(s.substring(0, i)), s.substring(i + 1));
   }
  }
 }
 
 public boolean has(int i)
 {
  return pages.containsKey(i);
 }
 
 public int size()
 {
  return pages.size();
 }
 
 public String set(int i, List<HtmlItem> list)
 {
  StringBuilder sb = new StringBuilder();
  for(HtmlItem hi : list)
  {
   sb.append(hi.getItemValue());
   sb.append('\n');
  }
  return set(i, sb.toString());
 }
 
 public String set(int i, String s)
 {
  if(pages.containsKey(i)) pages.remove(i);
  pages.put(i, s);
  return s;
 }
 
 public String get(int i)
 {
  return pages.get(i);
 }
 
 public void setURL(String u)
 {
  url = u;
 }
 
 public String getURL()
 {
  return url;
 }
 
 public void save(SharedPreferences sp)
 {
  Set<String>sets = new HashSet<>();
  Iterator it = pages.entrySet().iterator();
  while(it.hasNext())
  {
   sets.add(it.next().toString());
  }

  sp.edit().putString(URL, url)
  .putStringSet(PAGES, sets)
  .commit();
 }
 
 public void clear(SharedPreferences sp)
 {
  url = null;
  pages.clear();
  sp.edit().remove(URL)
  .remove(PAGES).commit();
 }
 
}
