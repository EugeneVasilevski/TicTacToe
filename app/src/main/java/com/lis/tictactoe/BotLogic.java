package com.lis.tictactoe;

import java.util.ArrayList;

public class BotLogic extends GameLogic {
    private ArrayList<Integer> listLines;
    private int permissibleCellValue = 1;

    public BotLogic() {
        initializationListLines();
    }

    public boolean activeMove() {
        for (int i = 0; i < field_size; i++) {
            for (int j = 0; j < field_size; j++) {
                if (auxiliaryGameArray[i][j] == 3) {
                    x = i;
                    y = j;
                    for (int k = 0; k < 4; k++) {
                        if (!gameArray[i][j][k]) {
                            z = k;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void randomMove() {
        while (true) {
            int f = (int) (Math.random() * listLines.size());
            int a = listLines.get(f);
            int[] arr2 = new int[field_size];
            int length = 0;
            for (int j = 0; j < field_size; j++) {
                if (auxiliaryGameArray[a][j] != -1 && auxiliaryGameArray[a][j] <= permissibleCellValue) {
                    for (int k = 0; k < 4; k++) {
                        if (!gameArray[a][j][k]) {
                            x = a; y = j; z = k;
                            setCoordAdjacentCell();
                            if (auxiliaryGameArray[x][y] <= permissibleCellValue) {
                                arr2[length] = j;
                                length++;
                                break;
                            }
                        }
                    }
                }
            }
            if (length != 0) {
                int b = (int) (Math.random() * length);
                b = arr2[b];
                int[] arr3 = new int[4];
                length = 0;
                for (int k = 0; k < 4; k++) {
                    if (!gameArray[a][b][k]) {
                        x = a; y = b; z = k;
                        setCoordAdjacentCell();
                        if (auxiliaryGameArray[x][y] <= permissibleCellValue) {
                            arr3[length] = k;
                            length++;
                        }
                    }
                }
                int c = (int) (Math.random() * length);
                c = arr3[c];
                x = a; y = b; z = c;
                break;
            } else {
                listLines.remove(f);
                if (listLines.isEmpty()) {
                    initializationListLines();
                    permissibleCellValue++;
                }
            }
        }
    }

    private void initializationListLines() {
        listLines = new ArrayList(field_size);
        for (int i = 0; i < field_size; i++) {
            listLines.add(i);
        }
    }

}
