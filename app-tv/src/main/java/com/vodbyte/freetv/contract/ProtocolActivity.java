package com.vodbyte.freetv.contract;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.vodbyte.freetv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProtocolActivity extends AppCompatActivity {

    @BindView(R.id.protocol_title)
    TextView protocol_title;

    @BindView(R.id.protocol_content)
    TextView protocol_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        protocol_title.setText(getString(R.string.app_name) + "软件许可和服务协议");
        protocol_content.setText(getString(R.string.alter_tip, getString(R.string.app_name)));
    }
}