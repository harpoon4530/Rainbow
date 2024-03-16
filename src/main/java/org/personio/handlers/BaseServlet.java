package org.personio.handlers;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BaseServlet extends HttpServlet {

    public static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    protected void writeResponse(HttpServletResponse resp, String contentType, String responseMessage) throws IOException {
        resp.setContentType(contentType + ";charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(responseMessage);
    }

    protected void writeResponseWithContentType(HttpServletResponse resp, String contentType, int responseCode, String responseMessage) throws IOException {
        resp.setContentType(contentType + ";charset=utf-8");
        resp.setStatus(responseCode);
        resp.getWriter().print(responseMessage);
    }

    protected void writeDelimitedResponseWithBytes(HttpServletResponse resp, String contentType, int responseCode, Message message) throws IOException {
        resp.setContentType(contentType);
        resp.setStatus(responseCode);

        // TODO: check to validate this
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        message.writeDelimitedTo(bOutput);


        resp.setContentLength(bOutput.size()); // This is necessary due to http1.1 chunked encoding on certain platforms (tcp nagle)
        message.writeDelimitedTo(resp.getOutputStream());
        resp.getOutputStream().flush();
    }

    protected void writeResponseWithBytes(HttpServletResponse resp, String contentType, int responseCode, byte[] bytes) throws IOException {
        resp.setContentType(contentType);
        resp.setStatus(responseCode);
        resp.setContentLength(bytes.length); // This is necessary due to http1.1 chunked encoding on certain platforms (tcp nagle)
        resp.getOutputStream().write(bytes);
        resp.getOutputStream().flush();
    }

//    @Override
//    public final void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        boolean ok = false;
//        try {
//            doYpOptions(new YpRequest(request), new YpResponse(response));
//            ok = true;
//        } finally {
//            ok = false;
//            //emitCounter("options", ok);
//        }
//    }

//    protected void doYpOptions(YpRequest request, YpResponse response) throws IOException {
//        //throw new MethodNotAllowedException("OPTIONS");
//    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean ok = false;


        int i = 0;

//        long st = System.currentTimeMillis();
//        try {
//            int i = 0;
//            ypReq = new YpRequest(request);
//            ypRes = new YpResponse(response);
//
//            doBeforeYpGet(ypReq, ypRes);
//            doYpGet(ypReq, ypRes);
//            doAfterYpGet(ypReq, ypRes);
//            ok = true;
//        } catch (RedirectException e) {
//            ypRes.sendRedirect(e.getUrl());
//            logger.info("Redirecting [302]: {}", e.getUrl());
//            ok = true;
//        } finally {
//            long et = System.currentTimeMillis();
//            //emitCounter("get", ok, et - st);
//        }
    }

//    protected void doBeforeYpGet(YpRequest request, YpResponse response)
//            throws IOException, RedirectException {
//    }
//
//    protected void doAfterYpGet(YpRequest request, YpResponse response) throws IOException {
//    }
//
//    protected void doYpGet(YpRequest request, YpResponse response) throws IOException {
//        throw new MethodNotAllowedException("GET");
//    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean ok = false;
        try {
            int j = 0;
            //doYpPost(new YpRequest(request), new YpResponse(response));
            ok = true;
        } finally {
            int i = 0;
            //emitCounter("post", ok);
        }
    }


    @Override
    public final void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean ok = false;
        try {
            //doYpPut(new YpRequest(request), new YpResponse(response));
            ok = true;
        } finally {
            //todo - hassaan
            //emitCounter("put", ok);
        }
    }

//    protected void doYpPut(YpRequest request, YpResponse response) throws IOException {
//        throw new MethodNotAllowedException("PUT");
//    }

    @Override
    public final void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        boolean ok = false;
        try {
            //doYpDelete(new YpRequest(req), new YpResponse(resp));
            ok = true;
        } finally {
            //todo - hassaan
            //emitCounter("delete", ok);
        }
    }

//    protected void sendRedirect(YpResponse resp, String url) {
//        try {
//            resp.sendRedirect(url);
//            logger.info("Redirecting [302]: {}", url);
//        } catch (IOException e) {
//            logger.warn("Failed to send redirect: {}", url);
//        }
//    }

    protected void writeResponse(HttpServletResponse resp, int responseCode, String responseMessage)
            throws IOException {
        writeResponseWithContentType(resp, "text/html", responseCode, responseMessage);
    }

//    protected String getBaseUrl(YpRequest req) {
//        return req.getScheme() + "://" + req.getHeader("Host");
//    }

//    protected void check(boolean cond, String msg) {
//        if (!cond) {
//            throw new BadRequestException(msg);
//        }
//    }

}
