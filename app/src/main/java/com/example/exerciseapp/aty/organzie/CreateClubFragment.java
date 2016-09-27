package com.example.exerciseapp.aty.organzie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.fragment.BaseFragment;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2016/8/31.
 */
public class CreateClubFragment extends BaseFragment {
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.owner)
    TextView owner;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.qq)
    TextView qq;
    @Bind(R.id.subject)
    TextView subject;
    @Bind(R.id.submit)
    TextView submit;

    static  Model model = new Model();

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_create_club;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (model != null) {
            name.setText(model.name);
            address.setText(model.addr);
            owner.setText(model.owner);
            phone.setText(model.phone);
            qq.setText(model.qq);
            subject.setText(model.subject);
        }
    }

    @OnClick({R.id.pname,R.id.paddr,R.id.powner,R.id.pphone,R.id.pqq,R.id.psubject})
    public void goEdit(View view){
        if(view.getId() == R.id.psubject){
            Intent intent = new Intent();
            intent.setClass(getActivity(), SelectSubjectActivity.class);
            intent.putExtra("name","1");
            startActivity(intent);
        }
        else{
            Intent intent = new Intent();
            intent.setClass(getActivity(), AlterEditActivity1.class);
            intent.putExtra("id", view.getId());
            startActivity(intent);
        }
    }

    @OnClick(R.id.submit)
    public void submit(){

        if(model.owner == null || model.owner.equals("")){
            Toast.makeText(getActivity(), "负责人不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(model.phone == null || model.phone.equals("")){
            Toast.makeText(getActivity(), "联系电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(model.addr == null || model.addr.equals("")){
            Toast.makeText(getActivity(), "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(model.name == null || model.name.equals("")){
            Toast.makeText(getActivity(), "机构名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(model.subject == null || model.subject.equals("")){
            Toast.makeText(getActivity(), "请选择项目", Toast.LENGTH_SHORT).show();
            return;
        }

        RestAdapterUtils.getZhuAPI().apply1(MyApplication.uid, "apply_club_org", "3.3.0", model.owner, model.phone, model.addr, model.name, 1, MyApplication.token,
                model.qq, "", model.subject, new Callback<ErrorMsg>() {
                    @Override
                    public void success(ErrorMsg errorMsg, Response response) {
                        System.out.println("成功");
                        if (errorMsg != null && errorMsg.getResult() == 1) {
                            ScreenUtils.show_msg(getActivity(), errorMsg.getDesc());
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("申请提交，等待审核中...");
                            builder.setCancelable(false);
                            builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            });
                            builder.setTitle("提示");
                            builder.show();
                        } else {
                            ScreenUtils.show_msg(getActivity(), errorMsg.getDesc());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("error");
                    }
                });
    }

    public static class Model{
        public String name;
        public String addr;
        public String owner;
        public String phone;
        public String qq;
        public String subject;
    }
}
