/*
 * ICG SRL - International Consulting Group 2017
 */
package bo.gob.ine.web;

import bo.gob.ine.services.IEntityServices;
import com.google.gson.Gson;
import com.icg.entityclassutils.EntityResult;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * VisorController
 *
 * @since 30-01-2018
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
@Scope("session")
@RequestMapping(value = "/visor")
public class VisorController implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(VisorController.class);
    @Autowired
    private IEntityServices service;
    @Autowired
    ServletContext servletContext;
    private OutputStream os;

    @RequestMapping
    public String index(Model model) {
        return "iweb/index";
    }

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
                    res.put(en.getKey(), vs.toString());
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
            String sql = "select * from amanzanado.t_fichacensocnpv \n"
                    + "where idmanzana in " + paramsToIn(aids);
            EntityResult er = service.nativeQueryFind(sql);
            Map<String, Object> res = sumData(er);
            System.out.println(res);
            os = fichaOutputStream(convertStringMap(res));

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

    private void setText(PdfContentByte pdfPage, float x, float y, String text) throws DocumentException, IOException {
        // Add text in existing PDF
        pdfPage.beginText();
        pdfPage.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, //Font name
                BaseFont.CP1257, //Font encoding
                BaseFont.EMBEDDED //Font embedded
        ), 7); // set font and size

        pdfPage.setTextMatrix(x, y); // set x and y co-ordinates
        //0, 800 will write text on TOP LEFT of pdf page
        //0, 0 will write text on BOTTOM LEFT of pdf page
        pdfPage.showText(text); // add the text
        //System.out.println("Text added in " + outputFilePath);

        pdfPage.endText();
    }

    private OutputStream fichaOutputStream(Map<String, String> data) {
        try {
            String BLANK_FILE = servletContext.getRealPath("/input") + "/Ficha_INE_CNPV.pdf";

            OutputStream os = new ByteArrayOutputStream();
            PdfReader pdfReader = new PdfReader(BLANK_FILE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, os);

            // Ccontenido en pagina 1
            PdfContentByte pdfPage1 = pdfStamper.getOverContent(1);

            // DATOS INICIALES
            setText(pdfPage1, 160, 747, data.get("departamento"));
            setText(pdfPage1, 160, 738, data.get("provincia"));
            setText(pdfPage1, 160, 729, data.get("municipio"));
            setText(pdfPage1, 160, 720, data.get("municipio"));
            setText(pdfPage1, 160, 712, data.get("num_manzano"));

            // POBLACION EMPADRONADA POR SEXO, SEGUN GRUPO DE EDAD
            setText(pdfPage1, 160, 652, data.get("pob_edad_tot"));
            setText(pdfPage1, 208, 652, data.get("pob_edad_toth"));
            setText(pdfPage1, 257, 652, data.get("pob_edad_totm"));

            setText(pdfPage1, 160, 640, data.get("pob_edad_0003"));
            setText(pdfPage1, 208, 640, data.get("pob_edad_0003h"));
            setText(pdfPage1, 257, 640, data.get("pob_edad_0003m"));

            setText(pdfPage1, 160, 629, data.get("pob_edad_0405"));
            setText(pdfPage1, 208, 629, data.get("pob_edad_0405h"));
            setText(pdfPage1, 257, 629, data.get("pob_edad_0405m"));

            setText(pdfPage1, 160, 617, data.get("pob_edad_0619"));
            setText(pdfPage1, 208, 617, data.get("pob_edad_0619h"));
            setText(pdfPage1, 257, 617, data.get("pob_edad_0619m"));

            setText(pdfPage1, 160, 605, data.get("pob_edad_2039"));
            setText(pdfPage1, 208, 605, data.get("pob_edad_2039h"));
            setText(pdfPage1, 257, 605, data.get("pob_edad_2039m"));

            setText(pdfPage1, 160, 593, data.get("pob_edad_4059"));
            setText(pdfPage1, 208, 593, data.get("pob_edad_4059h"));
            setText(pdfPage1, 257, 593, data.get("pob_edad_4059m"));

            setText(pdfPage1, 160, 581, data.get("pob_edad_60mas"));
            setText(pdfPage1, 208, 581, data.get("pob_edad_60mash"));
            setText(pdfPage1, 257, 581, data.get("pob_edad_60masm"));

            setText(pdfPage1, 160, 552, data.get("pob_total18amast"));
            setText(pdfPage1, 208, 552, data.get("pob_total18amash"));
            setText(pdfPage1, 257, 552, data.get("pob_total18amasm"));

            setText(pdfPage1, 160, 532, data.get("pob_m1549t"));

            setText(pdfPage1, 160, 500, data.get("pob_vivpartt"));
            setText(pdfPage1, 208, 500, data.get("pob_vivparth"));
            setText(pdfPage1, 257, 500, data.get("pob_vivpartm"));

            setText(pdfPage1, 160, 480, data.get("pob_vivcolectt"));
            setText(pdfPage1, 208, 480, data.get("pob_vivcolecth"));
            setText(pdfPage1, 257, 480, data.get("pob_vivcolectm"));

            setText(pdfPage1, 160, 460, data.get("pob_vivctranst"));
            setText(pdfPage1, 208, 460, data.get("pob_vivctransth"));
            setText(pdfPage1, 257, 460, data.get("pob_vivctranstm"));

            setText(pdfPage1, 160, 440, data.get("pob_vivcallet"));
            setText(pdfPage1, 208, 440, data.get("pob_vivcalleh"));
            setText(pdfPage1, 257, 440, data.get("pob_vivcallem"));

            setText(pdfPage1, 160, 406, data.get("pob_inscregcivt"));
            setText(pdfPage1, 208, 406, data.get("pob_inscregcivh"));
            setText(pdfPage1, 257, 406, data.get("pob_inscregcivm"));

            setText(pdfPage1, 160, 386, data.get("pob_tienecarnett"));
            setText(pdfPage1, 208, 386, data.get("pob_tienecarneth"));
            setText(pdfPage1, 257, 386, data.get("pob_tienecarnetm"));

            // POBLACIÓN EMPADRONADA POR SEXO, SEGÚN IDIOMA EN EL QUE APRENDIÓ A HABLAR
            setText(pdfPage1, 160, 321, data.get("idioma_ninez_ttotal"));
            setText(pdfPage1, 208, 321, data.get("idioma_ninez_htotalh"));
            setText(pdfPage1, 257, 321, data.get("idioma_ninez_mtotalm"));

            setText(pdfPage1, 160, 309, data.get("idioma_ninez_tcastellano"));
            setText(pdfPage1, 208, 309, data.get("idioma_ninez_hcastellanoh"));
            setText(pdfPage1, 257, 309, data.get("idioma_ninez_mcastellanom"));

            setText(pdfPage1, 160, 297, data.get("idioma_ninez_tquechua"));
            setText(pdfPage1, 208, 297, data.get("idioma_ninez_hquechua"));
            setText(pdfPage1, 257, 297, data.get("idioma_ninez_mquechua"));

            setText(pdfPage1, 160, 285, data.get("idioma_ninez_taymara"));
            setText(pdfPage1, 208, 285, data.get("idioma_ninez_haymarah"));
            setText(pdfPage1, 257, 285, data.get("idioma_ninez_maymaram"));

            setText(pdfPage1, 160, 273, data.get("idioma_ninez_tguarani"));
            setText(pdfPage1, 208, 273, data.get("idioma_ninez_hguarani"));
            setText(pdfPage1, 257, 273, data.get("idioma_ninez_mguarani"));

            setText(pdfPage1, 160, 262, data.get("idioma_ninez_toficiales"));
            setText(pdfPage1, 208, 262, data.get("idioma_ninez_hoficiales"));
            setText(pdfPage1, 257, 262, data.get("idioma_ninez_moficiales"));

            setText(pdfPage1, 160, 250, data.get("idioma_ninez_totros"));
            setText(pdfPage1, 208, 250, data.get("idioma_ninez_hotrosh"));
            setText(pdfPage1, 257, 250, data.get("idioma_ninez_motrosm"));

            setText(pdfPage1, 160, 238, data.get("idioma_ninez_textranjero"));
            setText(pdfPage1, 208, 238, data.get("idioma_ninez_hextranjero"));
            setText(pdfPage1, 257, 238, data.get("idioma_ninez_mextranjero"));

            setText(pdfPage1, 160, 226, data.get("idioma_ninez_tnohabla"));
            setText(pdfPage1, 208, 226, data.get("idioma_ninez_hnohablah"));
            setText(pdfPage1, 257, 226, data.get("idioma_ninez_mnohablam"));

            setText(pdfPage1, 160, 214, data.get("idioma_ninez_tsinespecificar"));
            setText(pdfPage1, 208, 214, data.get("idioma_ninez_hsinespecificarh"));
            setText(pdfPage1, 257, 214, data.get("idioma_ninez_msinespecificarm"));

            // POBLACIÓN EMPADRONADA DE 6 A 19 AÑOS POR SEXO, SEGÚN ASISTENCIA ESCOLAR
            setText(pdfPage1, 160, 170, data.get("asist_escolart"));
            setText(pdfPage1, 208, 170, data.get("asist_escolarh"));
            setText(pdfPage1, 257, 170, data.get("asist_escolarm"));

            setText(pdfPage1, 160, 158, data.get("asist_asistet"));
            setText(pdfPage1, 208, 158, data.get("asist_asisteh"));
            setText(pdfPage1, 257, 158, data.get("asist_asistem"));

            setText(pdfPage1, 160, 147, data.get("asist_noasistet"));
            setText(pdfPage1, 208, 147, data.get("asist_noasisteh"));
            setText(pdfPage1, 257, 147, data.get("asist_noasistem"));

            setText(pdfPage1, 160, 135, data.get("asist_sinespecificart"));
            setText(pdfPage1, 208, 135, data.get("asist_sinespecificarh"));
            setText(pdfPage1, 257, 135, data.get("asist_sinespecificarm"));

            // LUGAR DONDE ACUDE LA POBLACIÓN CUANDO TIENEN PROBLEMAS DE SALUD
            setText(pdfPage1, 415, 648, data.get("salud_cajat"));
            setText(pdfPage1, 463, 648, data.get("salud_cajah"));
            setText(pdfPage1, 512, 648, data.get("salud_cajam"));

            setText(pdfPage1, 415, 632, data.get("salud_seguro_privadot"));
            setText(pdfPage1, 463, 632, data.get("salud_seguro_privadoh"));
            setText(pdfPage1, 512, 632, data.get("salud_seguro_privadom"));

            setText(pdfPage1, 415, 616, data.get("salud_seguro_publicot"));
            setText(pdfPage1, 463, 616, data.get("salud_seguro_publicoh"));
            setText(pdfPage1, 512, 616, data.get("salud_seguro_publicom"));

            setText(pdfPage1, 415, 595, data.get("salud_privadot"));
            setText(pdfPage1, 463, 595, data.get("salud_privadoh"));
            setText(pdfPage1, 512, 595, data.get("salud_privadom"));

            setText(pdfPage1, 415, 579, data.get("salud_medio_tradicionalt"));
            setText(pdfPage1, 463, 579, data.get("salud_medio_tradicionalh"));
            setText(pdfPage1, 512, 579, data.get("salud_medio_tradicionalm"));

            setText(pdfPage1, 415, 567, data.get("salud_solcaserast"));
            setText(pdfPage1, 463, 567, data.get("salud_solcaserash"));
            setText(pdfPage1, 512, 567, data.get("salud_solcaserasm"));

            setText(pdfPage1, 415, 555, data.get("salud_farmaciat"));
            setText(pdfPage1, 463, 555, data.get("salud_farmaciah"));
            setText(pdfPage1, 512, 555, data.get("salud_farmaciam"));

            // POBLACIÓN EMPADRONADA, POR SEXO, SEGÚN LUGAR DE NACIMIENTO Y RESIDENCIA HABITUAL
            setText(pdfPage1, 415, 511, data.get("lugar_nacimientot"));
            setText(pdfPage1, 463, 511, data.get("lugar_nacimientoh"));
            setText(pdfPage1, 512, 511, data.get("lugar_nacimientom"));

            setText(pdfPage1, 415, 499, data.get("lugar_nacimiento_aquit"));
            setText(pdfPage1, 463, 499, data.get("lugar_nacimiento_aquih"));
            setText(pdfPage1, 512, 499, data.get("lugar_nacimiento_aquim"));

            setText(pdfPage1, 415, 488, data.get("lugar_nacimiento_otrolugart"));
            setText(pdfPage1, 463, 488, data.get("lugar_nacimiento_otrolugarh"));
            setText(pdfPage1, 512, 488, data.get("lugar_nacimiento_otrolugarm"));

            setText(pdfPage1, 415, 476, data.get("lugar_nacimiento_exteriort"));
            setText(pdfPage1, 463, 476, data.get("lugar_nacimiento_exteriorh"));
            setText(pdfPage1, 512, 476, data.get("lugar_nacimiento_exteriorm"));

            setText(pdfPage1, 415, 441, data.get("lugar_recidenciat"));
            setText(pdfPage1, 463, 441, data.get("lugar_recidenciah"));
            setText(pdfPage1, 512, 441, data.get("lugar_recidenciam"));

            setText(pdfPage1, 415, 429, data.get("lugar_recidencia_aquit"));
            setText(pdfPage1, 463, 429, data.get("lugar_recidencia_aquih"));
            setText(pdfPage1, 512, 429, data.get("lugar_recidencia_aquim"));

            setText(pdfPage1, 415, 417, data.get("lugar_recidencia_otrolugart"));
            setText(pdfPage1, 463, 417, data.get("lugar_recidencia_otrolugarh"));
            setText(pdfPage1, 512, 417, data.get("lugar_recidencia_otrolugarm"));

            setText(pdfPage1, 415, 405, data.get("lugar_recidencia_exteriort"));
            setText(pdfPage1, 463, 405, data.get("lugar_recidencia_exteriorh"));
            setText(pdfPage1, 512, 405, data.get("lugar_recidencia_exteriorm"));

            // POBLACIÓN EMPADRONADA DE 10 AÑOS O MÁS DE EDAD, SEGÚN ACTIVIDAD ECONÓMICA Y CATEGORIA OCUPACIONAL
            setText(pdfPage1, 415, 361, data.get("actividad_total"));
            setText(pdfPage1, 463, 361, data.get("actividad_totalh"));
            setText(pdfPage1, 512, 361, data.get("actividad_totalm"));

            setText(pdfPage1, 415, 345, data.get("actividad_agricultura"));
            setText(pdfPage1, 463, 345, data.get("actividad_agriculturah"));
            setText(pdfPage1, 512, 345, data.get("actividad_agriculturam"));

            setText(pdfPage1, 415, 329, data.get("actividad_mineria"));
            setText(pdfPage1, 463, 329, data.get("actividad_mineriah"));
            setText(pdfPage1, 512, 329, data.get("actividad_mineriam"));

            setText(pdfPage1, 415, 317, data.get("actividad_industria"));
            setText(pdfPage1, 463, 317, data.get("actividad_industriah"));
            setText(pdfPage1, 512, 317, data.get("actividad_industriam"));

            setText(pdfPage1, 415, 301, data.get("actividad_electricidad"));
            setText(pdfPage1, 463, 301, data.get("actividad_electricidadh"));
            setText(pdfPage1, 512, 301, data.get("actividad_electricidadm"));

            setText(pdfPage1, 415, 284, data.get("actividad_construccion"));
            setText(pdfPage1, 463, 284, data.get("actividad_construccionh"));
            setText(pdfPage1, 512, 284, data.get("actividad_construccionm"));

            setText(pdfPage1, 415, 268, data.get("actividad_comercio"));
            setText(pdfPage1, 463, 268, data.get("actividad_comercioh"));
            setText(pdfPage1, 512, 268, data.get("actividad_comerciom"));

            setText(pdfPage1, 415, 252, data.get("actividad_otrosservicios"));
            setText(pdfPage1, 463, 252, data.get("actividad_otrosserviciosh"));
            setText(pdfPage1, 512, 252, data.get("actividad_otrosserviciosm"));

            setText(pdfPage1, 415, 240, data.get("actividad_sinespecificar"));
            setText(pdfPage1, 463, 240, data.get("actividad_sinespecificarh"));
            setText(pdfPage1, 512, 240, data.get("actividad_sinespecificarm"));

            setText(pdfPage1, 415, 228, data.get("actividad_descripsionincompleta"));
            setText(pdfPage1, 463, 228, data.get("actividad_descripsionincompletah"));
            setText(pdfPage1, 512, 228, data.get("actividad_descripsionincompletam"));

            // Categoría ocupacional
            setText(pdfPage1, 415, 205, data.get("ocupacional_totalt"));
            setText(pdfPage1, 463, 205, data.get("ocupacional_totalh"));
            setText(pdfPage1, 512, 205, data.get("ocupacional_totalm"));

            setText(pdfPage1, 415, 193, data.get("ocupacional_obrerot"));
            setText(pdfPage1, 463, 193, data.get("ocupacional_obreroh"));
            setText(pdfPage1, 512, 193, data.get("ocupacional_obrerom"));

            setText(pdfPage1, 415, 181, data.get("ocupacional_hogart"));
            setText(pdfPage1, 463, 181, data.get("ocupacional_hogarh"));
            setText(pdfPage1, 512, 181, data.get("ocupacional_hogarm"));

            setText(pdfPage1, 415, 165, data.get("ocupacional_cuentapropiat"));
            setText(pdfPage1, 463, 165, data.get("ocupacional_cuentapropiah"));
            setText(pdfPage1, 512, 165, data.get("ocupacional_cuentapropiam"));

            setText(pdfPage1, 415, 149, data.get("ocupacional_sociot"));
            setText(pdfPage1, 463, 149, data.get("ocupacional_socioh"));
            setText(pdfPage1, 512, 149, data.get("ocupacional_sociom"));

            setText(pdfPage1, 415, 133, data.get("ocupacional_familiart"));
            setText(pdfPage1, 463, 133, data.get("ocupacional_familiarh"));
            setText(pdfPage1, 512, 133, data.get("ocupacional_familiarm"));

            setText(pdfPage1, 415, 112, data.get("ocupacional_cooperativistat"));
            setText(pdfPage1, 463, 112, data.get("ocupacional_cooperativistah"));
            setText(pdfPage1, 512, 112, data.get("ocupacional_cooperativistam"));

            setText(pdfPage1, 415, 96, data.get("ocupacional_sinespecificart"));
            setText(pdfPage1, 463, 96, data.get("ocupacional_sinespecificarh"));
            setText(pdfPage1, 512, 96, data.get("ocupacional_sinespecificarm"));

            // Ccontenido en pagina 2
            PdfContentByte pdfPage2 = pdfStamper.getOverContent(2);

            // Viviendas
            setText(pdfPage2, 250, 699, data.get("total_viv"));
            setText(pdfPage2, 250, 687, data.get("viv_vivpart"));
            setText(pdfPage2, 250, 675, data.get("viv_vivcolec"));

            // Disponibilidad de energía eléctrica
            setText(pdfPage2, 250, 652, data.get("total_energia"));
            setText(pdfPage2, 250, 640, data.get("viv_sb_enrg_red"));
            setText(pdfPage2, 250, 628, data.get("viv_sb_enrg_otrfuente"));
            setText(pdfPage2, 250, 614, data.get("viv_sb_enrg_notiene"));

            // Combustible o energía más utilizado para cocinar
            setText(pdfPage2, 250, 592, data.get("total_combustible"));
            setText(pdfPage2, 250, 580, data.get("viv_sb_comb_gasgarraf"));
            setText(pdfPage2, 250, 569, data.get("viv_sb_comb_caneria"));
            setText(pdfPage2, 250, 557, data.get("viv_sb_comb_lenia"));
            setText(pdfPage2, 250, 545, data.get("viv_sb_comb_otros"));

            // Procedencia del agua que utilizan en la vivienda
            setText(pdfPage2, 250, 521, data.get("total_sb_agua"));
            setText(pdfPage2, 250, 509, data.get("viv_sb_agua_red"));
            setText(pdfPage2, 250, 498, data.get("viv_sb_agua_ppublica"));
            setText(pdfPage2, 250, 486, data.get("viv_sb_agua_carro"));
            setText(pdfPage2, 250, 474, data.get("viv_sb_agua_pozo"));
            setText(pdfPage2, 250, 462, data.get("viv_sb_agua_lluvia"));
            setText(pdfPage2, 250, 450, data.get("viv_sb_agua_otros"));

            // Desague del servicio sanitario
            setText(pdfPage2, 475, 702, data.get("total_desague"));
            setText(pdfPage2, 475, 690, data.get("viv_sb_desgu_alcant"));
            setText(pdfPage2, 475, 678, data.get("viv_sb_desgu_camsept"));
            setText(pdfPage2, 475, 667, data.get("viv_sb_desgu_pozociego"));
            setText(pdfPage2, 475, 655, data.get("viv_sb_desgu_calle"));
            setText(pdfPage2, 475, 643, data.get("viv_sb_desgu_quebrada"));
            setText(pdfPage2, 475, 631, data.get("viv_sb_desgu_lago"));

            // Eliminación de la basura
            setText(pdfPage2, 475, 595, data.get("total_basura"));
            setText(pdfPage2, 475, 580, data.get("viv_basura_contened"));
            setText(pdfPage2, 475, 559, data.get("viv_basura_carro"));
            setText(pdfPage2, 475, 539, data.get("viv_basura_baldio"));
            setText(pdfPage2, 475, 523, data.get("viv_basura_rio"));
            setText(pdfPage2, 475, 511, data.get("viv_basura_queman"));
            setText(pdfPage2, 475, 499, data.get("viv_basura_entierran"));
            setText(pdfPage2, 475, 487, data.get("viv_basura_otros"));

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
            String sql = "select data.idmanzana, st_astext(data.geom) as geom from \n"
                    + "(select *, st_contains(st_geomfromtext(:geom, 4326),geom) \n"
                    + "from amanzanado.t_manzanas_view) as data \n"
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
