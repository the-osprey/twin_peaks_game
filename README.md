# twin_peaks_game
This is a pseudo-randomly generated interactive 2D dungeon-exploring game. It started as a project for one of my courses, but evolved beyond that as I decided to take it further! 

![test](twin_peaks_game/media/game_ex.xcf")

There are several features under the hood that I'll draw your attention to. Most of the code is in the "byog" folder, so go there! Since the map is pseudorandomly generated, I created a novel room-building algorithm that guarantees a moveable, completely connected map in which the player can move around. There are 2^31 potential maps that can be generated from a user-inputed seed. Thus, you can reproduce the same map by just inputing the same seed again. The game itself does not require any under-the-hood interaction on the user's part. The game launches to a main menu which the user can click through or use the keyboard to navigate. During the game, the mouse can be used to inspect tiles or move the player. Another feature is saving and loading. Specifically, a user's progress, location and state can be saved by pressing Q. Then, on reload, just select the load option to replay your game! Lastly, since testing an open ended game is not so easy, we have a method that allows the input of keyboard movements. This output can then be tested with respect to the game files.

##### Instructions to run:
All you need to do is download the "tpeaks_game.jar" file to your desired location. Then, open your terminal and run ```$ java -jar tpeaks_game.jar```in the directory. 

Unfortunately, Jar files are not great with audio. Update incoming for that. 

Outside media credits:
Bruno Morales for graphics on main menu
Angelo Badalamenti for the audio files
