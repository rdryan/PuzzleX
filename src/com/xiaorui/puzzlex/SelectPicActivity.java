/*
Copyright (C) 2014  Xiao Rui

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiaorui.puzzlex; 

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.diy.banner.DiyAdSize;
import net.youmi.android.diy.banner.DiyBanner;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public final class SelectPicActivity extends Activity {
	   
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.picgrid);
	      
		  // 实例化自定义广告条
		  //LinearLayout adLayout=(LinearLayout)findViewById(R.id.AdLayout2);
		  //DiyBanner banner = new DiyBanner(this, DiyAdSize.SIZE_MATCH_SCREENx32);
		  //adLayout.addView(banner);
		
		  // 实例化广告条
		  AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		  LinearLayout adLayout=(LinearLayout)findViewById(R.id.AdLayout2);
		  adLayout.addView(adView);

	      
	        GridView gv = (GridView)findViewById(R.id.GridView1); 
	        //为GridView设置适配器 
	        gv.setAdapter(new MyAdapter(this)); 
	        
	        //注册监听事件 
	        gv.setOnItemClickListener(new OnItemClickListener() 
	        { 
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	            { 
	                //Toast.makeText(SelectPicActivity.this, "pic" + position, Toast.LENGTH_SHORT).show();
	                
	                Intent i = new Intent(SelectPicActivity.this, PuzzleActivity.class);
	                Bundle bundle = new Bundle();
	                bundle.putInt("picID", position);
	                i.putExtras(bundle);
	                startActivity(i);
	            } 
	        }); 
	    } 

}

//自定义适配器 
class MyAdapter extends BaseAdapter{ 
    //上下文对象 
    private Context context; 
    //图片数组 
    private Integer[] imgs = { 
            R.drawable.pic0, R.drawable.pic1, R.drawable.pic2, 
            R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,
            R.drawable.pic6, R.drawable.pic7, R.drawable.pic8,
            R.drawable.pic9, R.drawable.pic10,
    }; 

    MyAdapter(Context context){ 
        this.context = context; 
    } 
    public int getCount() { 
        return imgs.length; 
    } 

    public Object getItem(int item) { 
        return item; 
    } 

    public long getItemId(int id) { 
        return id; 
    } 
     
    //创建View方法 
    public View getView(int position, View convertView, ViewGroup parent) { 
         ImageView imageView; 
            if (convertView == null) { 
                imageView = new SquareImageView(context); 
                //imageView.setLayoutParams(new GridView.LayoutParams(320,320));//设置ImageView对象布局 
                imageView.setAdjustViewBounds(false);//设置边界对齐 
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
                imageView.setPadding(8, 8, 8, 8);//设置间距 
            }  
            else { 
                imageView = (SquareImageView) convertView; 
            } 
            imageView.setImageResource(imgs[position]);//为ImageView设置图片资源 
            return imageView; 
    }
    
 
 public class SquareImageView extends ImageView {
        public SquareImageView(Context context) {
            super(context);
        }
        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
        }
    }    
    
} 
