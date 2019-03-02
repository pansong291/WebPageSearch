package pansong291.webpage.search.other;

import java.util.List;
import java.util.ArrayList;

public class HtmlUtils
{
 private static enum MODE
 {
  MATCH_NAME, MATCH_PARAM, MATCH_END, NONE
 }

 public static List<HtmlItem> getHtmlItems(String html, String name, String... params)
 {
  List<HtmlItem> list = new ArrayList<>();
  int index1 = 0, index2 = 0;
  char ch;
  MODE currentMode = MODE.NONE;
  int paramsCount = getIncludeParamsCount(params);
  int name_match_index = 0;
  int param_match_index = 0;
  int found_param_count = 0;
  boolean isLastDivision = false;
  boolean shouldRecord = false;
  for (int i = 0; i < html.length(); i++)
  {
   ch = html.charAt(i);
   if (Character.isWhitespace(ch))
   {
    continue;
   }
   switch (ch)
   {
    case '<':
     if (currentMode != MODE.MATCH_END)
     {
      currentMode = MODE.MATCH_NAME;
      index1 = i;
      found_param_count = 0;
      //log("find < , start MATCH_NAME mode");
     }
     break;

    case '/':
     if (currentMode == MODE.MATCH_PARAM)
     {
      isLastDivision = true;
      continue;
     }
     break;

    case '>':
     switch (currentMode)
     {
      case MATCH_NAME:
       break;

      case MATCH_PARAM:
       currentMode = MODE.MATCH_END;
       //log("match end");
       //log("found_param_coount = " + found_param_count);
       if (found_param_count == paramsCount)//params.length)
       {
        shouldRecord = true;
        //log("record next > index");
       }
       if (!isLastDivision) break;

      case MATCH_END:
       if (shouldRecord)
       {
        index2 = i;
        shouldRecord = false;
        //log("the > index is " + i);
        String str = html.substring(index1, index2 + 1);
        if (!isExclude(params, str))
        {
         //log(str);
         list.add(new HtmlItem(str));
        }
       }
       currentMode = MODE.NONE;
       break;

      case NONE:
       break;
     }
     break;
    default:
     switch (currentMode)
     {
      case MATCH_NAME:
       if (ch == name.charAt(name_match_index++))
       {
        //log("match " + ch + " at " + (name_match_index - 1));
        if (name_match_index == name.length())
        {
         name_match_index = 0;
         currentMode = MODE.MATCH_PARAM;
         //log("name matched success, start MATCH_PARAM mode");
        }
       }else
       {
        name_match_index = 0;
        currentMode = MODE.NONE;
        //log("name matched failed, restart MATCH_NAME mode");
       }
       break;

      case MATCH_PARAM:
       int singleQuoteCount = 0;
       int doubleQuoteCount = 0;
       char cj, cp;
       for (int k = 0; k < params.length; k++)
       {
        for (int j = i; param_match_index < params[k].length(); j++)
        {
         cj = html.charAt(j);
         cp = params[k].charAt(param_match_index);
         if (cp == '-') break;
         if (cj == '\'' && html.charAt(j - 1) != '\\')
         {
          if (singleQuoteCount == 0) singleQuoteCount++;
          else singleQuoteCount--;
         }
         if (cj == '\"' && html.charAt(j - 1) != '\\')
         {
          if (doubleQuoteCount == 0) doubleQuoteCount++;
          else doubleQuoteCount--;
         }
         if (Character.isWhitespace(cj) && singleQuoteCount == 0 && doubleQuoteCount == 0) continue;
         if (cj != cp) break;
         param_match_index++;
        }
        if (param_match_index == params[k].length())
        {
         found_param_count++;
         i += param_match_index - 1;
         //log("param matched success");
        }else
        {
         //log("param matched failed, restart MATCH_PARAM mode");
        }
        param_match_index = 0;
       }
       break;

      case MATCH_END:
       break;

      case NONE:
       break;
     }
   }
   isLastDivision = false;
  }
  return list;
 }

 private static void log(String str)
 {
  System.out.println(str);
 }

 private static int getIncludeParamsCount(String[] params)
 {
  int c = 0;
  for (int i = 0; i < params.length; i++)
  {
   if (params[i].charAt(0) != '-') c++;
  }
  //log("paramLenth = " + c);
  return c;
 }

 private static boolean isExclude(String[] params, String item)
 {
  for (int i = 0; i < params.length; i++)
  {
   if (params[i].charAt(0) == '-' && item.indexOf(params[i].substring(1)) >= 0)
    return true;
  }
  return false;
 }
 
}
