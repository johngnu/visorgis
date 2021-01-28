/*
 * INE - Instituto Nacional de Estadistica 2020
 */
package bo.gob.ine.web;

import bo.gob.ine.services.IEntityServices;
import com.google.gson.Gson;
import com.icg.entityclassutils.EntityResult;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AgropecuarioController
 *
 * @since 30-01-2018
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
@Scope("session")
@RequestMapping(value = "/agropecuario")
public class AgropecuarioController implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(AgropecuarioController.class);
    @Autowired
    private IEntityServices service;
    @Autowired
    ServletContext servletContext;
    private OutputStream os;

    @RequestMapping(value = "/ficha", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> ficha(@RequestParam String id) {
        logger.info("GET ficha data by ID");
        Map<String, Object> data = new HashMap<>();
        try {
            String sql = "select * from disperso.t_fichacensocnaproductor \n"
                    + "where idcomunidad = :id";
            EntityResult er = service.nativeQueryFind(sql, id);

            data.put("data", er.getObjectData());
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    private Map<String, Object> sumData(EntityResult er) {
        Map<String, Object> res = new HashMap<>();
        if (er.getSize() > 0) {
            Set<String> keys = er.getListData().get(0).keySet();
            for (String key : keys) {
                Set<String> strVal = new HashSet<>();
                Double dVal = 0d;
                for (Map<String, Object> m : er.getListData()) {
                    if (m.get(key) != null) {
                        if (m.get(key) instanceof String) {
                            strVal.add(m.get(key).toString());
                        } else if (m.get(key) instanceof Integer || m.get(key) instanceof BigDecimal) {
                            dVal += new Double(m.get(key).toString());
                        }
                    }
                }
                if (strVal.isEmpty()) {
                    res.put(key, dVal);
                } else {
                    res.put(key, strVal.toString().replaceAll("\\[|\\]", ""));
                }
            }

        }
        return res;
    }

    private String paramsToIn(String[] as) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        int y = 0;
        for (String x : as) {
            sb.append("'").append(x).append("'");
            y++;
            if (as.length > y) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @RequestMapping(value = "/ficha/pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> fichaPdf(@RequestParam(required = false) Boolean download) {
        try {
            if (os != null) {
                if (download == null) {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.valueOf("application/pdf"));
                    headers.set("Content-Disposition", "inline; filename=\"ficha.pdf\"");
                    return new ResponseEntity<>(((ByteArrayOutputStream) os).toByteArray(), headers, HttpStatus.OK);
                } else {
                    if (download) {
                        final HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.valueOf("application/pdf"));
                        headers.set("Content-Disposition", "attachment; filename=\"ficha.pdf\"");
                        return new ResponseEntity<>(((ByteArrayOutputStream) os).toByteArray(), headers, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/ficha/data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> fichaData(@RequestParam String ids, @RequestParam(required = false) Boolean download) {
        Map<String, Object> data = new HashMap<>();
        try {
            Gson g = new Gson();
            String[] aids = g.fromJson(ids, String[].class);

            String sql = "select * from disperso.t_fichacensocnaproductor \n"
                    + "where idcomunidad in " + paramsToIn(aids);
            EntityResult er = service.nativeQueryFind(sql);
            Map<String, Object> res = sumData(er);
            res.put("num_puntos", aids.length);
            // vivienda

            os = fichaOutputStream(convertStringMap(res));

            data.put("data", res);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/ficha/unidata", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> iniData(@RequestParam String id, @RequestParam(required = false) Boolean download) {
        Map<String, Object> data = new HashMap<>();
        try {
            // get data
            Map<String, Object> res = service.getExistObject("public", "temporal", "cod_ine", id);
            //             
            os = fichaOutputStream(convertStringMap(res));

            data.put("data", res);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    private Map<String, String> convertStringMap(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>();
        for (Entry<String, Object> e : map.entrySet()) {
            if (e.getValue() != null) {
                result.put(e.getKey(), String.valueOf(e.getValue()));
            } else {
                result.put(e.getKey(), "");
            }
        }
        return result;
    }

    private void setText(PdfContentByte pdfPage, float x, float y, String text, int aligment) throws DocumentException, IOException {
        // Add text in existing PDF
        pdfPage.beginText();
        pdfPage.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, //Font name
                BaseFont.CP1257, //Font encoding
                BaseFont.EMBEDDED //Font embedded
        ), 7); // set font and size

        //pdfPage.setTextMatrix(x, y); // set x and y co-ordinates
        //0, 800 will write text on TOP LEFT of pdf page
        //0, 0 will write text on BOTTOM LEFT of pdf page
        //pdfPage.showText(text); // add the text
        //System.out.println("Text added in " + outputFilePath);        
        pdfPage.showTextAligned(aligment, text, x, y, 0);

        pdfPage.endText();
    }

    private OutputStream fichaOutputStream(Map<String, String> data) {
        try {
            String BLANK_FILE = servletContext.getRealPath("/input") + "/Ficha_INE_CNA.pdf";
            System.out.println(data);

            OutputStream os = new ByteArrayOutputStream();
            PdfReader pdfReader = new PdfReader(BLANK_FILE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, os);

            // Ccontenido en pagina 1
            PdfContentByte pdfPage1 = pdfStamper.getOverContent(1);

            // DATOS INICIALES
            setText(pdfPage1, 160, 747, data.get("depto_estadistico"), Element.ALIGN_LEFT);
            setText(pdfPage1, 160, 738, data.get("prov_estadistico"), Element.ALIGN_LEFT);
            setText(pdfPage1, 160, 729, data.get("mun_estadistico"), Element.ALIGN_LEFT);
            setText(pdfPage1, 160, 720, data.get("nom_comunidad"), Element.ALIGN_LEFT);  //LISTA DE COMUNIDADES

            // DATOS GENERALES
            setText(pdfPage1, 240, 665, data.get("zonaagro"), Element.ALIGN_CENTER);
            setText(pdfPage1, 281, 652, data.get("nroupas"), Element.ALIGN_RIGHT);     // NUMERO DE COMUNIDADES
            setText(pdfPage1, 281, 640, data.get("nroupas"), Element.ALIGN_RIGHT);

            // SUPERFICIE
            setText(pdfPage1, 281, 587, data.get("sup_agrit"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 575, data.get("sup_ver"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 564, data.get("sup_ver_sr"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 281, 552, data.get("sup_ver_r"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 540, data.get("sup_barbec"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 528, data.get("sup_descan"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 281, 516, data.get("sup_ganat"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 504, data.get("sup_pastoc"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 493, data.get("sup_paston"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 281, 481, data.get("sup_fort"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 469, data.get("sup_forest"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 457, data.get("sup_bosque"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 281, 445, data.get("sup_noagrt"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 433, data.get("sup_otrast"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 422, data.get("sup_total"), Element.ALIGN_RIGHT);

            // AGRICULTURA
            setText(pdfPage1, 171, 361, data.get("ai_suptotal"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 349, data.get("ai_cult1"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 349, data.get("ai_sup1"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 349, data.get("ai_prod1"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 349, data.get("ai_rend1"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 337, data.get("ai_cult2"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 337, data.get("ai_sup2"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 337, data.get("ai_prod2"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 337, data.get("ai_rend2"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 325, data.get("ai_cult3"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 325, data.get("ai_sup3"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 325, data.get("ai_prod3"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 325, data.get("ai_rend3"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 314, data.get("ai_cult4"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 314, data.get("ai_sup4"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 314, data.get("ai_prod4"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 314, data.get("ai_rend4"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 171, 278, data.get("av_suptotal"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 266, data.get("av_cult1"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 266, data.get("av_sup1"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 266, data.get("av_prod1"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 266, data.get("av_rend1"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 254, data.get("av_cult2"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 254, data.get("av_sup2"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 254, data.get("av_prod2"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 254, data.get("av_rend2"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 242, data.get("av_cult3"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 242, data.get("av_sup3"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 242, data.get("av_prod3"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 242, data.get("av_rend3"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 230, data.get("av_cult4"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 230, data.get("av_sup4"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 230, data.get("av_prod4"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 230, data.get("av_rend4"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 219, data.get("av_cult5"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 219, data.get("av_sup5"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 219, data.get("av_prod5"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 219, data.get("av_rend5"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 207, data.get("av_cult6"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 207, data.get("av_sup6"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 207, data.get("av_prod6"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 207, data.get("av_rend6"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 48, 195, data.get("av_cult7"), Element.ALIGN_LEFT);
            setText(pdfPage1, 170, 195, data.get("av_sup7"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 228, 195, data.get("av_prod7"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 283, 195, data.get("av_rend7"), Element.ALIGN_RIGHT);

            // CONSTRUCCIONES E INSTALACIONES
            setText(pdfPage1, 535, 652, data.get("ci_silos"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 640, data.get("ci_segrano"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 629, data.get("ci_inverna"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 617, data.get("ci_carpas"), Element.ALIGN_RIGHT);

            // MAQUINARIA, EQUIPOS E IMPLEMENTOS AGRICOLAS
            setText(pdfPage1, 535, 564, data.get("e_tractor"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 552, data.get("e_trimotor"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 540, data.get("e_cosmotor"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 528, data.get("e_enfmotor"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 516, data.get("e_trimanua"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 504, data.get("e_cosmanua"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 493, data.get("e_enfmanua"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 481, data.get("e_motocul"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 469, data.get("e_eqfumiga"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 457, data.get("e_segadora"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 445, data.get("e_ahtanima"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 433, data.get("e_amtanima"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 422, data.get("e_atmecan"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 410, data.get("e_carrastr"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 398, data.get("e_rastras"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 387, data.get("e_tolvaabo"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 375, data.get("e_sembrad"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 363, data.get("e_lavahort"), Element.ALIGN_RIGHT);

            // MIEMBROS DE LA UPA POR ACTIVIDAD
            setText(pdfPage1, 535, 310, data.get("ap_agricul"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 299, data.get("ap_ganader"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 287, data.get("ap_avicola"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 275, data.get("ap_foresta"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 263, data.get("ap_extracc"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 251, data.get("ap_recolec"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 240, data.get("ap_caza"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 228, data.get("ap_pisicol"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 216, data.get("ap_noparti"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 204, data.get("ap_total"), Element.ALIGN_RIGHT);

            //ACTIVIDAD SECUMDARIA
            setText(pdfPage1, 535, 180, data.get("as_mineria"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 168, data.get("as_indmanu"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 157, data.get("as_comerci"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 535, 145, data.get("as_constru"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 133, data.get("as_transpo"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 121, data.get("as_otros"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 109, data.get("as_ninguna"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 535, 98, data.get("as_total"), Element.ALIGN_RIGHT);

            // Ccontenido en pagina 2
            PdfContentByte pdfPage2 = pdfStamper.getOverContent(2);

            // GANADERIA
            setText(pdfPage2, 281, 682, data.get("g_bovino"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 669, data.get("g_bueyes"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 658, data.get("g_bufalos"), Element.ALIGN_RIGHT);

            setText(pdfPage2, 281, 646, data.get("g_ovino"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 634, data.get("g_pgranja"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 622, data.get("g_pcorral"), Element.ALIGN_RIGHT);

            setText(pdfPage2, 281, 610, data.get("g_caprino"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 598, data.get("g_llamas"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 587, data.get("g_alpacas"), Element.ALIGN_RIGHT);

            setText(pdfPage2, 281, 575, data.get("g_caballos"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 563, data.get("g_mulas"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 551, data.get("g_asnos"), Element.ALIGN_RIGHT);

            setText(pdfPage2, 281, 540, data.get("g_conejos"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 528, data.get("g_cuyes"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 516, data.get("g_avgranja"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 281, 504, data.get("g_avcorral"), Element.ALIGN_RIGHT);

            // CAZA Y PESCA
            setText(pdfPage2, 535, 672, data.get("cp_caza"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 661, data.get("cp_pesca"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 649, data.get("cp_cria"), Element.ALIGN_RIGHT);

            // INDICADORES
            setText(pdfPage2, 535, 596, data.get("ind_supupa"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 585, data.get("ind_psupr"), Element.ALIGN_RIGHT);

            pdfStamper.close(); // close pdfStamper

            return os;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/ficha/selected", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selected(@RequestParam String geom) {
        logger.info("GET selected data by geom");
        Map<String, Object> data = new HashMap<>();
        try {
            String sql = "select data.cod_ine, st_astext(data.geom) as geom from \n"
                    + "(select *, st_contains(st_geomfromtext(:geom, 4326),geom) \n"
                    + "from disperso.t_comunidades_cna_publicacion) as data \n"
                    + "where st_contains = true";

            EntityResult er = service.nativeQueryFind(sql, geom);

            data.put("data", er.getListData());
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

}
