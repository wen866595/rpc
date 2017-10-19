package net.coderbee.rpc.core.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.coderbee.rpc.core.Caller;
import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.util.ReflectUtil;

public class Provider <T> implements Caller<T> {
    private T ref;
    private URL serviceUrl;
    private Class<T> serviceInterface;
    private Map<String, Method> methodMap = new HashMap<>();

    public Provider (T ref, URL serviceUrl, Class<T> serviceInterface) {
        this.ref = ref;
        this.serviceInterface = serviceInterface;
        this.serviceUrl = serviceUrl;
        initMethodMap();
    }

    private void initMethodMap() {
        Method[] methods = serviceInterface.getDeclaredMethods();
        for(Method method : methods) {
            String desc = ReflectUtil.getMethodDesc(method);
            methodMap.put(desc, method);
        }
    }

    public Method lookUp(String methodDesc) {
        return methodMap.get(methodDesc);
    }

    public URL getUrl() {
        return serviceUrl;
    }

    @Override
    public Class<T> getInterface() {
        return serviceInterface;
    }

    @Override
    public RpcResponse invoke(RpcRequest request) {
        RpcResponse response = new RpcResponse();

        String desc = request.getMethodName();
        if (request.getParameterTypeDesc() == null) {
            desc = desc + "()";
        } else {
            desc = desc + "(" + request.getParameterTypeDesc() + ")";
        }
        Method method = lookUp(desc);
        try {
            Object val = method.invoke(ref, request.getParameters());
            response.setResult(val);
        } catch (Exception e) {
            if (e.getCause() != null) {
                response.setError(new RpcException("provider call error", e.getCause()));
            } else {
                response.setError(new RpcException("provider call error", e));
            }
        }

        response.setRequestId(request.getRequestId());
        response.setVersion(request.getVersion());
        response.setAttachments(request.getAttachments());

        return response;
    }
}
