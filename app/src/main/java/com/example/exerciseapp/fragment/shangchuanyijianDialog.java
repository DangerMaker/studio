package com.example.exerciseapp.fragment;

import com.example.exerciseapp.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class shangchuanyijianDialog extends Dialog{
	//定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
            public void back(String name);
    }
    
    private String name;
    private OnCustomDialogListener customDialogListener;
//    EditText etName;
    private TextView texthcqlwb;

    public shangchuanyijianDialog(Context context,String name,OnCustomDialogListener customDialogListener) {
            super(context);
            this.name = name;
            this.customDialogListener = customDialogListener;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_qinglihuancun);
            //设置标题
            setTitle(name); 
//            etName = (EditText)findViewById(R.id.edit);
            texthcqlwb=(TextView) findViewById(R.id.texthcqlwb);
            texthcqlwb.setText("上传完成");
            Button clickBtn = (Button) findViewById(R.id.btntijiao);
            clickBtn.setOnClickListener(clickListener);
    }
    
    private View.OnClickListener clickListener = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                shangchuanyijianDialog.this.dismiss();
            }
    };

}