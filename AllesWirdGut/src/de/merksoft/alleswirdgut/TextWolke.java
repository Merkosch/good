package de.merksoft.alleswirdgut;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class TextWolke extends Sprite{

	private PhysicsWorld 	physicsWorld; 
	private FixtureDef   	fixtureDef;
	
	private TextSammlung 	textSammlung;
	private Body    		body;
	private Text    		text;
	private Font			font; 
	
	private Thread	 		update;
	protected boolean 		recycled;
	
	
	public TextWolke(float pX, float pY, TextureRegion pTextureRegion, Font font, PhysicsWorld physicsWorld) {
		super(pX, pY, pTextureRegion);
		this.setScale(0.5f);
		this.font = font; 
		
		this.recycled = false; 
		
		this.textSammlung = TextSammlung.getInstance();
		this.fixtureDef   = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		this.physicsWorld = physicsWorld;
		
		this.text = new Text(128, 128, font, this.textSammlung.getRandomText(TextSammlung.DE));
		this.text.setScale(2.0f);
		this.attachChild(text);
		
		this.body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, fixtureDef);
		this.physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
		
		this.startMoving();
	}
	
	public void setSprache(int sprache){
		this.detachChildren();
		this.text = new Text(128, 128, font, this.textSammlung.getRandomText(sprache));
		this.text.setScale(2.0f);
		this.attachChild(text);
	}
	
	public void setLinearVelocity(float x, float y){
		final Vector2 velocity = Vector2Pool.obtain(x, y);
		this.body.setLinearVelocity(velocity);	    						
		Vector2Pool.recycle(velocity);	
	}
	
	public void setKeepMoving(boolean keep){
//		Log.d("AllesWirdGut", "#########setKeepMoving: "+keep);
		if (!keep)
			this.setLinearVelocity(0.0f, 0.0f);
		else
			this.startMoving();
	}
	
	private void startMoving(){
		double a = Math.random()-0.5;
		double b = Math.random()-0.5;
		this.body.setLinearVelocity(new Vector2((float)a, (float)b));
//		this.body.setLinearVelocity(new Vector2(0.1f, 0.1f));
		
		this.update = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(;;){
					Log.d("AllesWirdGut", "*********RUN********");
                    	if (isOutOfRange() && !recycled)
                    		recycle();
                    	else
                    		recycled = false; 
                    try { Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		this.update.start();
	}

	public boolean isOutOfRange() {
		if (this.getX() <= -128.0f || this.getX() >= 352.0f)
			return true;
		if (this.getY() <= -128.0f || this.getY() >= 672.0f)
			return true;
//		if (this.getX() <= -256.0f || this.getX() >= 480.0f)
//			return true;
//		if (this.getY() <= -256.0f || this.getY() >= 800.0f)
//			return true;
		return false;
	}

	public void recycle() {
		Log.d("AllesWirdGut", "#########recycle: ");		
		float x = -this.body.getLinearVelocity().x;
		float y = -this.body.getLinearVelocity().y;
		this.setLinearVelocity(x, y);
		recycled = true;
	}
}
