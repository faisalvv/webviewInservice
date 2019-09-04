package com.faisal.myapplication;


public interface OnManageActionListener {

    void showLoader(boolean isShow, String action);
    void quickBack(String action);
    void onShowRetryView(String action);


}