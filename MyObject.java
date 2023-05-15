package com.Celestia.PayForBlobs;

public class MyObject {

    private String namespace_id;
    private String data;
    private int gas_limit;
    private int fee;

    public MyObject(String namespace_id, String data, int gas_limit, int fee) {
        this.namespace_id = namespace_id;
        this.data = data;
        this.gas_limit = gas_limit;
        this.fee = fee;
    }

    public String getValue1() {
        return namespace_id;
    }

    public String getValue2() {
        return data;
    }

    public Integer getValue3() {
        return gas_limit;
    }

    public Integer getValue4() {
        return fee;
    }
}
