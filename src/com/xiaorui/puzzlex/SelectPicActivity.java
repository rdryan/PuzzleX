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

//import net.youmi.android.banner.AdSize;
//import net.youmi.android.banner.AdView;
//import net.youmi.android.diy.banner.DiyAdSize;
//import net.youmi.android.diy.banner.DiyBanner;
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
	      
		  // ʵ�����Զ�������
		  //LinearLayout adLayout=(LinearLayout)findViewById(R.id.AdLayout2);
		  //DiyBanner banner = new DiyBanner(this, DiyAdSize.SIZE_MATCH_SCREENx32);
		  //adLayout.addView(banner);
		
		  // ʵ���������
		  //AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		  //LinearLayout adLayout=(LinearLayout)findViewById(R.id.AdLayout2);
		  //adLayout.addView(adView);

	      
	        GridView gv = (GridView)findViewById(R.id.GridView1); 
	        //ΪGridView���������� 
	        gv.setAdapter(new MyAdapter(this)); 
	        
	        //ע������¼� 
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

//�Զ��������� 
class MyAdapter extends BaseAdapter{ 
    //�����Ķ��� 
    private Context context; 
    //ͼƬ���� 
    private Integer[] imgs = { 
            R.drawable.s0, R.drawable.s1, R.drawable.s2, 
            R.drawable.s3, R.drawable.s4, R.drawable.s5,
            R.drawable.s6, R.drawable.s7, R.drawable.s8,
            R.drawable.s9, R.drawable.s10,
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
     
    //����View���� 
    public View getView(int position, View convertView, ViewGroup parent) { 
         ImageView imageView; 
            if (convertView == null) { 
                imageView = new SquareImageView(context); 
                //imageView.setLayoutParams(new GridView.LayoutParams(320,320));//����ImageView���󲼾� 
                imageView.setAdjustViewBounds(false);//���ñ߽���� 
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//���ÿ̶ȵ����� 
                imageView.setPadding(8, 8, 8, 8);//���ü�� 
            }  
            else { 
                imageView = (SquareImageView) convertView; 
            } 
            imageView.setImageResource(imgs[position]);//ΪImageView����ͼƬ��Դ 
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
