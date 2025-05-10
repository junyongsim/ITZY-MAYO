package com.syu.itzy_mayo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.GridLayout;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import java.util.Random;

public class Game2048Activity extends AppCompatActivity {

    private GridLayout gridLayout;
    private final int GRID_SIZE = 4;
    private final TextView[][] cells = new TextView[GRID_SIZE][GRID_SIZE];
    private final Random random = new Random();
    private float startX, startY, endX, endY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(GRID_SIZE);
        gridLayout.setRowCount(GRID_SIZE);

        int cellSize = getResources().getDisplayMetrics().widthPixels / GRID_SIZE;

        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                cells[x][y] = createCell(cellSize);
                gridLayout.addView(cells[x][y]);
            }
        }

        setContentView(gridLayout);

        addRandomTile();
        addRandomTile();
    }

    private TextView createCell(int size) {
        TextView cell = new TextView(this);
        cell.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        cell.setGravity(Gravity.CENTER);
        cell.setTypeface(Typeface.DEFAULT_BOLD);
        cell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        cell.setBackgroundColor(Color.LTGRAY);
        return cell;
    }

    private boolean canMove() {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                String current = cells[x][y].getText().toString();
                if (!current.isEmpty()) {
                    int value = Integer.parseInt(current);

                    // Right Check
                    if (x + 1 < GRID_SIZE && value == getValueAt(x + 1, y)) {
                        return true;
                    }
                    // Down Check
                    if (y + 1 < GRID_SIZE && value == getValueAt(x, y + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int getValueAt(int x, int y) {
        String text = cells[x][y].getText().toString();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }
    private void addRandomTile() {
        boolean isFull = true;

        // 먼저 빈 칸이 있는지 체크
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (cells[x][y].getText().toString().isEmpty()) {
                    isFull = false;
                }
            }
        }

        if (isFull) {
            if (!canMove()) {
                // 만약 이동할 수 없다면, 게임 오버
                // 예를 들어 Dialog를 띄우거나 Toast를 띄워서 알림
                // finish(); // 또는 Activity 종료
                System.out.println("Game Over");
                return;
            } else {
                return; // 이동은 가능하지만 빈 공간이 없으면 추가 안 함
            }
        }

        // 빈 공간이 있는 경우에만 타일 추가
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (!cells[x][y].getText().toString().isEmpty());

        cells[x][y].setText(random.nextInt(10) < 9 ? "2" : "4");
        updateCellColor(cells[x][y]);
    }

    private void updateCellColor(TextView cell) {
        switch (cell.getText().toString()) {
            case "2": cell.setBackgroundColor(Color.parseColor("#EEE4DA")); break;
            case "4": cell.setBackgroundColor(Color.parseColor("#EDE0C8")); break;
            case "8": cell.setBackgroundColor(Color.parseColor("#F2B179")); break;
            case "16": cell.setBackgroundColor(Color.parseColor("#F59563")); break;
            case "32": cell.setBackgroundColor(Color.parseColor("#F67C5F")); break;
            case "64": cell.setBackgroundColor(Color.parseColor("#F65E3B")); break;
            case "128": cell.setBackgroundColor(Color.parseColor("#EDCF72")); break;
            case "256": cell.setBackgroundColor(Color.parseColor("#EDCC61")); break;
            case "512": cell.setBackgroundColor(Color.parseColor("#EDC850")); break;
            case "1024": cell.setBackgroundColor(Color.parseColor("#EDC53F")); break;
            case "2048": cell.setBackgroundColor(Color.parseColor("#EDC22E")); break;
            default: cell.setBackgroundColor(Color.LTGRAY); break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false; // 터치 이벤트 막음
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                swipeLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                swipeRight();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                swipeUp();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                swipeDown();
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        addRandomTile();
        return true;
    }


    private void swipeLeft() {
        for (int y = 0; y < GRID_SIZE; y++) {
            int index = 0;
            int[] newRow = new int[GRID_SIZE];

            for (int x = 0; x < GRID_SIZE; x++) {
                if (!cells[x][y].getText().toString().isEmpty()) {
                    int value = Integer.parseInt(cells[x][y].getText().toString());
                    if (index > 0 && newRow[index - 1] == value) {
                        newRow[index - 1] *= 2;
                    } else {
                        newRow[index++] = value;
                    }
                }
            }

            for (int x = 0; x < GRID_SIZE; x++) {
                cells[x][y].setText(newRow[x] == 0 ? "" : String.valueOf(newRow[x]));
                updateCellColor(cells[x][y]);
            }
        }
    }

    private void swipeRight() {
        for (int y = 0; y < GRID_SIZE; y++) {
            int index = GRID_SIZE - 1;
            int[] newRow = new int[GRID_SIZE];

            for (int x = GRID_SIZE - 1; x >= 0; x--) {
                if (!cells[x][y].getText().toString().isEmpty()) {
                    int value = Integer.parseInt(cells[x][y].getText().toString());
                    if (index < GRID_SIZE - 1 && newRow[index + 1] == value) {
                        newRow[index + 1] *= 2;
                    } else {
                        newRow[index--] = value;
                    }
                }
            }

            for (int x = 0; x < GRID_SIZE; x++) {
                cells[x][y].setText(newRow[x] == 0 ? "" : String.valueOf(newRow[x]));
                updateCellColor(cells[x][y]);
            }
        }
    }

    private void swipeUp() {
        for (int x = 0; x < GRID_SIZE; x++) {
            int index = 0;
            int[] newColumn = new int[GRID_SIZE];

            for (int y = 0; y < GRID_SIZE; y++) {
                if (!cells[x][y].getText().toString().isEmpty()) {
                    int value = Integer.parseInt(cells[x][y].getText().toString());
                    if (index > 0 && newColumn[index - 1] == value) {
                        newColumn[index - 1] *= 2;
                    } else {
                        newColumn[index++] = value;
                    }
                }
            }

            for (int y = 0; y < GRID_SIZE; y++) {
                cells[x][y].setText(newColumn[y] == 0 ? "" : String.valueOf(newColumn[y]));
                updateCellColor(cells[x][y]);
            }
        }
    }

    private void swipeDown() {
        for (int x = 0; x < GRID_SIZE; x++) {
            int index = GRID_SIZE - 1;
            int[] newColumn = new int[GRID_SIZE];

            for (int y = GRID_SIZE - 1; y >= 0; y--) {
                if (!cells[x][y].getText().toString().isEmpty()) {
                    int value = Integer.parseInt(cells[x][y].getText().toString());
                    if (index < GRID_SIZE - 1 && newColumn[index + 1] == value) {
                        newColumn[index + 1] *= 2;
                    } else {
                        newColumn[index--] = value;
                    }
                }
            }

            for (int y = 0; y < GRID_SIZE; y++) {
                cells[x][y].setText(newColumn[y] == 0 ? "" : String.valueOf(newColumn[y]));
                updateCellColor(cells[x][y]);
            }
        }
    }
}
