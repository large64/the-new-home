package game.logic.toolbox;

import game.graphics.entities.Entity;
import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.InfoProvider;
import game.graphics.windowparts.Scene;
import game.logic.entities.RawEntity;
import game.logic.entities.buildings.RawBarrack;
import game.logic.entities.buildings.RawBuilding;
import game.logic.entities.buildings.RawHome;
import game.logic.entities.buildings.RawHospital;
import game.logic.entities.units.RawHealer;
import game.logic.entities.units.RawScientist;
import game.logic.entities.units.RawSoldier;
import game.logic.entities.units.RawUnit;

import java.util.List;

public class GameObserver {
    private static int numberOfEntities;

    private static int numberOfFriendlyEntities;
    private static int numberOfEnemyEntities;

    private static int numberOfBuildings;
    private static int numberOfUnits;

    private static int numberOfHomes;
    private static int numberOfHospitals;
    private static int numberOfBarracks;

    private static int numberOfSoldiers;
    private static int numberOfHealers;
    private static int numberOfScientists;

    public GameObserver() {
        lookForChanges();
    }

    public static void lookForChanges() {
        List<Entity> entities = Scene.getEntities();
        resetVariables();

        for (Entity entity : entities) {
            RawEntity rawEntity = entity.getRawEntity();
            if (!rawEntity.getSide().equals(Side.NEUTRAL)) {
                numberOfEntities++;

                if (rawEntity instanceof RawBuilding) {
                    numberOfBuildings++;
                    if (rawEntity instanceof RawHome) {
                        numberOfHomes++;
                    } else if (rawEntity instanceof RawHospital) {
                        numberOfHospitals++;
                    } else if (rawEntity instanceof RawBarrack) {
                        numberOfBarracks++;
                    }
                } else if (rawEntity instanceof RawUnit) {
                    numberOfUnits++;
                    if (rawEntity instanceof RawSoldier) {
                        numberOfSoldiers++;
                    } else if (rawEntity instanceof RawHealer) {
                        numberOfHealers++;
                    } else if (rawEntity instanceof RawScientist) {
                        numberOfScientists++;
                    }
                }
                if (rawEntity.getSide().equals(Side.FRIEND)) {
                    numberOfFriendlyEntities++;
                } else if (rawEntity.getSide().equals(Side.ENEMY)) {
                    numberOfEnemyEntities++;
                }
            }
        }
    }

    private static void resetVariables() {
        numberOfEntities = 0;

        numberOfFriendlyEntities = 0;
        numberOfEnemyEntities = 0;

        numberOfBuildings = 0;
        numberOfUnits = 0;

        numberOfHomes = 0;
        numberOfHospitals = 0;
        numberOfBarracks = 0;

        numberOfSoldiers = 0;
        numberOfHealers = 0;
        numberOfScientists = 0;
    }

    public static int getNumberOfEntities() {
        return numberOfEntities;
    }

    private static int getNumberOfFriendlyEntities() {
        return numberOfFriendlyEntities;
    }

    public static int getNumberOfEnemyEntities() {
        return numberOfEnemyEntities;
    }

    public static int getNumberOfBuildings() {
        return numberOfBuildings;
    }

    public static int getNumberOfUnits() {
        return numberOfUnits;
    }

    public static int getNumberOfHomes() {
        return numberOfHomes;
    }

    public static int getNumberOfHospitals() {
        return numberOfHospitals;
    }

    public static int getNumberOfBarracks() {
        return numberOfBarracks;
    }

    public static int getNumberOfSoldiers() {
        return numberOfSoldiers;
    }

    public static int getNumberOfHealers() {
        return numberOfHealers;
    }

    public static int getNumberOfScientists() {
        return numberOfScientists;
    }

    public static void checkGameOver(AttackWave attackWave) {
        if (attackWave.isAllWavesAreGone() && getNumberOfEnemyEntities() == 0) {
            InfoProvider.writeMessage("Congratulations! You are the winner!");
            Scene.setGameMode(GameMode.STOPPED);
        } else if (getNumberOfFriendlyEntities() == 0) {
            InfoProvider.writeMessage("You have lost. Maybe next time!");
            Scene.setGameMode(GameMode.STOPPED);
        }
    }
}
