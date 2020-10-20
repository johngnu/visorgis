/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.gob.ine.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Castillo
 */
public class ClientInfo {

    private static final Logger logger = LoggerFactory.getLogger(ClientInfo.class);

    public void printClientInfo(HttpServletRequest request) {
        final String referer = getReferer(request);
        final String fullURL = getFullURL(request);
        final String clientIpAddr = getClientIpAddr(request);
        final String clientOS = getClientOS(request);
        final String clientBrowser = getClientBrowser(request);
        final String userAgent = getUserAgent(request);

        logger.info("\n"
                + "User Agent \t" + userAgent + "\n"
                + "Operating System\t" + clientOS + "\n"
                + "Browser Name\t" + clientBrowser + "\n"
                + "IP Address\t" + clientIpAddr + "\n"
                + "Full URL\t" + fullURL + "\n"
                + "Referrer\t" + referer);
    }

    public static String getClientInfoRaw(HttpServletRequest request) {
        ClientInfo c = new ClientInfo();

        final String referer = c.getReferer(request);
        final String fullURL = c.getFullURL(request);
        final String clientIpAddr = c.getClientIpAddr(request);
        final String clientOS = c.getClientOS(request);
        final String clientBrowser = c.getClientBrowser(request);
        final String userAgent = c.getUserAgent(request);

        return referer + "|" + fullURL + "|" + clientIpAddr + "|" + clientOS + "|" + clientBrowser + "|" + userAgent;
    }

    public static Map<String, Object> getClientInfoMap(HttpServletRequest request) {
        ClientInfo c = new ClientInfo();

        final String referer = c.getReferer(request);
        final String url = c.getFullURL(request);
        final String client_address = c.getClientIpAddr(request);
        final String client_os = c.getClientOS(request);
        final String client_browser = c.getClientBrowser(request);
        final String user_agent = c.getUserAgent(request);

        Map<String, Object> in = new HashMap<>();

        in.put("referer", referer);
        in.put("ulr", url);
        in.put("client_address", client_address);
        in.put("client_os", client_os);
        in.put("client_browser", client_browser);
        in.put("user_agent", user_agent);

        return in;
    }

    public String getReferer(HttpServletRequest request) {
        final String referer = request.getHeader("referer");
        return referer;
    }

    public String getFullURL(HttpServletRequest request) {
        final StringBuffer requestURL = request.getRequestURL();
        final String queryString = request.getQueryString();

        final String result = queryString == null ? requestURL.toString() : requestURL.append('?')
                .append(queryString)
                .toString();

        return result;
    }

    // http://stackoverflow.com/a/18030465/1845894
    public String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // http://stackoverflow.com/a/18030465/1845894
    public String getClientOS(HttpServletRequest request) {
        try {
            final String browserDetails = request.getHeader("User-Agent");

            //=================OS=======================
            final String lowerCaseBrowser = browserDetails.toLowerCase();
            if (lowerCaseBrowser.contains("windows")) {
                return "Windows";
            } else if (lowerCaseBrowser.contains("mac")) {
                return "Mac";
            } else if (lowerCaseBrowser.contains("x11")) {
                return "Unix";
            } else if (lowerCaseBrowser.contains("android")) {
                return "Android";
            } else if (lowerCaseBrowser.contains("iphone")) {
                return "IPhone";
            } else {
                return "UnKnown, More-Info: " + browserDetails;
            }
        } catch (Exception e) {
            return "";
        }
    }

    // http://stackoverflow.com/a/18030465/1845894
    public String getClientBrowser(HttpServletRequest request) {
        try {
            final String browserDetails = request.getHeader("User-Agent");
            final String user = browserDetails.toLowerCase();

            String browser = "";

            //===============Browser===========================
            if (user.contains("msie")) {
                String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
                browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
            } else if (user.contains("safari") && user.contains("version")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split(
                        "/")[0] + "-" + (browserDetails.substring(
                                browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if (user.contains("opr") || user.contains("opera")) {
                if (user.contains("opera")) {
                    browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split(
                            "/")[0] + "-" + (browserDetails.substring(
                                    browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
                } else if (user.contains("opr")) {
                    browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/",
                            "-")).replace(
                                    "OPR", "Opera");
                }
            } else if (user.contains("chrome")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf(
                    "mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf(
                    "mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
                //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
                browser = "Netscape-?";

            } else if (user.contains("firefox")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if (user.contains("rv")) {
                browser = "IE";
            } else {
                browser = "UnKnown, More-Info: " + browserDetails;
            }

            return browser;
        } catch (Exception e) {
            return "";
        }
    }

    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}
