package com.lis.tictactoe;

public class GameLogic {
    public static boolean[][][] gameArray;
    public static int[][] auxiliaryGameArray;
    public static int field_size;
    public static int x, y, z;
    public int a = 0;
    public int b = 0;
    private int num_remaining_cells;

    public GameLogic() {}

    public GameLogic(int num_cells, int num_ext_cells) {
        gameArray = new boolean[num_cells][num_cells][4];
        auxiliaryGameArray = new int[num_cells][num_cells];
        field_size = num_cells;
        num_remaining_cells = num_cells * num_cells - (num_cells - num_ext_cells) * ((num_cells - num_ext_cells) / 2 + 1);

        int control_point1 = (num_cells - num_ext_cells) / 2;
        int control_point2 = num_cells - (control_point1 + 1);
        int a = control_point1, b = control_point2;

        for (int i = 0; i <= control_point1; i++) {
            initialization(num_cells, i, a, b);
            gameArray[i][a][0] = true;
            gameArray[i][a][1] = true;
            gameArray[i][b][1] = true;
            gameArray[i][b][2] = true;
            auxiliaryGameArray[i][a] = 2;
            auxiliaryGameArray[i][b] = 2;
            a--;
            b++;
        }
        for (int i = control_point2; i < num_cells; i++) {
            a++;
            b--;
            initialization(num_cells, i, a, b);
            gameArray[i][a][0] = true;
            gameArray[i][a][3] = true;
            gameArray[i][b][2] = true;
            gameArray[i][b][3] = true;
            auxiliaryGameArray[i][a] = 2;
            auxiliaryGameArray[i][b] = 2;
        }
        a = 0;
        b = num_cells - 1;
        for (int i = control_point1 + 1; i < control_point2; i++) {
            initialization(num_cells, i, a, b);
            gameArray[i][0][0] = true;
            gameArray[0][i][1] = true;
            gameArray[i][num_cells - 1][2] = true;
            gameArray[num_cells - 1][i][3] = true;
            auxiliaryGameArray[i][0] = 1;
            auxiliaryGameArray[0][i] = 1;
            auxiliaryGameArray[i][num_cells - 1] = 1;
            auxiliaryGameArray[num_cells - 1][i] = 1;
        }
    }

    private void initialization(int num_cells, int i, int a, int b) {
        for (int j = 0; j < num_cells; j++) {
            if (j < a || j > b) {
                gameArray[i][j] = null;
                auxiliaryGameArray[i][j] = -1;
            } else {
                for (int k = 0; k < 4; k++) {
                    gameArray[i][j][k] = false;
                }
                auxiliaryGameArray[i][j] = 0;
            }
        }
    }

    public boolean isGameOver() {
        if (num_remaining_cells == 0) {
            return true;
        }
        return false;
    }

    public void setCoordAdjacentCell() {
        switch (z) {
            case 0:
                y--; z = 2;
                break;
            case 1:
                x--; z = 3;
                break;
            case 2:
                y++; z = 0;
                break;
            case 3:
                x++; z = 1;
                break;
            default:
                break;
        }
    }

    public boolean checkStrokeWithInitialization() {
        if (gameArray[x][y] != null) {
            if (!gameArray[x][y][z]) {
                gameArray[x][y][z] = true;
                auxiliaryGameArray[x][y]++;
                if (auxiliaryGameArray[x][y] == 4) {
                    num_remaining_cells--;
                }
                return true;
            }
        }
        return false;
    }

}
