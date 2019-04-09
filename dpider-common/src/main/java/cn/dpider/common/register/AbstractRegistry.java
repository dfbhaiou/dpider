package cn.dpider.common.register;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
public abstract class AbstractRegistry implements Register {

    @Override
    public String register(RegisterRequest request) {
        if (request == null) {
            return null;
        }
        return doRegister(request);
    }

    protected abstract String doRegister(RegisterRequest request);

    @Override
    public void unRegister(RegisterRequest request) {
        if (request == null) {
            return;
        }
        doUnRegister(request);
    }

    protected abstract void doUnRegister(RegisterRequest request);
}
