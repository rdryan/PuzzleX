/*
Copyright (C) 2011  Wade Chatam

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

//import net.youmi.android.banner.AdSize;
//import net.youmi.android.banner.AdView;
//import net.youmi.android.spot.SpotManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

/**
 * This is the primary @class Activity for the PhotoGaffe application.  It
 * creates the game board and handles any menu items selected.
 * @author wadechatam
 *
 */
public final class PuzzleActivity extends Activity {

   public static final int IMAGEREQUESTCODE = 8242008;
   public static final int DIALOG_PICASA_ERROR_ID = 0;
   public static final int DIALOG_GRID_SIZE_ID = 1;
   public static final int DIALOG_COMPLETED_ID = 2;
   private static SoundPool snd;
   private static int soundhit;
   private static int sounderror;
   private static int soundfinished;
   private static Boolean isSoundEffectsOn;
   
   //图片数组 
   private Integer[] imgs = { 
           R.drawable.pic0, R.drawable.pic1, R.drawable.pic2, 
           R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,
           R.drawable.pic6, R.drawable.pic7, R.drawable.pic8,
           R.drawable.pic9, R.drawable.pic10,
   }; 
  
   private GameBoard board;
   private Bitmap bitmap; // temporary holder for puzzle picture
   private int bmpWidth;
   private int bmpHeight;
   private int boardWidth;
   private int boardHeight;
   private int boardSZ;
   
   private boolean numbersVisible = false; // Whether a title is displayed that
                                  // shows the correct location of the
                                 // tiles.

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//sets the orientation to portrait
      setContentView(R.layout.board);
      
	  // 实例化广告条
	  //AdView adView = new AdView(this, AdSize.FIT_SCREEN);
	  //RelativeLayout adLayout=(RelativeLayout)findViewById(R.id.AdLayout3);
	  //adLayout.addView(adView);
      
	  // 加载插播资源
	  //SpotManager.getInstance(this).loadSpotAds();
	  // 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
	  //SpotManager.getInstance(this).setSpotTimeout(5000);   // 设置5秒
	  //SpotManager.getInstance(this).setShowInterval(20);    // 设置20秒的显示时间间隔	  
	  // 如需要使用自动关闭插屏功能，请取消注释下面方法
	  //SpotManager.getInstance(this).setAutoCloseSpot(false); // 设置自动关闭插屏开关
	  //SpotManager.getInstance(this).setCloseTime(6000);     // 设置关闭插屏时间
	  
	  //使用SoundPool播放音效
	  snd = new SoundPool(3,AudioManager.STREAM_SYSTEM,5);
	  soundhit = snd.load(this,R.raw.s_hit,0);
	  sounderror = snd.load(this,R.raw.s_error,0);
	  soundfinished = snd.load(this,R.raw.s_finished,0);
	  //isSoundEffectsOn = SettingsActivity.isSoundEffectsOn(this);
      //selectImageFromGallery();
      selectImage();
   }    

   /* (non-Javadoc)
    * Will start an intent for external Gallery app.  
    * Image returned via onActivityResult().
    */
   private void selectImageFromGallery() {
      Intent galleryIntent = new Intent(Intent.ACTION_PICK, 
            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
      startActivityForResult(galleryIntent, IMAGEREQUESTCODE);
   }

   /* (non-Javadoc)
    * Run when Gallery app returns selected image.
    */
   @Override
   protected final void onActivityResult(final int requestCode, 
         final int resultCode, 
         final Intent i) {
      super.onActivityResult(requestCode, resultCode, i);

      if (resultCode == RESULT_OK) {
         switch (requestCode) {
         case IMAGEREQUESTCODE:
            Uri imageUri = i.getData();
            
            try {
               //bitmap = createScaledBitmap(imageUri);
               bitmap = createScaledBitmap(R.drawable.pic0);
            } catch (FileNotFoundException e) {
               // You see, what had happened was...
               // When using the Gallery app for selecting an image, the 
               // Gallery will display the user's on-line Picasa web
               // albums.  If the user attempts to select one of the
               // pictures from their Picasa web albums, Gingerbread and 
               // earlier versions of the OS will throw this exception.
               // Honeycomb and later will automatically download the 
               // picture.  This catch will be called for Gingerbread
               // users and just tell them that we do not support using
               // Picasa web album pictures for their version of Android.
               // They will then be prompted to select another picture from
               // the Gallery.
               showDialog(DIALOG_PICASA_ERROR_ID);
            } catch (IOException e) {
               e.printStackTrace();
               finish();
            } catch (IllegalArgumentException e) {
               showDialog(DIALOG_PICASA_ERROR_ID);
            }
            
            createGameBoard(SettingsActivity.getGridSize(this));
            break;
         } // end switch
      } // end if
   }
   
   private final void selectImage()
   {
	   isSoundEffectsOn = SettingsActivity.isSoundEffectsOn(this);
	   
	   Intent intent = this.getIntent();
	   Bundle bundle = intent.getExtras();
	   int picID = bundle.getInt("picID", 0);
	   
		try {
			//bitmap = createScaledBitmap(R.drawable.pic0);
			bitmap = createScaledBitmap(imgs[picID]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	   createGameBoard(SettingsActivity.getGridSize(this));
   }
   
   /* (non-Javadoc)
    * Returns a scaled image of the bitmap at the given location.  This helps
    * prevent OutOfMemory exceptions when loading large images from the SD
    * card.
    */
//   private Bitmap createScaledBitmap(Uri uri)
   private Bitmap createScaledBitmap(int id) 
         throws FileNotFoundException, IOException, 
               IllegalArgumentException {
      //InputStream is = getContentResolver().openInputStream(uri);
      InputStream is = this.getResources().openRawResource(id);
      
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      
      BitmapFactory.Options boundingBox = new BitmapFactory.Options();
      boundingBox.inJustDecodeBounds = true;
      boundingBox.inDither = true;
      BitmapFactory.decodeStream(is, null, boundingBox);
      is.close();
      
      int screenSize = (int) (metrics.widthPixels * metrics.density *
            metrics.heightPixels * metrics.density);
      int imageSize = boundingBox.outWidth * boundingBox.outHeight;
      
      bmpWidth = boundingBox.outWidth;
      bmpHeight = boundingBox.outHeight;
      
      
      BitmapFactory.Options pictureOptions = new BitmapFactory.Options();

      //TODO improve the mechanism for determining if an image should be
      // sampled based on memory available and size of image.
      if (imageSize > screenSize) {
//         pictureOptions.inSampleSize = 8;
//         Integer.highestOneBit((int) Math.ceil(imageSize / screenSize));
      }
      
      pictureOptions.inDither = true;
      //pictureOptions.inPreferredConfig = Config.RGB_565;
      pictureOptions.inPurgeable = true;
      //pictureOptions.inSampleSize = 2;
      
      //is = getContentResolver().openInputStream(uri);
      is = this.getResources().openRawResource(id);
      Bitmap bitmap = BitmapFactory.decodeStream(is, null, pictureOptions);
      is.close();
      return bitmap;
   }
   

   /* (non-Javadoc)
    * Basic wrapper method for creating the game board and setting the number
    * visibility.
    * @param gridSize row and column count (3 = 3x3; 4 = 4x4; 5 = 5x5; etc.)
    */
   private final void createGameBoard(short gridSize) {
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
      TableLayout tableLayout;
      tableLayout = (TableLayout) findViewById(R.id.parentLayout);    
      tableLayout.removeAllViews();
      
      //boardWidth  = (int) (metrics.widthPixels * metrics.density * 1);
      //boardHeight = (int) (metrics.heightPixels * metrics.density * 1);
      
      boardWidth  = (int) metrics.widthPixels;
      boardHeight = (int) metrics.heightPixels;
      
      if (boardWidth > boardHeight) {
    	  boardSZ = boardHeight;
      } else {
    	  boardSZ = boardWidth;
      }
            
      board = GameBoard.createGameBoard(this, 
    		  bitmap, 
    		  tableLayout,
    		  boardSZ,  //(int) (metrics.widthPixels * metrics.density * 0.9),
    		  boardSZ,  //(int) (metrics.heightPixels * metrics.density * 0.9),
    		  gridSize);
      
      board.setNumbersVisible(numbersVisible);
      bitmap.recycle(); // free memory for this copy of the picture since the
                     // picture is stored by the GameBoard class
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.arranger_menu, menu);
      return true;
   }

   //TODO Replace this with ActionBar support for IceCream Sandwich (4.0)
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      boolean returnVal = true;

      switch (item.getItemId()) {
      case R.id.new_picture :
         //selectImageFromGallery();
         Intent i = new Intent(this, SelectPicActivity.class);         
         startActivity(i);
         break;
      case R.id.reshuffle:
         board.shuffleTiles();
         break;
      //case R.id.settings:
      //   Intent i2 = new Intent(this, SettingsActivity.class);
      //   startActivity(i2);
      //   break;
      default:
         returnVal = super.onOptionsItemSelected(item);
      }

      return returnVal;
   }
   
   @Override
   protected void onResume() {
      super.onResume();
      numbersVisible = SettingsActivity.isNumbersVisible(this);
      
      if (board == null) {
         return;
      }      
      
      board.setNumbersVisible(numbersVisible);      
      // Check if the size of the board has changed, since this puzzle was
      // started.  If so, create a new board.
      if (board.getGridSize() != SettingsActivity.getGridSize(this)) {
         short gridSize = SettingsActivity.getGridSize(this);
         createGameBoard(gridSize);
      }
   }

   @Override
   protected Dialog onCreateDialog(int id) {
      Dialog dialog;
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      switch(id) {
      case DIALOG_PICASA_ERROR_ID:
         builder.setMessage(R.string.picasa_error)
         .setCancelable(false)
         .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               // After message is displayed, have the user pick again.
               selectImageFromGallery();
            }
         });
         dialog = builder.create();
         break;
      case DIALOG_COMPLETED_ID:
         builder.setMessage(createCompletionMessage())
         .setCancelable(false)
         .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               //board.shuffleTiles();
            }
         });
         dialog = builder.create();         
   	     //Show Ads
   	     //SpotManager.getInstance(this).showSpotAds(this);   	     
         break;
      default:
         dialog = null;
      }
      return dialog;
   }
   
   //TODO When updating to ICS-level API, replace this with Fragment
   @Override
   protected void onPrepareDialog(int id, Dialog dialog) {
      switch (id) {
      case DIALOG_COMPLETED_ID:
         ((AlertDialog) dialog).setMessage(createCompletionMessage());
         break;
      }
   }
   
   /* (non-Javadoc)
    * Return a 'congratulatory' message that also contains the number of moves.
    */
   private String createCompletionMessage() {
      String completeMsg = 
            getResources().getString(R.string.congratulations) + " " 
            + String.valueOf(board.getMoveCount());
      //String[] insults = getResources().getStringArray(R.array.insults);
      completeMsg += "\n";
      //int insultIndex = (int) Math.floor(Math.random() * insults.length); 
      //completeMsg += insults[insultIndex];
      
      return completeMsg;
   }
   
   @Override
   protected void onStop() {
      // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
	  //SpotManager.getInstance(this).disMiss(false);
	  super.onStop();
   }

   @Override
   protected void onDestroy() {
	  //SpotManager.getInstance(this).unregisterSceenReceiver();
	  super.onDestroy();
   }
   
   public static void playHitSound() {
	   if (isSoundEffectsOn) {
		   snd.play(soundhit,0.8f,0.8f,0,0,1);
	   }
   }
   
   public static void playErrorSound() {
	   if (isSoundEffectsOn) {
		   snd.play(sounderror,0.8f,0.8f,0,0,1);
	   }
   }
   
   public static void playFinishSound() {
	   if (isSoundEffectsOn) {
		   snd.play(soundfinished,0.8f,0.8f,0,0,1);
	   }
   }
   
} 
