package com.huitian.oamanager.ui.user.activity;


import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.ui.user.contract.ModifyPasswordContract;
import com.huitian.oamanager.ui.user.model.ModifyPasswordModel;
import com.huitian.oamanager.ui.user.presenter.ModifyPasswordPresenter;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baseapp.AppManager;

import butterknife.Bind;
import butterknife.OnClick;

public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter, ModifyPasswordModel> implements ModifyPasswordContract.View {
    @Bind(R.id.et_old_pw)
    EditText etOldPw;
    @Bind(R.id.et_new_pw)
    EditText etNewPw;
    @Bind(R.id.et_confirm_pw)
    EditText etConfirmPw;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @Bind(R.id.tool_bar)
    Toolbar toolBar;

    @OnClick(R.id.btn_commit)
    public void changePassword() {
        commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        setSimpleToolbar(toolBar, tvToolbarTitle, "修改密码");
    }

    private void commit() {
        String oldPassword = etOldPw.getText().toString().trim();
        String newPassword = etNewPw.getText().toString().trim();
        String confirmPassword = etConfirmPw.getText().toString().trim();
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showShortToast("密码不能为空");
            return;
        }
        if (oldPassword.equals(newPassword)) {
            showShortToast("旧密码和新密码不能相同");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showShortToast("两次输入的密码不一致");
            return;
        }
        mPresenter.changePassword(oldPassword, newPassword);
    }

    @Override
    public void showLoading(String title) {
        startProgressDialog(title);
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        showShortToast("密码修改失败");
    }

    @Override
    public void changePasswordSuccess(String msg) {
        showShortToast("密码修改成功");
        AppManager.getAppManager().finishAllActivity();
        startActivity(LoginActivity.class);
    }

    @Override
    public void changePasswordFail(String msg) {
        showShortToast(msg);
    }

}
