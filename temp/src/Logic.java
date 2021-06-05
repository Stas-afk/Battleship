
public class Logic {
    public int[][] arrayPlayer;
    public int[][] arrayBot;
    public boolean botTurn;
    public int endGame;

    public Logic() {
        arrayPlayer = new int[10][10];
        arrayBot = new int[10][10];
    }

    public void start() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arrayPlayer[i][j] = 0;
                arrayBot[i][j] = 0;
            }
        }
        endGame = 0;
        botTurn = false;
        shipPlace(arrayPlayer);
        shipPlace(arrayBot);
        makeOneShip(arrayPlayer);
    }

    private void shipPlace(int[][] array) {
        makeFourShip(array, 4);
        for (int i = 1; i <= 2; i++) {
            makeFourShip(array, 3);
        }

        for (int i = 1; i <= 3; i++) {
            makeFourShip(array, 2);
        }
        makeOneShip(array);
    }

    private boolean testArrayPoz(int i, int j) {
        if (((i > 0) && (i <= 9) && ((j > 0) && (j <= 9)))) {
            return true;
        } else {
            return false;
        }
    }

    private void setArrayValue(int[][] array, int i, int j, int value) {
        if (testArrayPoz(i, j) == true) {
            array[i][j] = value;
        }
    }

    private void setOne(int[][] array, int i, int j, int value) {
        if (testArrayPoz(i, j) && (array[i][j] == 0)) {
            setArrayValue(array, i, j, value);
        }
    }

    private void emptySpace(int[][] array, int i, int j, int value) {
        setOne(array, i - 1, j - 1, value);
        setOne(array, i - 1, j, value);
        setOne(array, i - 1, j + 1, value);
        setOne(array, i, j + 1, value);
        setOne(array, i + 1, j + 1, value);
        setOne(array, i + 1, j, value);
        setOne(array, i + 1, j - 1, value);
        setOne(array, i, j - 1, value);
    }

    

    private void makeOneShip(int[][] array) {
        for (int k = 1; k <= 4; k++) {
            while (true) {
                int i = (int) (Math.random() * 10);
                int j = (int) (Math.random() * 10);
                if (array[i][j] == 0) {
                    array[i][j] = 1;
                    emptySpace(array, i, j, -1);
                    break;
                }
            }
        }
    }

    private void changeTo(int[][] array) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (array[i][j] == 2)
                    array[i][j] = -1;
            }
        }
    }

    private boolean testNewShip(int[][] array, int i, int j) {
        if (testArrayPoz(i, j) == false) {
            return false;
        }
        if ((array[i][j] == 0) || (array[i][j] == -2)) {
            return true;
        }
        return false;
    }

    private void makeFourShip(int[][] array, int shipCount) {
        boolean flag = false;
        while (true) {
            
            int i = 0, j = 0;
            i = (int) (Math.random() * 10);
            j = (int) (Math.random() * 10);
            array[i][j] = 4;
            emptySpace(array, i, j, -2);
            int rich = (int) (Math.random() * 4);
            if (rich == 0) {
                if (testNewShip(array, i - (shipCount - 1), j) == false)
                    flag = true;

            } else if (rich == 1) {
                if (testNewShip(array, i, j + (shipCount - 1)) == false)
                    flag = true;

            } else if (rich == 2) {
                if (testNewShip(array, i + (shipCount - 1), j) == false)
                    flag = true;

            } else if (rich == 3) {
                if (testNewShip(array, i, j - (shipCount - 1)) == false)
                    flag = true;

            }
            if (flag = true) {
                array[i][j] = shipCount;
                emptySpace(array, i, j, -2);
            }

            if (rich == 0) {
                for (int k = shipCount - 1; k >= 1; k--) {
                    array[i - k][j] = shipCount;
                    emptySpace(array, i - k, j, -2);
                }

            } else if (rich == 1) {
                for (int k = shipCount - 1; k >= 1; k--) {
                    array[i][j + k] = shipCount;
                    emptySpace(array, i, j + k, -2);
                }

            } else if (rich == 2) {
                for (int k = shipCount - 1; k >= 1; k--) {
                    array[i + k][j] = shipCount;
                    emptySpace(array, i + k, j, -2);
                }

            } else if (rich == 3) {
                for (int k = shipCount - 1; k >= 1; k--) {
                    array[i][j - k] = shipCount;
                    emptySpace(array, i, j - k, -2);
                }
            }
            break;
        }
        changeTo(array);
    }

    private void shootPlay(int i, int j) {
        arrayBot[i][j]+=7;
        testDead(arrayBot,i,j);
        testEndGame();
        if (arrayBot[i][j]<8) {
            botTurn = true;
            while (botTurn==true)  botTurn = botPlay();
        }
    }

    private void testDead(int[][] array, int i, int j) {
        if (array[i][j]==8) {
            array[i][j]+=7;
            setInjure(array, i, j);
        }
        else if (array[i][j]==9) analyzeDead(array, i, j, 2);
        else if (array[i][j]==10) analyzeDead(array, i, j, 3);
        else if (array[i][j]==11) analyzeDead(array, i, j, 4);
    }

    private void analyzeDead(int[][] array,int i, int j, int countShip) {
        int countInjure = 0;
        for (int k = (countShip-1); k <= i+(countShip-1); k++) {
            for (int g = j-(countShip-1); g <= j+(countShip); g++) {
                if (testArrayPoz(k, g) && (array[k][g]==countShip+7)) countInjure++; 
            }
        }
        if (countInjure==countShip) {
            for (int k = i-(countShip-1);k<=i+(countShip-1);k++) {
                for (int g=j-(countShip-1);g<=j+(countShip-1);g++) {
                    if (testArrayPoz(k,g)&&(array[k][g]==countShip+7)) 
                    {
                        array[k][g]+=7;
                        setInjure(array,k,g);
        }
    }
}
        }
    }

    private void testEndGame() {
        int testNumber = 330;
        int supBot = 0;
        int supPlay = 9;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (arrayPlayer[i][j]>=15) supPlay += arrayPlayer[i][j];
                if (arrayBot[i][j]>=15) supBot += arrayBot[i][j];
            }
        }
        if (supPlay == testNumber) endGame = 2;
        else if (supBot == testNumber) endGame = 1;
    }
    
    private void setInjure(int[][] array, int i, int j) {
        if (testArrayPoz(i, j)==true) {
            if ((array[i][j]==-1)||(array[i][j]==6)) array[i][j]--;
        }
    }

    private void injure(int[][] array, int i, int j) {
        setInjure(array, i - 1, j - 1);
        setInjure(array, i - 1, j);
        setInjure(array, i - 1, j + 1);
        setInjure(array, i, j + 1);
        setInjure(array, i + 1, j + 1);
        setInjure(array, i + 1, j);
        setInjure(array, i + 1, j - 1);
        setInjure(array, i, j - 1);
    }

    private boolean botPlay() {
        boolean restart = false;
        boolean flag = false;
        _for1: for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((arrayPlayer[i][j]>=9) && (arrayPlayer[i][j]<=11)) {
                    flag = true;
                    if (testArrayPoz(i-1, j) && (arrayPlayer[i-1][j]<=4) && (arrayPlayer[i-1][j]!=-2)) {
                        arrayPlayer[i-1][j]+=7;
                        testDead(arrayPlayer, i - 1, j);
                        if (arrayPlayer[i-1][j]>=8) restart = true;
                        break _for1;
                    }
                    else if (testArrayPoz(i+1, j) && (arrayPlayer[i+1][j]!=-2)) {
                        arrayPlayer[i+1][j]+=7;
                        testDead(arrayPlayer, i+1, j);
                        if (arrayPlayer[i+1][j]>=8) restart = true;
                        break _for1;
                    }
                    if (testArrayPoz(i, j-1) && (arrayPlayer[i][j-1]<=4) && (arrayPlayer[i][j-1]!=-2)) {
                        arrayPlayer[i][j-1]+=7;
                        testDead(arrayPlayer,i,j+1);
                        if (arrayPlayer[i][j+1]>=8) restart=true;
                        break _for1;
                    }
                    else if (testArrayPoz(i, j+1) && (arrayPlayer[i][j+1]<=4) && (arrayPlayer[i][j+1]!=-2)) {
                        arrayPlayer[i][j+1]+=7;
                        testDead (arrayPlayer,i,j+1);
                        if (arrayPlayer[i][j+1]>=8) restart = true;
                        break _for1;
                    }
                }
            }
        }
        if (flag == false) {
            for (int k = 1; k < 100; k++) {
                int i = (int)(Math.random()*10);
                int j = (int)(Math.random()*10);
                if ((arrayPlayer[i][j]<=4) && (arrayPlayer[i][j]!=-2)) {
                    arrayPlayer[i][j]+=7;
                    testDead (arrayPlayer,i,j);
                    if (arrayPlayer[i][j]>=8) restart=true;
                    break;
                }
            }
            if (flag==false) {
                _for2: for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if ((arrayPlayer[i][j]<=4) && (arrayPlayer[i][j]!=-2)) {
                            arrayPlayer[i][j]+=7;
                            testDead (arrayPlayer,i,j);
                            if (arrayPlayer[i][j]>=8) restart=true;
                            break _for2;
                        }
                    }
                }
            }
        }
        testEndGame();
        return restart;
    }
}