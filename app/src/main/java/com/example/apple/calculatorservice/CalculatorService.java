package com.example.apple.calculatorservice;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

/**
 * Created by leejw on 2017/7/6.
 */
public class CalculatorService extends Service {

    RelativeLayout mFloatLayout;
    LayoutParams wmParams;
    WindowManager mWindowManager;
    int screenWidth;
    int screenHeight;


    private Character character; //运算符
    private Character teamcharacter;
    private float num1 = 0.0f;
    private float num2 = 0.0f;
    private int flag = 0;
    private String numstr = "";
    private String charstr = "";
    private TextView text;
    private TextView process;
    private Button[] buttons = new Button[11];
    private Button[] buttons2 = new Button[7];

    private static final String TAG = "CalculatorService";
    private LinearLayout mFloatView;
    private ImageView closeIv;
    private ImageView fullIv;
    private boolean isfull;
    private LinearLayout mTable;
    private RelativeLayout mbtnRl;
    private LinearLayout deletell;
    private int lastX, lastY;
    private Button popupbtn;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(TAG, "oncreat");
        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void createFloatView() {
        wmParams = new LayoutParams();
        //WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //window type
        wmParams.type = LayoutParams.TYPE_PHONE;

        wmParams.format = PixelFormat.RGBA_8888;

        wmParams.flags =
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
        ;

        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.calculator_layout, null);
        //mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

        screenWidth = SystemUtil.getScreenWidth();
        screenHeight = SystemUtil.getScreenHeight();
        mFloatView = (LinearLayout) mFloatLayout.findViewById(R.id.calculator_ll);
        mbtnRl = (RelativeLayout) mFloatLayout.findViewById(R.id.btn_rl);
        mTable = (LinearLayout) mFloatLayout.findViewById(R.id.table);
        buttons[0] = (Button) mFloatLayout.findViewById(R.id.btn_0);
        buttons[1] = (Button) mFloatLayout.findViewById(R.id.btn_1);
        buttons[2] = (Button) mFloatLayout.findViewById(R.id.btn_2);
        buttons[3] = (Button) mFloatLayout.findViewById(R.id.btn_3);
        buttons[4] = (Button) mFloatLayout.findViewById(R.id.btn_4);
        buttons[5] = (Button) mFloatLayout.findViewById(R.id.btn_5);
        buttons[6] = (Button) mFloatLayout.findViewById(R.id.btn_6);
        buttons[7] = (Button) mFloatLayout.findViewById(R.id.btn_7);
        buttons[8] = (Button) mFloatLayout.findViewById(R.id.btn_8);
        buttons[9] = (Button) mFloatLayout.findViewById(R.id.btn_9);
        buttons[10] = (Button) mFloatLayout.findViewById(R.id.btn_poin);

        buttons2[0] = (Button) mFloatLayout.findViewById(R.id.btn_divi);
        buttons2[1] = (Button) mFloatLayout.findViewById(R.id.btn_mult);
        buttons2[2] = (Button) mFloatLayout.findViewById(R.id.btn_supt);
        buttons2[3] = (Button) mFloatLayout.findViewById(R.id.btn_add);
        buttons2[4] = (Button) mFloatLayout.findViewById(R.id.btn_equa);
        buttons2[5] = (Button) mFloatLayout.findViewById(R.id.btn_per);
        deletell = (LinearLayout) mFloatLayout.findViewById(R.id.btn_posi);
        buttons2[6] = (Button) mFloatLayout.findViewById(R.id.btn_c);

        text = (TextView) mFloatLayout.findViewById(R.id.print);
        process = (TextView) mFloatLayout.findViewById(R.id.process);
        closeIv = (ImageView) mFloatLayout.findViewById(R.id.close_iv);
        fullIv = (ImageView) mFloatLayout.findViewById(R.id.full_iv);
        closeIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        fullIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isfull) {
                    SystemUtil.setViewWidth(mbtnRl, (int) SystemUtil.dpToPixel(204, getApplicationContext()));
                    SystemUtil.setViewWidth(mFloatLayout, (int) SystemUtil.dpToPixel(220, getApplicationContext()));
                    SystemUtil.setViewHeight(mFloatLayout, (int) SystemUtil.dpToPixel(380, getApplicationContext()));
                    SystemUtil.setViewWidth(mFloatView, (int) SystemUtil.dpToPixel(220, getApplicationContext()));
                    SystemUtil.setViewHeight(mFloatView, (int) SystemUtil.dpToPixel(380, getApplicationContext()));
                    SystemUtil.setViewWidth(mTable, (int) SystemUtil.dpToPixel(204, getApplicationContext()));
                    fullIv.setImageResource(R.mipmap.full_ic);
                    for (Button btn :buttons){
                        btn.setTextSize(20);
                    }
                    for (Button btn :buttons2){
                        btn.setTextSize(20);
                    }
                    isfull = false;
                } else {
                    SystemUtil.setViewWidth(mFloatLayout, screenWidth);
                    SystemUtil.setViewHeight(mFloatLayout, screenHeight-(int) SystemUtil.dpToPixel(20, getApplicationContext()));
                    SystemUtil.setViewWidth(mFloatView, screenWidth);
                    SystemUtil.setViewHeight(mFloatView, screenHeight-(int) SystemUtil.dpToPixel(20, getApplicationContext()));
                    SystemUtil.setViewWidth(mbtnRl, screenWidth-(int) SystemUtil.dpToPixel(16, getApplicationContext()));
                    SystemUtil.setViewWidth(mTable, screenWidth-(int) SystemUtil.dpToPixel(16, getApplicationContext()));
                    fullIv.setImageResource(R.mipmap.no_full_ic);
                    for (Button btn :buttons){
                        btn.setTextSize(36);
                    }
                    for (Button btn :buttons2){
                        btn.setTextSize(36);
                    }
                    isfull = true;
                }
                wmParams.x = 0;

                wmParams.y = 0;

                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
            }
        });
        deletell.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String processStr = process.getText().toString();
                String textStr = text.getText().toString();
                if (processStr.length() > 0) {
                    process.setText(processStr.subSequence(0, processStr.length() - 1));
                }
                if (textStr.length() > 0) {
                    text.setText(textStr.subSequence(0, textStr.length() - 1));
                }
                if (character == null) {
                    if (text.getVisibility() == View.VISIBLE) {
                        process.setText("");
                    }
                    if (teamcharacter != null) {
                        teamcharacter = null;
                    } else {
                        text.setVisibility(View.GONE);
                        text.setText("0");
                        buttons2[6].setText("AC");
                    }
                    if (numstr.length() > 0) {
                        numstr = numstr.substring(0, numstr.length() - 1);
                        if (TextUtils.isEmpty(numstr)) {
                            num1 = 0f;
                            text.setText("");
                        } else {
                            num1 = Float.valueOf(numstr);
                            text.setText(round(numstr));
                        }
                    }
                } else {
                    if (numstr.length() > 0) {
                        numstr = numstr.substring(0, numstr.length() - 1);
                        if (TextUtils.isEmpty(numstr)) {
                            num2 = 0f;
                            text.setText("");
                        } else {
                            num2 = Float.valueOf(numstr);
                            text.setText(round(numstr));
                        }
                    }
                }
            }
        });
        NumberAction na = new NumberAction();
        CharAction ca = new CharAction();

        for (Button num : buttons) {
            num.setOnClickListener(na);
        }

        for (Button _char : buttons2) {
            _char.setOnClickListener(ca);
        }

        text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCopyDialog();
                return true;
            }
        });
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        mFloatView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //getRawX???????????????????????getX???????????????

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        if (dx < 100 && dy < 100) {
                            return false;
                        }
                        wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
                        //Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
                        Log.i(TAG, "RawX" + event.getRawX());
                        Log.i(TAG, "X" + event.getX());

                        wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;
                        // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
                        Log.i(TAG, "RawY" + event.getRawY());
                        Log.i(TAG, "Y" + event.getY());

                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });

        mFloatView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(FxService.this, "onClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showCopyDialog() {
        ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        //将文本数据复制到剪贴板
        ClipData clip = ClipData.newPlainText("copy", text.getText().toString());
        cm.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "计算结果已复制成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

    private float str2double(String charstr) {
        try {
            return Float.valueOf(charstr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private class NumberAction implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            String processStr = process.getText().toString();
            Button bt = (Button) arg0;
            if (!".".equals(bt.getText().toString())) {
                text.setVisibility(View.GONE);
            }

            if (character == null) {
                if (numstr.contains(".") && ".".equals(bt.getText().toString())) {
                    return;
                }
                if (flag == 1) {
                    numstr = "";
                    flag = 0;
                    numstr = bt.getText().toString();
                    text.setText(round(numstr));
                    process.setText(round(numstr));
                } else {
                    if (".".equals(bt.getText().toString()) && TextUtils.isEmpty(numstr)) {
                        numstr = "0";
                    }
                    numstr += bt.getText().toString();
                    if (".".equals(bt.getText().toString())) {
                        text.setText(round(numstr) + ".");
                        process.setText(round(numstr) + ".");
                    } else if ("0".equals(bt.getText().toString()) && numstr.contains(".")) {
                        text.setText(round(numstr));
                        process.setText(numstr);
                    } else {
                        text.setText(round(numstr));
                        process.setText(round(numstr));
                    }

                    buttons2[6].setText("C");
                }
            } else {
                if (numstr.contains(".") && ".".equals(bt.getText().toString())) {
                    return;
                } else {
                    if (".".equals(bt.getText().toString()) && TextUtils.isEmpty(numstr)) {
                        numstr = "0";
                    }
                    numstr += bt.getText().toString();

                    if (".".equals(bt.getText().toString())) {
                        text.setText(round(numstr) + ".");
                        process.setText(processStr + ".");
                    } else if ("0".equals(bt.getText().toString()) && numstr.contains(".")) {
                        text.setText(round(numstr));
                        process.setText(processStr + "0");
                    } else {
                        text.setText(round(numstr));
                        process.setText(processStr + bt.getText().toString());
                    }

                    buttons2[6].setText("C");
                }

            }

        }
    }

    private class CharAction implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Button bt = (Button) arg0;
            charstr = bt.getText().toString();
            if (TextUtils.isEmpty(numstr)) {
                if (charstr.equals("C")) {
                    numstr = "";
                    num2 = 0f;
                    text.setVisibility(View.GONE);
                    text.setText("0");
                    process.setText(round(num1 + "") + teamcharacter);
                    buttons2[6].setText("AC");
                } else if (charstr.equals("AC")) {
                    numstr = "";
                    text.setVisibility(View.GONE);
                    text.setText("0");
                    process.setText("");
                    num1 = 0.0f;
                    num2 = 0.0f;
                    character = null;
                    teamcharacter = null;
                } else if (charstr.equals("÷")) {
                    teamcharacter = '÷';
                    numstr = process.getText().toString().replace("÷", "").replace("×", "").replace("+", "").replace("-", "");
                    if (!TextUtils.isEmpty(numstr)) {
                        num1 = str2double(numstr);
                        character = '÷';
                        process.setText(round(numstr) + charstr);
                        numstr = "";
                    }
                } else if (charstr.equals("×")) {
                    teamcharacter = '×';
                    numstr = process.getText().toString().replace("÷", "").replace("×", "").replace("+", "").replace("-", "");
                    if (!TextUtils.isEmpty(numstr)) {
                        num1 = str2double(numstr);
                        character = '×';
                        process.setText(round(numstr) + charstr);
                        numstr = "";

                    }
                } else if (charstr.equals("+")) {
                    teamcharacter = '+';
                    numstr = process.getText().toString().replace("÷", "").replace("×", "").replace("+", "").replace("-", "");
                    if (!TextUtils.isEmpty(numstr)) {
                        num1 = str2double(numstr);
                        character = '+';
                        process.setText(round(numstr) + charstr);
                        numstr = "";

                    }
                } else if (charstr.equals("-")) {
                    teamcharacter = '-';
                    numstr = process.getText().toString().replace("÷", "").replace("×", "").replace("+", "").replace("-", "");
                    if (!TextUtils.isEmpty(numstr)) {
                        num1 = str2double(numstr);
                        character = '-';
                        process.setText(round(numstr) + charstr);
                        numstr = "";
                    }
                }
                return;
            }

            if (teamcharacter == null) {
                if (character != null) {
                    teamcharacter = character;
                }
            } else if (charstr.equals("÷")
                    || charstr.equals("×")
                    || charstr.equals("+")
                    || charstr.equals("-")) {
                num2 = str2double(numstr);
                numstr = caculate(teamcharacter, num1, num2) + "";
                text.setVisibility(View.VISIBLE);
                text.setText(round(numstr));
                process.setText(round(numstr) + charstr);
            }

            if (charstr.equals("C")) {
                numstr = "";
                text.setVisibility(View.GONE);
                text.setText("0");
                if (teamcharacter != null) {
                    process.setText(round(num1 + "") + teamcharacter);
                } else {
                    process.setText("");
                }
                if (character == null) {
                    process.setText("");
                }
                num2 = 0f;
                buttons2[6].setText("AC");
            } else if (charstr.equals("AC")) {
                numstr = "";
                text.setVisibility(View.GONE);
                text.setText("0");
                process.setText("");
                num1 = 0.0f;
                num2 = 0.0f;
                character = null;
                teamcharacter = null;

            } else if (charstr.equals("+/-")) {
                if (character == null) {
                    if (numstr != "") {
                        num1 = str2double(numstr);
                        num1 = -num1;
                        numstr = num1 + "";
                        text.setText(round(numstr));
                    }
                } else {
                    if (numstr != "") {
                        num2 = str2double(numstr);
                        num2 = -num2;
                        numstr = num2 + "";
                        text.setText(round(numstr));
                    }
                }

            } else if (charstr.equals("%")) {
                if (character == null) {
                    if (numstr != "") {
                        num1 = str2double(numstr);
                        num1 = num1 / 100;
                        numstr = num1 + "";
                        text.setText(round(numstr));
                    }
                } else {
                    if (numstr != "") {
                        num2 = str2double(numstr);
                        num2 = num2 / 100;
                        numstr = num2 + "";
                        text.setText(round(numstr));
                    }
                }

            } else if (charstr.equals("÷")) {
                teamcharacter = '÷';
                if (!TextUtils.isEmpty(numstr)) {
                    num1 = str2double(numstr);
                    character = '÷';
                    process.setText(round(numstr) + charstr);
                    numstr = "";
                }
            } else if (charstr.equals("×")) {
                teamcharacter = '×';
                if (!TextUtils.isEmpty(numstr)) {
                    num1 = str2double(numstr);
                    character = '×';
                    process.setText(round(numstr) + charstr);
                    numstr = "";

                }
            } else if (charstr.equals("+")) {
                teamcharacter = '+';
                if (!TextUtils.isEmpty(numstr)) {
                    num1 = str2double(numstr);
                    character = '+';
                    process.setText(round(numstr) + charstr);
                    numstr = "";

                }
            } else if (charstr.equals("-")) {
                teamcharacter = '-';
                if (!TextUtils.isEmpty(numstr)) {
                    num1 = str2double(numstr);
                    character = '-';
                    process.setText(round(numstr) + charstr);
                    numstr = "";
                }
            } else if (charstr.equals("=")) {
                if (character == null || TextUtils.isEmpty(numstr)) {
                } else {
                    num2 = str2double(numstr);
                    numstr = caculate(character, num1, num2) + "";
                    text.setVisibility(View.VISIBLE);
                    text.setText(round(numstr));
                    character = null;
                    teamcharacter = null;
                    flag = 1;
                }

            } else {
            }

        }
    }

    private float caculate(Character character, float Float1, float Float2) {
        if (character == '-') {
            return sub(Float1, Float2);
        } else if (character == '+') {
            return add(Float1, Float2);
        } else if (character == '×') {
            return mul(Float1, Float2);
        } else if (character == '÷') {
            try {
                return div(Float1, Float2, 9);
            } catch (IllegalAccessException e) {
                return 0f;
            }
        } else
            return 0f;

    }

    public String round(String numstr) {
        try {
            float f = str2double(numstr);
            int fi = (int) f;
            if (f == fi)
                return fi + "";
            else
                return f + "";
        } catch (Exception e) {
            return "0";
        }
    }


    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public float add(float value1, float value2) {
        try {
            BigDecimal b1 = new BigDecimal(Double.valueOf(value1)).setScale(7, BigDecimal.ROUND_HALF_UP);
            BigDecimal b2 = new BigDecimal(Double.valueOf(value2)).setScale(7, BigDecimal.ROUND_HALF_UP);
            return b1.add(b2).floatValue();
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public float sub(float value1, float value2) {

        try {
            BigDecimal b1 = new BigDecimal(Double.valueOf(value1)).setScale(7, BigDecimal.ROUND_HALF_UP);
            BigDecimal b2 = new BigDecimal(Double.valueOf(value2)).setScale(7, BigDecimal.ROUND_HALF_UP);
            return b1.subtract(b2).floatValue();
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public float mul(float value1, float value2) {
        try {
            BigDecimal b1 = new BigDecimal(Double.valueOf(value1)).setScale(7, BigDecimal.ROUND_HALF_UP);
            BigDecimal b2 = new BigDecimal(Double.valueOf(value2)).setScale(7, BigDecimal.ROUND_HALF_UP);
            return b1.multiply(b2).floatValue();
        } catch (NumberFormatException e) {
            return 0f;
        }

    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public float div(float value1, float value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        try {
            BigDecimal b1 = new BigDecimal(Double.valueOf(value1)).setScale(7, BigDecimal.ROUND_HALF_UP);
            BigDecimal b2 = new BigDecimal(Double.valueOf(value2)).setScale(7, BigDecimal.ROUND_HALF_UP);
            return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
        } catch (NumberFormatException |ArithmeticException e) {
            return 0f;
        }


    }

}
