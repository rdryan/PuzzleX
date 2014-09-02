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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public final class SelectPicActivity extends Activity {
	   
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.picgrid);
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
            R.drawable.pic0, R.drawable.pic1, R.drawable.pic2, 
            R.drawable.pic3, R.drawable.pic4
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
                imageView = new ImageView(context); 
                imageView.setLayoutParams(new GridView.LayoutParams(320,320));//����ImageView���󲼾� 
                imageView.setAdjustViewBounds(false);//���ñ߽���� 
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//���ÿ̶ȵ����� 
                imageView.setPadding(8, 8, 8, 8);//���ü�� 
            }  
            else { 
                imageView = (ImageView) convertView; 
            } 
            imageView.setImageResource(imgs[position]);//ΪImageView����ͼƬ��Դ 
            return imageView; 
    } 
} 