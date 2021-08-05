/*
 * INE - Instituto Nacional de Estadistica 2020
 */
package bo.gob.ine.web;

import bo.gob.ine.services.IEntityServices;
import com.google.gson.Gson;
import com.icg.entityclassutils.EntityResult;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * AmanzanadoController
 *
 * @since 30-01-2018
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
@Scope("session")
@RequestMapping(value = "/amanzanado")
public class AmanzanadoController implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(AmanzanadoController.class);
    @Autowired
    private IEntityServices service;
    @Autowired
    ServletContext servletContext;
    private OutputStream os;
    private OutputStream osx;
    private static String INPUT_FILE = "/Ficha_INE_CNPV.pdf";
    private static String EXCEL_TPL = "/Ficha_INE_CNPV.xlsx";

    @RequestMapping(value = "/ficha", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> ficha(@RequestParam String id) {
        logger.info("GET ficha data by ID");
        Map<String, Object> data = new HashMap<>();
        try {
            String sql = "select * from amanzanado.t_fichacensocnpv \n"
                    + "where idmanzana = :id";
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
            Map<String, Object> keys = er.getListData().get(0);
            for (Entry<String, Object> en : keys.entrySet()) {
                if (en.getValue() instanceof Integer) {
                    Integer x = 0;
                    for (Map<String, Object> m : er.getListData()) {
                        x += Integer.parseInt(m.get(en.getKey()).toString());
                    }
                    res.put(en.getKey(), x);
                } else if (en.getValue() instanceof String) {
                    Set<String> vs = new HashSet<>();
                    for (Map<String, Object> m : er.getListData()) {
                        vs.add(m.get(en.getKey()).toString());
                    }
                    res.put(en.getKey(), vs.toString().replaceAll("\\[|\\]", ""));
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

    @RequestMapping(value = "/ficha/xlsx", method = RequestMethod.GET)
    public ResponseEntity<byte[]> fichaXlsx() {
        try {
            if (osx != null) {
                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.set("Content-Disposition", "inline; filename=\"report_database.xlsx\"");
                return new ResponseEntity<>(((ByteArrayOutputStream) osx).toByteArray(), headers, HttpStatus.OK);
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
            String sql = "select * from amanzanado.t_fichacensocnpv \n"
                    + "where idmanzana in " + paramsToIn(aids);
            EntityResult er = service.nativeQueryFind(sql);
            Map<String, Object> res = sumData(er);
            res.put("num_manzano", aids.length);
            // vivienda
            Integer sv = (Integer) res.get("viv_vivpart")
                    + (Integer) res.get("viv_vivcolec");
            res.put("total_viv", sv);
            // disp. energ. elec.
            Integer eel = (Integer) res.get("viv_sb_enrg_red")
                    + (Integer) res.get("viv_sb_enrg_otrfuente")
                    + (Integer) res.get("viv_sb_enrg_notiene");
            res.put("total_energia", eel);
            // combustible
            Integer com = (Integer) res.get("viv_sb_comb_gasgarraf")
                    + (Integer) res.get("viv_sb_comb_caneria")
                    + (Integer) res.get("viv_sb_comb_lenia")
                    + (Integer) res.get("viv_sb_comb_otros");
            res.put("total_combustible", com);
            // Procedencia del agua
            Integer agu = (Integer) res.get("viv_sb_agua_red")
                    + (Integer) res.get("viv_sb_agua_ppublica")
                    + (Integer) res.get("viv_sb_agua_carro")
                    + (Integer) res.get("viv_sb_agua_pozo")
                    + (Integer) res.get("viv_sb_agua_lluvia")
                    + (Integer) res.get("viv_sb_agua_otros");
            res.put("total_sb_agua", agu);
            // Desague del servicio sanitario
            Integer san = (Integer) res.get("viv_sb_desgu_alcant")
                    + (Integer) res.get("viv_sb_desgu_camsept")
                    + (Integer) res.get("viv_sb_desgu_pozociego")
                    + (Integer) res.get("viv_sb_desgu_calle")
                    + (Integer) res.get("viv_sb_desgu_quebrada")
                    + (Integer) res.get("viv_sb_desgu_lago");
            res.put("total_desague", san);
            // Desague del servicio sanitario
            Integer bas = (Integer) res.get("viv_basura_contened")
                    + (Integer) res.get("viv_basura_carro")
                    + (Integer) res.get("viv_basura_baldio")
                    + (Integer) res.get("viv_basura_rio")
                    + (Integer) res.get("viv_basura_queman")
                    + (Integer) res.get("viv_basura_entierran")
                    + (Integer) res.get("viv_basura_otros");
            res.put("total_basura", bas);
            // fin sumas
            StringBuilder sb = new StringBuilder();
            for (String st : aids) {
                sb.append(st).append(", ");
            }
            res.put("data_ids", sb.toString());

            // System.out.println(res);
            os = fichaOutputStream(convertStringMap(res));
            osx = xFichaOutputStream(er);

            data.put("data", res);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    private Map<String, String> convertStringMap(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>();
        for (Entry<String, Object> e : map.entrySet()) {
            result.put(e.getKey(), String.valueOf(e.getValue()));
        }
        return result;
    }

    private void setText(PdfContentByte pdfPage, float x, float y, String text, int aligment) throws DocumentException, IOException {
        // Add text in existing PDF
        pdfPage.beginText();
        pdfPage.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, //Font name
                BaseFont.CP1252, //Font encoding
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
            String BLANK_FILE = servletContext.getRealPath("/input") + INPUT_FILE;

            OutputStream os = new ByteArrayOutputStream();
            PdfReader pdfReader = new PdfReader(BLANK_FILE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, os);

            // Ccontenido en pagina 1
            PdfContentByte pdfPage1 = pdfStamper.getOverContent(1);

            // DATOS INICIALES
            setText(pdfPage1, 160, 747, data.get("departamento"), Element.ALIGN_LEFT);
            setText(pdfPage1, 160, 738, data.get("provincia"), Element.ALIGN_LEFT);
            setText(pdfPage1, 160, 729, data.get("municipio"), Element.ALIGN_LEFT);
            setText(pdfPage1, 160, 720, data.get("municipio"), Element.ALIGN_LEFT);
            setText(pdfPage1, 160, 712, data.get("num_manzano"), Element.ALIGN_LEFT);

            // POBLACION EMPADRONADA POR SEXO, SEGUN GRUPO DE EDAD
            setText(pdfPage1, 185, 652, data.get("pob_edad_tot"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 652, data.get("pob_edad_toth"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 652, data.get("pob_edad_totm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 640, data.get("pob_edad_0003"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 640, data.get("pob_edad_0003h"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 640, data.get("pob_edad_0003m"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 629, data.get("pob_edad_0405"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 629, data.get("pob_edad_0405h"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 629, data.get("pob_edad_0405m"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 617, data.get("pob_edad_0619"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 617, data.get("pob_edad_0619h"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 617, data.get("pob_edad_0619m"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 605, data.get("pob_edad_2039"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 605, data.get("pob_edad_2039h"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 605, data.get("pob_edad_2039m"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 593, data.get("pob_edad_4059"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 593, data.get("pob_edad_4059h"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 593, data.get("pob_edad_4059m"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 581, data.get("pob_edad_60mas"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 581, data.get("pob_edad_60mash"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 581, data.get("pob_edad_60masm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 552, data.get("pob_total18amast"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 552, data.get("pob_total18amash"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 552, data.get("pob_total18amasm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 532, data.get("pob_m1549t"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 500, data.get("pob_vivpartt"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 500, data.get("pob_vivparth"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 500, data.get("pob_vivpartm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 480, data.get("pob_vivcolectt"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 480, data.get("pob_vivcolecth"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 480, data.get("pob_vivcolectm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 460, data.get("pob_vivctranst"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 460, data.get("pob_vivctransth"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 460, data.get("pob_vivctranstm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 440, data.get("pob_vivcallet"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 440, data.get("pob_vivcalleh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 440, data.get("pob_vivcallem"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 406, data.get("pob_inscregcivt"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 406, data.get("pob_inscregcivh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 406, data.get("pob_inscregcivm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 386, data.get("pob_tienecarnett"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 386, data.get("pob_tienecarneth"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 386, data.get("pob_tienecarnetm"), Element.ALIGN_RIGHT);

            // POBLACIÓN EMPADRONADA POR SEXO, SEGÚN IDIOMA EN EL QUE APRENDIÓ A HABLAR
            setText(pdfPage1, 185, 321, data.get("idioma_ninez_ttotal"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 321, data.get("idioma_ninez_htotalh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 321, data.get("idioma_ninez_mtotalm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 309, data.get("idioma_ninez_tcastellano"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 309, data.get("idioma_ninez_hcastellanoh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 309, data.get("idioma_ninez_mcastellanom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 297, data.get("idioma_ninez_tquechua"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 297, data.get("idioma_ninez_hquechua"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 297, data.get("idioma_ninez_mquechua"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 285, data.get("idioma_ninez_taymara"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 285, data.get("idioma_ninez_haymarah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 285, data.get("idioma_ninez_maymaram"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 273, data.get("idioma_ninez_tguarani"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 273, data.get("idioma_ninez_hguarani"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 273, data.get("idioma_ninez_mguarani"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 262, data.get("idioma_ninez_toficiales"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 262, data.get("idioma_ninez_hoficiales"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 262, data.get("idioma_ninez_moficiales"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 250, data.get("idioma_ninez_totros"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 250, data.get("idioma_ninez_hotrosh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 250, data.get("idioma_ninez_motrosm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 238, data.get("idioma_ninez_textranjero"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 238, data.get("idioma_ninez_hextranjero"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 238, data.get("idioma_ninez_mextranjero"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 226, data.get("idioma_ninez_tnohabla"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 226, data.get("idioma_ninez_hnohablah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 226, data.get("idioma_ninez_mnohablam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 214, data.get("idioma_ninez_tsinespecificar"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 214, data.get("idioma_ninez_hsinespecificarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 214, data.get("idioma_ninez_msinespecificarm"), Element.ALIGN_RIGHT);

            // POBLACIÓN EMPADRONADA DE 6 A 19 AÑOS POR SEXO, SEGÚN ASISTENCIA ESCOLAR
            setText(pdfPage1, 185, 170, data.get("asist_escolart"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 170, data.get("asist_escolarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 170, data.get("asist_escolarm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 158, data.get("asist_asistet"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 158, data.get("asist_asisteh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 158, data.get("asist_asistem"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 147, data.get("asist_noasistet"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 147, data.get("asist_noasisteh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 147, data.get("asist_noasistem"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 185, 135, data.get("asist_sinespecificart"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 233, 135, data.get("asist_sinespecificarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 281, 135, data.get("asist_sinespecificarm"), Element.ALIGN_RIGHT);

            // LUGAR DONDE ACUDE LA POBLACIÓN CUANDO TIENEN PROBLEMAS DE SALUD
            setText(pdfPage1, 441, 648, data.get("salud_cajat"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 648, data.get("salud_cajah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 648, data.get("salud_cajam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 632, data.get("salud_seguro_privadot"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 632, data.get("salud_seguro_privadoh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 632, data.get("salud_seguro_privadom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 616, data.get("salud_publicot"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 616, data.get("salud_publicoh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 616, data.get("salud_publicom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 595, data.get("salud_privadot"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 595, data.get("salud_privadoh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 595, data.get("salud_privadom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 579, data.get("salud_medio_tradicionalt"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 579, data.get("salud_medio_tradicionalh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 579, data.get("salud_medio_tradicionalm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 567, data.get("salud_solcaserast"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 567, data.get("salud_solcaserash"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 567, data.get("salud_solcaserasm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 555, data.get("salud_faramaciat"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 555, data.get("salud_faramaciah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 555, data.get("salud_faramaciam"), Element.ALIGN_RIGHT);

            // POBLACIÓN EMPADRONADA, POR SEXO, SEGÚN LUGAR DE NACIMIENTO Y RESIDENCIA HABITUAL
            setText(pdfPage1, 441, 511, data.get("lugar_nacimientot"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 511, data.get("lugar_nacimientoh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 511, data.get("lugar_nacimientom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 499, data.get("lugar_nacimiento_aquit"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 499, data.get("lugar_nacimiento_aquih"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 499, data.get("lugar_nacimiento_aquim"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 488, data.get("lugar_nacimiento_otrolugart"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 488, data.get("lugar_nacimiento_otrolugarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 488, data.get("lugar_nacimiento_otrolugarm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 476, data.get("lugar_nacimiento_exteriort"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 476, data.get("lugar_nacimiento_exteriorh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 476, data.get("lugar_nacimiento_exteriorm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 441, data.get("lugar_recidenciat"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 441, data.get("lugar_recidenciah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 441, data.get("lugar_recidenciam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 429, data.get("lugar_recidencia_aquit"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 429, data.get("lugar_recidencia_aquih"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 429, data.get("lugar_recidencia_aquim"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 417, data.get("lugar_recidencia_otrolugart"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 417, data.get("lugar_recidencia_otrolugarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 417, data.get("lugar_recidencia_otrolugarm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 405, data.get("lugar_recidencia_exteriort"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 405, data.get("lugar_recidencia_exteriorh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 405, data.get("lugar_recidencia_exteriorm"), Element.ALIGN_RIGHT);

            // POBLACIÓN EMPADRONADA DE 10 AÑOS O MÁS DE EDAD, SEGÚN ACTIVIDAD ECONÓMICA Y CATEGORIA OCUPACIONAL
            setText(pdfPage1, 441, 361, data.get("actividad_total"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 361, data.get("actividad_totalh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 361, data.get("actividad_totalm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 345, data.get("actividad_agricultura"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 345, data.get("actividad_agriculturah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 345, data.get("actividad_agriculturam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 329, data.get("actividad_mineria"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 329, data.get("actividad_mineriah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 329, data.get("actividad_mineriam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 317, data.get("actividad_industria"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 317, data.get("actividad_industriah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 317, data.get("actividad_industriam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 301, data.get("actividad_electricidad"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 301, data.get("actividad_electricidadh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 301, data.get("actividad_electricidadm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 284, data.get("actividad_construccion"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 284, data.get("actividad_construccionh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 284, data.get("actividad_construccionm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 268, data.get("actividad_comercio"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 268, data.get("actividad_comercioh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 268, data.get("actividad_comerciom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 252, data.get("actividad_otrosservicios"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 252, data.get("actividad_otrosserviciosh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 252, data.get("actividad_otrosserviciosm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 240, data.get("actividad_sinespecificar"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 240, data.get("actividad_sinespecificarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 240, data.get("actividad_sinespecificarm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 228, data.get("actividad_descripsionincompleta"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 228, data.get("actividad_descripsionincompletah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 228, data.get("actividad_descripsionincompletam"), Element.ALIGN_RIGHT);

            // Categoría ocupacional
            setText(pdfPage1, 441, 205, data.get("ocupacional_totalt"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 205, data.get("ocupacional_totalh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 205, data.get("ocupacional_totalm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 193, data.get("ocupacional_obrerot"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 193, data.get("ocupacional_obreroh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 193, data.get("ocupacional_obrerom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 181, data.get("ocupacional_hogart"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 181, data.get("ocupacional_hogarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 181, data.get("ocupacional_hogarm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 165, data.get("ocupacional_cuentapropiat"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 165, data.get("ocupacional_cuentapropiah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 165, data.get("ocupacional_cuentapropiam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 149, data.get("ocupacional_sociot"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 149, data.get("ocupacional_socioh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 149, data.get("ocupacional_sociom"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 133, data.get("ocupacional_familiart"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 133, data.get("ocupacional_familiarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 133, data.get("ocupacional_familiarm"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 112, data.get("ocupacional_cooperativistat"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 112, data.get("ocupacional_cooperativistah"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 112, data.get("ocupacional_cooperativistam"), Element.ALIGN_RIGHT);

            setText(pdfPage1, 441, 96, data.get("ocupacional_sinespecificart"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 489, 96, data.get("ocupacional_sinespecificarh"), Element.ALIGN_RIGHT);
            setText(pdfPage1, 537, 96, data.get("ocupacional_sinespecificarm"), Element.ALIGN_RIGHT);

            // Ccontenido en pagina 2
            PdfContentByte pdfPage2 = pdfStamper.getOverContent(2);

            // Viviendas
            setText(pdfPage2, 280, 699, data.get("total_viv"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 687, data.get("viv_vivpart"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 675, data.get("viv_vivcolec"), Element.ALIGN_RIGHT);

            // Disponibilidad de energía eléctrica
            setText(pdfPage2, 280, 652, data.get("total_energia"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 640, data.get("viv_sb_enrg_red"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 628, data.get("viv_sb_enrg_otrfuente"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 615, data.get("viv_sb_enrg_notiene"), Element.ALIGN_RIGHT);

            // Combustible o energía más utilizado para cocinar
            setText(pdfPage2, 280, 592, data.get("total_combustible"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 580, data.get("viv_sb_comb_gasgarraf"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 569, data.get("viv_sb_comb_caneria"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 557, data.get("viv_sb_comb_lenia"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 545, data.get("viv_sb_comb_otros"), Element.ALIGN_RIGHT);

            // Procedencia del agua que utilizan en la vivienda
            setText(pdfPage2, 280, 521, data.get("total_sb_agua"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 509, data.get("viv_sb_agua_red"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 498, data.get("viv_sb_agua_ppublica"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 486, data.get("viv_sb_agua_carro"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 474, data.get("viv_sb_agua_pozo"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 462, data.get("viv_sb_agua_lluvia"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 280, 450, data.get("viv_sb_agua_otros"), Element.ALIGN_RIGHT);

            // Desague del servicio sanitario
            setText(pdfPage2, 535, 702, data.get("total_desague"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 690, data.get("viv_sb_desgu_alcant"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 678, data.get("viv_sb_desgu_camsept"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 667, data.get("viv_sb_desgu_pozociego"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 655, data.get("viv_sb_desgu_calle"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 643, data.get("viv_sb_desgu_quebrada"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 631, data.get("viv_sb_desgu_lago"), Element.ALIGN_RIGHT);

            // Eliminación de la basura
            setText(pdfPage2, 535, 595, data.get("total_basura"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 580, data.get("viv_basura_contened"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 559, data.get("viv_basura_carro"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 539, data.get("viv_basura_baldio"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 523, data.get("viv_basura_rio"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 511, data.get("viv_basura_queman"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 499, data.get("viv_basura_entierran"), Element.ALIGN_RIGHT);
            setText(pdfPage2, 535, 487, data.get("viv_basura_otros"), Element.ALIGN_RIGHT);

            // Ccontenido en pagina 3
            PdfContentByte pdfPage3 = pdfStamper.getOverContent(3); //data_ids

            ColumnText ct = new ColumnText(pdfPage3);
            ct.setSimpleColumn(new Phrase(new Chunk(data.get("data_ids"), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL))),
                    50, 700, 500, 36, 25, Element.ALIGN_LEFT | Element.ALIGN_TOP);
            ct.go();

            pdfStamper.close(); // close pdfStamper

            return os;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private OutputStream xFichaOutputStream(EntityResult er) {
        try {
            // Open XLSX File template
            String BLANK_FILE = servletContext.getRealPath("/input") + EXCEL_TPL;
            FileInputStream file = new FileInputStream(BLANK_FILE);
            Workbook workbook = new XSSFWorkbook(file);
            // Select First or Defalut Sheet
            Sheet sheet = workbook.getSheetAt(0);

            int startRow = 6;
            int totalRows = er.getSize(); // total recor5ds (size data)

            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(sheet.getRow(startRow - 1).getCell(0).getCellStyle()); // Copy cell style

            sheet.shiftRows(startRow, sheet.getLastRowNum(), totalRows - 1);

            //sheet.shiftRows(7, 10, 7, true, true);
            int indexReport = startRow - 1;

            int i = 0;
            for (Map<String, Object> rec : er.getListData()) {
                Row w = sheet.getRow(indexReport + i);
                if (w == null) {
                    w = sheet.createRow(indexReport + i);
                    w.createCell(0).setCellStyle(newCellStyle);
                    w.createCell(1).setCellStyle(newCellStyle);
                    w.createCell(2).setCellStyle(newCellStyle);
                    w.createCell(3).setCellStyle(newCellStyle);
                    w.createCell(4).setCellStyle(newCellStyle);
                }
                // values
                w.getCell(0).setCellValue(rec.get("departamento").toString());
                w.getCell(1).setCellValue(rec.get("provincia").toString());
                w.getCell(2).setCellValue(rec.get("municipio").toString());
                w.getCell(3).setCellValue(rec.get("comunidad").toString());
                w.getCell(4).setCellValue(rec.get("cod_manzano").toString());

                i++;
            }

            OutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            os.close();
            return os;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping(value = "/ficha/selected", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selected(@RequestParam String geom, @RequestParam Boolean drawn) {
        logger.info("GET selected data by geom");
        Map<String, Object> data = new HashMap<>();
        try {
            System.out.println(drawn + " - " + geom);
            String sql = "";
            if (drawn) {
                // st_intersects
                sql = "select data.idmanzana, st_astext(data.geom) as geom from \n"
                        + "(select *, st_intersects(st_geomfromtext(:geom, 4326),geom) \n"
                        + "from amanzanado.v_fichamanzana) as data \n"
                        + "where st_intersects = true";
            } else {
                // st_contains
                sql = "select data.idmanzana, st_astext(data.geom) as geom from \n"
                        + "(select *, st_contains(st_geomfromtext(:geom, 4326),geom) \n"
                        + "from amanzanado.v_fichamanzana) as data \n"
                        + "where st_contains = true";
            }

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

    @RequestMapping(value = "/getdata", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getData(@RequestParam String geom) {
        logger.info("GET selected data by geom");
        Map<String, Object> data = new HashMap<>();
        try {

            /*String sql = "select data.id_manz, data.depto, data.prov, data.mpio, data.ciu_com, data.distrito, "
                    + "data.cod_cd_com, data.cod_loc, "
                    + "data.orden_manz, data.cod_ac, data.t_viv_ocu, data.t_viv_des, data.total_viv, data.total_pob, "
                    + "st_astext(data.geom) as geom from \n"
                    + "(select *, st_intersects(st_geomfromtext(:geom, 4326),geom) \n"
                    + "from ad_bol.bolivia_manzano_poligono) as data \n"
                    + "where st_intersects = true";
                        
            EntityResult er = service.nativeQueryFind(sql, geom);*/
            Gson gson = new Gson();
            String[] geoms = gson.fromJson(geom, String[].class);

            String sql = "select data.id_manz, data.depto, data.prov, data.mpio, data.ciu_com, data.distrito, "
                    + "data.cod_cd_com, data.cod_loc, "
                    + "data.orden_manz, data.cod_ac, data.t_viv_ocu, data.t_viv_des, data.total_viv, data.total_pob, "
                    + "st_astext(data.geom) as geom from \n"
                    + "(select *, st_intersects(st_geomfromtext(:geom, 4326),geom) \n"
                    + "from ad_bol.bolivia_manzano_poligono) as data \n"
                    + "where st_intersects = true";

            List<Map<String, Object>> res = new ArrayList();
            for (String g : geoms) {
                EntityResult er = service.nativeQueryFind(sql, g);
                res.addAll(er.getListData());
            }

            osx = xReportOutputStream(res);

            data.put("data", res);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    private String toString(Object o) {
        if (o != null) {
            return o.toString();
        }
        return "";
    }

    private void setNumber(Cell c, Object o) {
        if (o != null) {
            c.setCellValue(Double.parseDouble(o.toString()));
        }
    }

    private OutputStream xReportOutputStream(List<Map<String, Object>> er) {
        try {
            // Open XLSX File template
            String BLANK_FILE = servletContext.getRealPath("/input") + "/mnz_report.xlsx";
            FileInputStream file = new FileInputStream(BLANK_FILE);
            Workbook workbook = new XSSFWorkbook(file);
            // Select First or Defalut Sheet
            Sheet sheet = workbook.getSheetAt(0);

            int startRow = 6;
            int totalRows = er.size(); // total recor5ds (size data)

            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(sheet.getRow(startRow - 1).getCell(0).getCellStyle()); // Copy cell style
            if ((totalRows - 1) > 0) {
                sheet.shiftRows(startRow, sheet.getLastRowNum(), totalRows - 1);
            }
            //sheet.shiftRows(7, 10, 7, true, true);
            int indexReport = startRow - 1;

            int i = 0;
            for (Map<String, Object> rec : er) {
                Row w = sheet.getRow(indexReport + i);
                if (w == null) {
                    w = sheet.createRow(indexReport + i);
                    w.createCell(0).setCellStyle(newCellStyle);
                    w.createCell(1).setCellStyle(newCellStyle);
                    w.createCell(2).setCellStyle(newCellStyle);
                    w.createCell(3).setCellStyle(newCellStyle);
                    w.createCell(4).setCellStyle(newCellStyle);
                    w.createCell(5).setCellStyle(newCellStyle);
                    w.createCell(6).setCellStyle(newCellStyle);
                    w.createCell(7).setCellStyle(newCellStyle);
                    w.createCell(8).setCellStyle(newCellStyle);
                    w.createCell(9).setCellStyle(newCellStyle);
                    w.createCell(10).setCellStyle(newCellStyle);
                    w.createCell(11).setCellStyle(newCellStyle);
                }
                // values [string data]
                w.getCell(0).setCellValue(toString(rec.get("id_manz")));
                w.getCell(1).setCellValue(toString(rec.get("depto")));
                w.getCell(2).setCellValue(toString(rec.get("prov")));
                w.getCell(3).setCellValue(toString(rec.get("mpio")));
                w.getCell(3).setCellValue(toString(rec.get("ciu_com")));

                w.getCell(5).setCellValue(toString(rec.get("distrito")));
                w.getCell(6).setCellValue(toString(rec.get("orden_manz")));
                w.getCell(7).setCellValue(toString(rec.get("cod_ac")));
                // Double or number data sample
                setNumber(w.getCell(8), rec.get("t_viv_ocu"));
                setNumber(w.getCell(9), rec.get("t_viv_des"));
                setNumber(w.getCell(10), rec.get("total_viv"));
                setNumber(w.getCell(11), rec.get("total_pob"));

                i++;
            }

            OutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            os.close();
            return os;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
