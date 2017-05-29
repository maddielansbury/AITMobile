package hu.ait.minesweeper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import hu.ait.minesweeper.MainActivity;
import hu.ait.minesweeper.R;
import hu.ait.minesweeper.Section;
import hu.ait.minesweeper.model.MinesweeperModel;

public class MinesweeperView extends View {

    private Paint paintBg;
    private Paint paintGridLine;
    private Paint paintShown;
    private Paint paintText;
    private Bitmap bitmapFlag;
    private Bitmap bitmapMine;

    private int txtSize = 60;

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintBg = new Paint();
        paintBg.setColor(Color.parseColor("#f2f2f2"));
        paintBg.setStyle(Paint.Style.FILL);

        paintGridLine = new Paint();
        paintGridLine.setColor(Color.BLACK);
        paintGridLine.setStyle(Paint.Style.STROKE);
        paintGridLine.setStrokeWidth(5);

        paintShown = new Paint();
        paintShown.setColor(Color.parseColor("#dbdbdb"));
        paintShown.setStyle(Paint.Style.FILL);

        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(txtSize);

        bitmapFlag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        bitmapMine = BitmapFactory.decodeResource(getResources(), R.drawable.mine);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        drawFlag(canvas);
        drawShown(canvas);
        drawMinefield(canvas);
    }

    private void drawMinefield(Canvas canvas) {

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintGridLine);

        canvas.drawLine(0, getHeight() / 9, getWidth(), getHeight() / 9, paintGridLine);
        canvas.drawLine(0, 2 * getHeight() / 9, getWidth(), 2 * getHeight() / 9, paintGridLine);
        canvas.drawLine(0, 3 * getHeight() / 9, getWidth(), 3 * getHeight() / 9, paintGridLine);
        canvas.drawLine(0, 4 * getHeight() / 9, getWidth(), 4 * getHeight() / 9, paintGridLine);
        canvas.drawLine(0, 5 * getHeight() / 9, getWidth(), 5 * getHeight() / 9, paintGridLine);
        canvas.drawLine(0, 6 * getHeight() / 9, getWidth(), 6 * getHeight() / 9, paintGridLine);
        canvas.drawLine(0, 7 * getHeight() / 9, getWidth(), 7 * getHeight() / 9, paintGridLine);
        canvas.drawLine(0, 8 * getHeight() / 9, getWidth(), 8 * getHeight() / 9, paintGridLine);

        canvas.drawLine(getWidth() / 9, 0, getWidth() / 9, getHeight(), paintGridLine);
        canvas.drawLine(2 * getWidth() / 9, 0, 2 * getWidth() / 9, getHeight(), paintGridLine);
        canvas.drawLine(3 * getWidth() / 9, 0, 3 * getWidth() / 9, getHeight(), paintGridLine);
        canvas.drawLine(4 * getWidth() / 9, 0, 4 * getWidth() / 9, getHeight(), paintGridLine);
        canvas.drawLine(5 * getWidth() / 9, 0, 5 * getWidth() / 9, getHeight(), paintGridLine);
        canvas.drawLine(6 * getWidth() / 9, 0, 6 * getWidth() / 9, getHeight(), paintGridLine);
        canvas.drawLine(7 * getWidth() / 9, 0, 7 * getWidth() / 9, getHeight(), paintGridLine);
        canvas.drawLine(8 * getWidth() / 9, 0, 8 * getWidth() / 9, getHeight(), paintGridLine);
    }

    private void drawFlag(Canvas canvas) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Section currentSection = MinesweeperModel.getInstance().getSectionContents(i,j);
                float xCoord = i * getWidth() / 9;
                float yCoord = j * getHeight() / 9;
                if (currentSection.getFlag()) {
                    bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, getWidth() / 9, getHeight() / 9, false);
                    canvas.drawBitmap(bitmapFlag, xCoord, yCoord, null);
                }
            }
        }
    }

    private void drawShown(Canvas canvas) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Section currentSection = MinesweeperModel.getInstance().getSectionContents(i,j);
                float xCoord = i * getWidth() / 9;
                float yCoord = j * getHeight() / 9;
                float xCenter = xCoord + (getWidth() / 18) - txtSize / 2;
                float yCenter = yCoord + (getHeight() / 18) + txtSize / 2;
                if (currentSection.getShown()) {
                    canvas.drawRect(xCoord, yCoord,
                            (i + 1) * getWidth() / 9, (j + 1) * getHeight() / 9, paintShown);
                    if (currentSection.getMine()) {
                        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, getWidth() / 9, getHeight() / 9, false);
                        canvas.drawBitmap(bitmapMine, xCoord, yCoord, null);
                    } else {
                        int count = currentSection.getNearbyBombs();
                        switch (count) {
                            case 0 :
                                canvas.drawText("0", xCenter, yCenter, paintText);
                                break;
                            case 1 :
                                canvas.drawText("1", xCenter, yCenter, paintText);
                                break;
                            case 2 :
                                canvas.drawText("2", xCenter, yCenter, paintText);
                                break;
                            case 3 :
                                canvas.drawText("3", xCenter, yCenter, paintText);
                                break;
                            case 4 :
                                canvas.drawText("4", xCenter, yCenter, paintText);
                                break;
                            case 5 :
                                canvas.drawText("5", xCenter, yCenter, paintText);
                                break;
                            case 6 :
                                canvas.drawText("6", xCenter, yCenter, paintText);
                                break;
                            case 7 :
                                canvas.drawText("7", xCenter, yCenter, paintText);
                                break;
                            case 8 :
                                canvas.drawText("8", xCenter, yCenter, paintText);
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = ((int) event.getX()) / (getWidth() / 9);
            int tY = ((int) event.getY()) / (getHeight() / 9);

            if (tX >= 9 || tY >= 9) {
                return true;
            }
            if (!MinesweeperModel.getInstance().getSectionContents(tX, tY).getShown()) {
                if (MinesweeperModel.getInstance().getMode()) {
                    MinesweeperModel.getInstance().flagSection(tX, tY);
                } else {
                    MinesweeperModel.getInstance().sweepSection(tX, tY);
                }
                if (MinesweeperModel.getInstance().isHitMine()) {
                    loseMine();
                }
                if (MinesweeperModel.getInstance().isWrongFlag()) {
                    loseFlag();
                }
                if (MinesweeperModel.getInstance().isWon()) {
                    win();
                }
                invalidate();
            }
        }
        return true;
    }

    public void toggleMode() {
        MinesweeperModel.getInstance().toggleMode();
        if (MinesweeperModel.getInstance().getMode()) {
            ((MainActivity)getContext()).setModeMsg(getContext().getString(R.string.flag_mode));
        } else {
            ((MainActivity)getContext()).setModeMsg(getContext().getString(R.string.sweep_mode));
        }
    }

    public void newGame() {
        MinesweeperModel.getInstance().initMinefield();
        ((MainActivity)getContext()).setModeMsg(getContext().getString(R.string.sweep_mode));
        invalidate();
    }


    private void loseMine() {
        Snackbar.make(this, R.string.lost_mine,
                Snackbar.LENGTH_LONG).show();
    }
    private void loseFlag() {
        Snackbar.make(this, R.string.lost_flag,
                Snackbar.LENGTH_LONG).show();
    }
    private void win() {
        Snackbar.make(this, R.string.won,
                Snackbar.LENGTH_LONG).show();
    }
}
