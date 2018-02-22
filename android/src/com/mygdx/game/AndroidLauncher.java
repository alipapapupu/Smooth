package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Game;

public class AndroidLauncher extends AndroidApplication {

	final boolean GYROSCOPE=false;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useGyroscope = GYROSCOPE;
		config.useAccelerometer = !GYROSCOPE;
		config.useCompass = false;

		Game game=new Game();
		game.use(GYROSCOPE);

		initialize(new Game(), config);
	}
}
