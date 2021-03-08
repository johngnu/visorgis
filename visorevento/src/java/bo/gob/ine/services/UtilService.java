/*
 * ICG SRL - International Consulting Group 2017
 */
package bo.gob.ine.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base64;

/**
 * UtilService
 *
 * @author Edy Huiza Yampasi
 * @since 01-10-2017
 */
@Service
public class UtilService {

    public List<Map<String, String>> deserializeJsonList(String json) {
        Gson gson = new Gson();
        List<Map<String, String>> brands = (List<Map<String, String>>) gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {
        }.getType());
        return brands;
    }

    public String listmap_to_json_string(List<Map<String, Object>> list) {
        JSONArray json_arr = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject json_obj = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    json_obj.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            json_arr.put(json_obj);
        }
        JSONObject json_obj = new JSONObject();
        json_obj.put("results", json_arr);
        return json_obj.toString();
    }

    public String listmap_table_json_columns(List<Map<String, Object>> list) {
        JSONArray json_arr = new JSONArray();
        JSONObject json_obj = new JSONObject();
        json_obj.put("data", "name");
        json_obj.put("title", "Variable");
        json_arr.put(json_obj);
        for (Map<String, Object> map : list) {
            json_obj = new JSONObject();
            json_obj.put("data", map.get("gestion"));
            json_obj.put("title", map.get("gestion"));
            json_obj.put("defaultContent", "");
            json_arr.put(json_obj);
        }
        json_obj = new JSONObject();
        json_obj.put("data", "total");
        json_obj.put("title", "Total");
        json_arr.put(json_obj);
        return json_arr.toString();
    }

    public String listmap_to_json_string_columns(List<Map<String, Object>> list) {
        JSONArray json_arr = new JSONArray();
        JSONArray column_arr = new JSONArray();
        System.out.println("LIST: " + list);
        for (Map<String, Object> map : list) {
            JSONObject column_obj = new JSONObject();
            JSONObject json_obj = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    json_obj.put(entry.getKey(), entry.getValue());
                    column_obj.put("title", entry.getKey());
                    column_arr.put(column_obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            json_arr.put(json_obj);

        }
        System.out.println("Columns: " + column_arr.toString());
        JSONObject json_obj = new JSONObject();
        json_obj.put("data", json_arr);
        json_obj.put("recordsTotal", list.size());
        json_obj.put("recordsFiltered", list.size());
        json_obj.put("draw", 1);
        return json_obj.toString();
    }

    public String hashMD5Password(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public String getQRBase64(String data) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            Base64 encode = new Base64();
            return encode.encodeAsString(imageInByte);
        } catch (Exception e) {
            return null;
        }
    }

    public String listmap_to_json_jstree(List<Map<String, Object>> list) {
        JSONArray json_arr = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject json_obj = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                try {
                    if ("node_id".equals(entry.getKey())) {
                        JSONObject attr_obj = new JSONObject();
                        attr_obj.put(key, value);
                        json_obj.put("attr", attr_obj);
                    } else {
                        json_obj.put(key, value);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            json_obj.put("children", true);
            json_arr.put(json_obj);
        }
        return json_arr.toString();
    }

    public static byte[] getByteArray(Reader input) {
        try {
            char[] charArray = new char[8 * 1024];
            StringBuilder builder = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = input.read(charArray, 0, charArray.length)) != -1) {
                builder.append(charArray, 0, numCharsRead);
            }
            byte[] targetArray = builder.toString().getBytes();
            input.close();
            return targetArray;
        } catch (Exception e) {
            return null;
        }
    }
}
