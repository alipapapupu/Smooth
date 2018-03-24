package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Game;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		final boolean ACCELEROMETER = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
		config.useAccelerometer = ACCELEROMETER;
		config.useCompass = false;

		Game game=new Game();
		game.use(ACCELEROMETER);

		initialize(new Game(), config);
	}
}
