package cn.dpider.common.register;

public interface Register {

    String register(RegisterRequest request);

    void unRegister(RegisterRequest request);
}
