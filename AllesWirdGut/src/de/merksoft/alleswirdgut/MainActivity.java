package de.merksoft.alleswirdgut;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.math.Vector2;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends BaseGameActivity {

	private static final int 	CAMERA_WIDTH = 480;
	private static final int 	CAMERA_HEIGHT = 800;

	private PhysicsWorld 		physicsWorld; 
	
	private Camera 				mCamera;
	private Texture 			wolkeTexture; 
	private TextureRegion 		wolkeTextureRegion; 
	private Font 				font;
	
	private Texture 			textTexture; 
	private List<TextWolke> 	wolkenSammlung;
	private int 				sprache;
	private boolean				wolkenBewegung;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
	
		this.wolkenSammlung = new ArrayList<TextWolke>();
		this.physicsWorld = new PhysicsWorld(new Vector2(0, 0), false, 8, 1);
		
		this.wolkeTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.wolkeTextureRegion = TextureRegionFactory.createFromAsset(this.wolkeTexture, this, "gfx/wolke.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.wolkeTexture);
		
		this.textTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mEngine.getTextureManager().loadTextures(this.textTexture);
		this.font = new Font(textTexture, Typeface.DEFAULT_BOLD, 24, true, Color.GRAY);
		this.mEngine.getFontManager().loadFont(font);
		
	}

	@Override
	public Scene onLoadScene() {
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.sprache = TextSammlung.DE;
		this.wolkenBewegung = true;
		
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.6f, 0.7f, 1.0f));
		scene.registerUpdateHandler(physicsWorld);
		
		for (int i = 0; i < 4; i++) {
			this.wolkenSammlung.add(new TextWolke(0, 0, this.wolkeTextureRegion, font, this.physicsWorld));
			scene.getLastChild().attachChild(this.wolkenSammlung.get(i));
		}
		
		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			float x = 0.0f; 
			float y = 0.0f;
			
			// Die ersten beiden TouchEvents liefern aus irgend einem 
			// Grund falsche Daten. Daher darf erst das dritte TouchEvent
			// herangezogen werden.
			int counter = 0; 
			
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				
				if (pSceneTouchEvent.isActionUp()){
					x = 0.0f; 
					y = 0.0f;
					counter = 0; 
					if (!wolkenBewegung)
						for (TextWolke wolke : wolkenSammlung) {
							wolke.setLinearVelocity(0.0f, 0.0f);
						}
				}
				
//				Log.d("Alleswirdgut", counter+"####"+pSceneTouchEvent.getX()+"####"+pSceneTouchEvent.getY()+"****"+x+"****"+y);
				
				for (TextWolke wolke : wolkenSammlung) {
					if (wolke.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
						if ( (x!=0.0f && y!=0.0f) && counter > 2 ){
							x = x - pSceneTouchEvent.getX(); 
							y = y - pSceneTouchEvent.getY();
							wolke.setLinearVelocity(-x, -y);	    						
						}
						counter++;
						x = pSceneTouchEvent.getX(); 
						y = pSceneTouchEvent.getY(); 
					}
				}
				
				
				return true;
			}
			
		});
		
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();	
	    inflater.inflate(R.layout.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.english:  Toast.makeText(this, "English", Toast.LENGTH_LONG).show();
	                            this.sprache = TextSammlung.EN;
	                            for (TextWolke wolke : this.wolkenSammlung) {
	                            	wolke.setSprache(this.sprache);
	                            }
	        					break;
	        case R.id.deutsch:  Toast.makeText(this, "Deutsch", Toast.LENGTH_LONG).show();
	                            this.sprache = TextSammlung.DE;
	                            for (TextWolke wolke : this.wolkenSammlung) {
	                            	wolke.setSprache(this.sprache);
	                            }
	        					break;
	        case R.id.moving: 	if (this.sprache == TextSammlung.DE){
	        						if (wolkenBewegung)
	        							Toast.makeText(this, "Bewegung aus!", Toast.LENGTH_LONG).show();
	        						else
	        							Toast.makeText(this, "Bewegung ein!", Toast.LENGTH_LONG).show();
	    						}
	        					if (this.sprache == TextSammlung.EN){
	        						if (wolkenBewegung)
	        							Toast.makeText(this, "Movement off!", Toast.LENGTH_LONG).show();
	        						else
	        							Toast.makeText(this, "Movement on!", Toast.LENGTH_LONG).show();
	        					}
	        					for (TextWolke wolke : this.wolkenSammlung) {
									wolke.setKeepMoving(!this.wolkenBewegung);
								}
	                            this.wolkenBewegung = !this.wolkenBewegung;
	        					break;
	    }
	    return true;
	}
	
	
}