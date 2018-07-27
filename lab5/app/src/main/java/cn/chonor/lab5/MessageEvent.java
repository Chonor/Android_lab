package cn.chonor.lab5;

/**
 * Created by Chonor on 2017/10/29.
 */

public  class  MessageEvent {
    private Data data;
    public MessageEvent(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }
}
