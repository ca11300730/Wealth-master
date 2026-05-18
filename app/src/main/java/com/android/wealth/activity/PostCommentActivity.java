package com.android.wealth.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.wealth.R;


/**
 * 评论框
 */
public class PostCommentActivity extends Activity implements View.OnClickListener {

    public static final int REQUEST_CODE = 0x3f;
    public static final String DATA_KEY = "key";

    public static final String CONTENT_LENGTH = "content_length";
    public static final String SUPPORT_EMOJI = "support_emoji";


    private EditText mEditTextView;

    private int mLimitLength;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.post_comment);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        TextView cancelView =  findViewById(R.id.cancel);
        mEditTextView = findViewById(R.id.content);
        mLimitLength = getIntent().getIntExtra(CONTENT_LENGTH, 200);
        if (!getIntent().getBooleanExtra(SUPPORT_EMOJI, false)) {
            mEditTextView.setFilters(new InputFilter[]{new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    for (int index = start; index < end; index++) {
                        int type = Character.getType(source.charAt(index));
                        if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
//                        ToastUtil.show("不支持表情符号");
                            return "";
                        }
                    }
                    return null;
                }
            }, new InputFilter.LengthFilter(mLimitLength)});
        }
        TextView postView = (TextView) findViewById(R.id.post);
        TextView title = (TextView) findViewById(R.id.title);
        final View panel = findViewById(R.id.panel);

        cancelView.setOnClickListener(this);
        postView.setOnClickListener(this);
        panel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mEditTextView.requestFocus();
        mEditTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEditTextView, 0);
            }
        },300);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Window window = getWindow();
//        window.getDecorView().requestLayout();
    }

    @Override
    public void finish() {
        hintKeyborad();
        super.finish();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.cancel) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.post) {
            String content = mEditTextView.getText().toString().trim();
            if (content.isEmpty() || "".equals(content.trim())) {
                Toast.makeText(getApplicationContext(), "请填写内容", Toast.LENGTH_SHORT).show();
                return;
            }
            if (content.length() > mLimitLength) {
                Toast.makeText(getApplicationContext(), String.format("最多只能输入%s个字符", mLimitLength), Toast.LENGTH_SHORT).show();
                return;
            }
            mEditTextView.setText("");
            Intent intent = getIntent();
            if (intent == null) {
                intent = new Intent();
            }
            intent.putExtra(DATA_KEY, content);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    /**
     * 隐藏键盘
     */
    public void hintKeyborad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
