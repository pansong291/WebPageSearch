package pansong291.webpage.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.text.Editable;

public class LineEditText extends EditText
{
 public static final int TEXT_APPEND_NEW_LINE = -1, TEXT_REPLACE_LAST_LINE = -2;
 
 public void setText(int line, CharSequence chars)
 {
  Editable txt = getText();
  int length = length();
  int lineCount = getLineCount();
  int l1 = 0, l2 = 0;
  if(line < 0)
  {
   switch(line)
   {
    case TEXT_APPEND_NEW_LINE:
     if(length > 0 && txt.charAt(length - 1) != '\n')
      txt.append('\n');
     txt.append(chars);
     break;
    case TEXT_REPLACE_LAST_LINE:
     setText(lineCount - 1, chars);
     break;
   }
  }else if(line < lineCount)
  {
   l1 = indexOfLineFeed(line - 1);
   l2 = indexOfLineFeed(line);
   if(l2 - l1 == 1)
    txt.insert(l1 + 1, chars);
   else
    txt.replace(l1 + 1, l2, chars);
  }else
  {
   for(int i = line - lineCount; i >= 0; i--)
    txt.append('\n');
   txt.append(chars);
  }
 }
 
 public int indexOfLineFeed(int which)
 {
  Editable txt = getText();
  int length = length();
  int lineCount = getLineCount();
  if(which >= lineCount - 1) return length;
  
  int index = -1;
  if(which < lineCount / 2) for(int i = 0; which >= 0 && i < length; i++)
  {
   if(txt.charAt(i) == '\n')
   {
    index = i;
    which--;
   }
  }else for(int i = length - 1; which < lineCount - 1 && i >= 0; i--)
  {
   if(txt.charAt(i) == '\n')
   {
    index = i;
    which++;
   }
  }
  return index;
 }
  
 public LineEditText(Context context)
 {
  super(context);
 }

 public LineEditText(Context context, AttributeSet attrs)
 {
  super(context, attrs);
 }

 public LineEditText(Context context, AttributeSet attrs, int defStyleAttr)
 {
  super(context, attrs, defStyleAttr);
 }

 public LineEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
 {
  super(context, attrs, defStyleAttr, defStyleRes);
 }
}
