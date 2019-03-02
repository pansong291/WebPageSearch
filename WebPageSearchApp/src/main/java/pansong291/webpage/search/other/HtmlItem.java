package pansong291.webpage.search.other;

import java.util.HashMap;

public class HtmlItem
{
 String item;
 String itemName;
 String itemValue;
 HashMap<String, String> kv;

 private MODE currentMode;
 private static enum MODE
 {
  ITEM_NAME_START, ITEM_NAME_END, ITEM_VALUE, KEY, VALUE
 }

 protected HtmlItem(String item)
 {
  this.item = item.replace("\n", "");
 }

 public HashMap<String, String> getKeyAndValue()
 {
  if(kv == null)
  {
   kv = new HashMap<>();
   StringBuilder sb = new StringBuilder();
   char ch;
   int singleQuoteCount = 0;
   int doubleQuoteCount = 0;
   boolean isLastAppend = false;
   boolean isLastZhuanYi = false; //前一个是否是转义符
   String lastKey = null;
   currentMode = MODE.ITEM_NAME_START;
   for(int i = 0; i < item.length(); i++)
   {
    ch = item.charAt(i);
    if(Character.isWhitespace(ch))
    {
     switch(currentMode)
     {
      case ITEM_NAME_START:
       if(isLastAppend)
       {
        currentMode = MODE.KEY;
        itemName = sb.toString();
        sb.delete(0, sb.length());
       }
       break;
      case ITEM_NAME_END:
       break;
      case ITEM_VALUE:
       sb.append(ch);
       isLastAppend = true;
       break;
      case KEY:
       if(sb.length() > 0)lastKey = sb.toString();
       if(!kv.containsKey(lastKey))
       {
        kv.put(lastKey, "");
        sb.delete(0, sb.length());
       }
       break;
      case VALUE:
       if(singleQuoteCount != 0 || doubleQuoteCount != 0)
       {
        sb.append(ch);
        isLastAppend = true;
        continue;
       }
       if(sb.length() > 0 && kv.containsKey(lastKey) && kv.get(lastKey).equals(""))
       {
        kv.remove(lastKey);
        kv.put(lastKey, sb.toString());
        sb.delete(0, sb.length());
        currentMode = MODE.KEY;
       }
       break;
     }
     continue;
    }
    isLastAppend = false;
    switch(ch)
    {
     case '<':
      if(currentMode == MODE.ITEM_VALUE)
      {
       if(itemValue == null) itemValue = sb.toString();
       sb.delete(0, sb.length());
       currentMode = MODE.ITEM_NAME_END;
      }
      break;
     case '/':
      if(singleQuoteCount != 0 || doubleQuoteCount != 0)
      {
       sb.append(ch);
       isLastAppend = true;
      }
      break;
     case '>':
      if(currentMode == MODE.ITEM_NAME_START)
      {
       if(itemName == null) itemName = sb.toString();
       sb.delete(0, sb.length());
      }else if(currentMode == MODE.KEY)
      {
       if(sb.length() > 0)lastKey = sb.toString();
       if(!kv.containsKey(lastKey))
       {
        kv.put(lastKey, "");
        sb.delete(0, sb.length());
       }
      }else if(currentMode == MODE.VALUE)
      {
       if(kv.containsKey(lastKey) && kv.get(lastKey).equals(""))
       {
        kv.remove(lastKey);
        kv.put(lastKey, sb.toString());
        sb.delete(0, sb.length());
       }
      }
      currentMode = MODE.ITEM_VALUE;
      break;
     case '=':
      if(currentMode == MODE.KEY)
      {
       currentMode = MODE.VALUE;
       if(sb.length() > 0)lastKey = sb.toString();
       if(!kv.containsKey(lastKey))
       {
        kv.put(lastKey, "");
        sb.delete(0, sb.length());
       }
      }else
      {
       sb.append(ch);
       isLastAppend = true;
      }
      break;
     case '\\':
      sb.append(ch);
      isLastAppend = true;
      isLastZhuanYi = true;
      continue;
     case '\'':
      if(!isLastZhuanYi)
      {
       if(singleQuoteCount == 0) singleQuoteCount++;
       else singleQuoteCount--;
      }
      sb.append(ch);
      isLastAppend = true;
      break;
     case '\"':
      if(!isLastZhuanYi)
      {
       if(doubleQuoteCount == 0) doubleQuoteCount++;
       else doubleQuoteCount--;
      }
      sb.append(ch);
      isLastAppend = true;
      break;
     default:
      switch(currentMode)
      {
       case ITEM_NAME_START:
       case ITEM_VALUE:
       case KEY:
       case VALUE:
        sb.append(ch);
        isLastAppend = true;
        break;
       case ITEM_NAME_END:
        break;
      }
    }
    isLastZhuanYi = false;
   }
  }
  return kv;
 }
 
 public String getItem()
 {
  return item;
 }

 public String getItemName()
 {
  if(itemName == null)
  {
   StringBuilder sb = new StringBuilder();
   char ch;
   for(int i = 0; i < item.length(); i++)
   {
    ch = item.charAt(i);
    if(ch == '<' || Character.isWhitespace(ch))
    {
     if(sb.length() == 0) continue;
     else break;
    }
    sb.append(ch);
   }
   itemName = sb.toString();
  }
  return itemName;
 }

 public String getItemValue()
 {
  if(itemValue == null)
  {
   int index1 = item.indexOf('>');
   int index2 = item.lastIndexOf('<');
   if(index1 > index2)
    itemValue = "";
   else
    itemValue = item.substring(index1 + 1, index2);
  }
  return itemValue;
 }
 
}
