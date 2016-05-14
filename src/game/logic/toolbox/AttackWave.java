package game.logic.toolbox;

import game.graphics.entities.units.Soldier;
import game.graphics.models.TexturedModel;
import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.InfoProvider;
import game.graphics.windowparts.MiniMap;
import game.graphics.windowparts.Scene;
import game.logic.entities.RawMap;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

/**
 * Created by Dénes on 2016. 05. 14..
 */
public class AttackWave implements Runnable {
    private int nrOfEntities;
    private int nrOfWaves;
    private int currentWaveNr;
    private boolean allWavesAreGone;

    public AttackWave(int nrOfEntities, int nrOfWaves) {
        this.nrOfEntities = nrOfEntities;
        this.nrOfWaves = nrOfWaves;
        this.currentWaveNr = 0;
        this.allWavesAreGone = false;
    }

    @Override
    public void run() {
        if (Scene.getGameMode() == GameMode.ONGOING && !allWavesAreGone) {
            InfoProvider.writeMessage("A wave of enemies is coming! (" + (currentWaveNr + 1) + ")");

            TexturedModel soldierModel = Scene.getModelsMap().get("enemyUnit");
            Random random = new Random();

            for (int i = 0; i < nrOfEntities; i++) {
                float xInitial = (float) random.nextInt((199 - 0) + 1) + 0;
                float zInitial = (float) random.nextInt((199 - 0) + 1) + 0;
                new Soldier(soldierModel, new Vector3f(xInitial, Scene.getMainMap().getHeightOfMap(xInitial, zInitial), zInitial), 1, Side.ENEMY);
            }

            MiniMap.setEntities();
            RawMap.setRawEntities();
            RawMap.lookForChanges();
            GameObserver.lookForChanges();

            if ((currentWaveNr + 1) == nrOfWaves) {
                allWavesAreGone = true;
            }

            currentWaveNr++;
        }
    }

    public boolean isAllWavesAreGone() {
        return allWavesAreGone;
    }
}
