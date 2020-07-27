package com.jieyi;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.HttpRequestHandler;
import com.jieyi.util.MacUtil;
import org.apache.http.Consts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 函数计算的http请求入口
 */
public class FcHttpRequestEntrance implements HttpRequestHandler, FunctionInitializer {
    @Override
    public void initialize(Context context) {
        FunctionComputeLogger logger = context.getLogger();
        logger.debug(String.format("RequestID is %s %n", context.getRequestId()));
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws IOException, ServletException {
        FunctionComputeLogger logger = context.getLogger();
        String requestPath = (String) request.getAttribute("FC_REQUEST_PATH");
        String requestURI = (String) request.getAttribute("FC_REQUEST_URI");
        String requestClientIP = (String) request.getAttribute("FC_REQUEST_CLIENT_IP");

        String requestStr = readJsonStringFromHttpServletRequest(request);
        logger.debug("requestStr:" + requestStr);

        JSONObject jsonRequest = JSONObject.parseObject(requestStr);
        String txntype = (String) jsonRequest.get("txntype");
        Map mapRet = new HashMap<>();
        mapRet.put("result","0000");
        mapRet.put("resultdesc","成功");
        Map mapRetData = new HashMap();
        if("pboc-3des-mac".equals(txntype)){
            String mac = pboc3desmac(jsonRequest,logger);
            mapRetData.put("mac",mac);
        }
        mapRet.put("data",mapRetData);

        response.setStatus(200);
//        response.setHeader("header1", "value1");
//        response.setHeader("header2", "value2");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");

        //String body = String.format("Path: %s\n Uri: %s\n IP: %s\n", requestPath, requestURI, requestClientIP);
        String body = JSONObject.toJSONString(mapRet);

        OutputStream out = response.getOutputStream();
        out.write((body).getBytes());
        out.flush();
        out.close();
    }

    private String pboc3desmac(JSONObject jsonRequest,FunctionComputeLogger logger) {
        JSONObject dataJson = (JSONObject) jsonRequest.get("data");
        String data = (String) dataJson.get("data");
        String init = (String) dataJson.get("init");
        String key = (String) dataJson.get("key");
        String mac = MacUtil.mac3Des(key, init, data);
        logger.debug("mac:" + mac);
        return mac;
    }

    private String readJsonStringFromHttpServletRequest(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
