/*
 * Program to modify existing pdf file
 */
package bo.gob.ine.web;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author John Castillo
 */
public class Ficha {

    public static void setText(PdfContentByte pdfPage, float x, float y, String text) throws DocumentException, IOException {
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

    public static Map<String, String> data() {
        Map<String, String> data = new HashMap<>();
        // Map data<field, value> 
       
        // DATOS INICIALES
        data.put("departamento", "ORURO");
        data.put("provincia", "CERCADO");
        data.put("municipio", "ORURO");
        data.put("municipio", "ORURO");
        data.put("num_manzano", "100");     //Sumatoria
        
        // POBLACION EMPADRONADA POR SEXO, SEGUN GRUPO DE EDAD
        data.put("pob_edad_tot", "101"); 
        data.put("pob_edad_toth", "102");
        data.put("pob_edad_totm", "103");
        
        data.put("pob_edad_0003", "104"); 
        data.put("pob_edad_0003h", "105");
        data.put("pob_edad_0003m", "106");
        
        data.put("pob_edad_0405", "107"); 
        data.put("pob_edad_0405h", "108");
        data.put("pob_edad_0405m", "109");
        
        data.put("pob_edad_0619", "110"); 
        data.put("pob_edad_0619h", "111");
        data.put("pob_edad_0619m", "112");
        
        data.put("pob_edad_2039", "113"); 
        data.put("pob_edad_2039h", "114");
        data.put("pob_edad_2039m", "115");
        
        data.put("pob_edad_4059", "116"); 
        data.put("pob_edad_4059h", "117");
        data.put("pob_edad_4059m", "118");
        
        data.put("pob_edad_60mas", "119"); 
        data.put("pob_edad_60mash", "120");
        data.put("pob_edad_60masm", "121");
        
        data.put("pob_total18amast", "122"); 
        data.put("pob_total18amash", "123");
        data.put("pob_total18amasm", "124");
        
        data.put("pob_m1549t", "125");
        
        data.put("pob_vivpartt", "126"); 
        data.put("pob_vivparth", "127");
        data.put("pob_vivpartm", "128");
        
        data.put("pob_vivcolectt", "129"); 
        data.put("pob_vivcolecth", "130");
        data.put("pob_vivcolectm", "131");
        
        data.put("pob_vivctranst", "132"); 
        data.put("pob_vivctransth", "133");
        data.put("pob_vivctranstm", "134");
        
        data.put("pob_vivcallet", "135"); 
        data.put("pob_vivcalleh", "136");
        data.put("pob_vivcallem", "137");
        
        data.put("pob_inscregcivt", "138"); 
        data.put("pob_inscregcivh", "139");
        data.put("pob_inscregcivm", "140");
        
        data.put("pob_tienecarnett", "141"); 
        data.put("pob_tienecarneth", "142");
        data.put("pob_tienecarnetm", "143");
        
        // POBLACIÓN EMPADRONADA POR SEXO, SEGÚN IDIOMA EN EL QUE APRENDIO A HABLAR
        
        data.put("idioma_ninez_ttotal", "144"); 
        data.put("idioma_ninez_htotalh", "145");
        data.put("idioma_ninez_mtotalm", "146");
        
        data.put("idioma_ninez_ttotal", "147"); 
        data.put("idioma_ninez_htotalh", "148");
        data.put("idioma_ninez_mtotalm", "149");
        
        data.put("idioma_ninez_tcastellano", "150"); 
        data.put("idioma_ninez_hcastellanoh", "151");
        data.put("idioma_ninez_mcastellanom", "152");
        
        data.put("idioma_ninez_tquechua", "153"); 
        data.put("idioma_ninez_hquechua", "154");
        data.put("idioma_ninez_mquechua", "155");
        
        data.put("idioma_ninez_taymara", "156"); 
        data.put("idioma_ninez_haymarah", "157");
        data.put("idioma_ninez_maymaram", "158");
        
        data.put("idioma_ninez_tguarani", "159"); 
        data.put("idioma_ninez_hguarani", "160");
        data.put("idioma_ninez_mguarani", "161");
        
        data.put("idioma_ninez_toficiales", "162"); 
        data.put("idioma_ninez_hoficiales", "163");
        data.put("idioma_ninez_moficiales", "164");
        
        data.put("idioma_ninez_totros", "165"); 
        data.put("idioma_ninez_hotrosh", "166");
        data.put("idioma_ninez_motrosm", "167");
        
        data.put("idioma_ninez_textranjero", "168"); 
        data.put("idioma_ninez_hextranjero", "169");
        data.put("idioma_ninez_mextranjero", "170");
        
        data.put("idioma_ninez_tnohabla", "171"); 
        data.put("idioma_ninez_hnohablah", "172");
        data.put("idioma_ninez_mnohablam", "173");
        
        data.put("idioma_ninez_tsinespecificar", "174"); 
        data.put("idioma_ninez_hsinespecificarh", "175");
        data.put("idioma_ninez_msinespecificarm", "176");
        
        // POBLACIÓN EMPADRONADA DE 6 A 19 AÑOS POR SEXO, SEGÚN ASISTENCIA ESCOLAR
        
        data.put("asist_escolart", "177"); 
        data.put("asist_escolarh", "178");
        data.put("asist_escolarm", "179");
        
        data.put("asist_asistet", "180"); 
        data.put("asist_asisteh", "181");
        data.put("asist_asistem", "182");
        
        data.put("asist_noasistet", "183"); 
        data.put("asist_noasisteh", "184");
        data.put("asist_noasistem", "185");
        
        data.put("asist_sinespecificart", "186"); 
        data.put("asist_sinespecificarh", "187");
        data.put("asist_sinespecificarm", "188");
        
        // LUGAR DONDE ACUDE LA POBLACIÓN CUANDO TIENEN PROBLEMAS DE SALUD
        
        data.put("salud_cajat", "189"); 
        data.put("salud_cajah", "190");
        data.put("salud_cajam", "191");
        
        data.put("salud_seguro_privadot", "192"); 
        data.put("salud_seguro_privadoh", "193");
        data.put("salud_seguro_privadom", "194");
        
        data.put("salud_seguro_publicot", "195"); 
        data.put("salud_seguro_publicoh", "196");
        data.put("salud_seguro_publicom", "197");
        
        data.put("salud_privadot", "198"); 
        data.put("salud_privadoh", "199");
        data.put("salud_privadom", "200");
        
        data.put("salud_medio_tradicionalt", "201"); 
        data.put("salud_medio_tradicionalh", "202");
        data.put("salud_medio_tradicionalm", "203");
        
        data.put("salud_solcaserast", "204"); 
        data.put("salud_solcaserash", "205");
        data.put("salud_solcaserasm", "206");
        
        data.put("salud_farmaciat", "207"); 
        data.put("salud_farmaciah", "208");
        data.put("salud_farmaciam", "209");
        
        // POBLACIÓN EMPADRONADA, POR SEXO, SEGÚN LUGAR DE NACIMIENTO Y RESIDENCIA HABITUAL
        
        data.put("lugar_nacimientot", "210"); 
        data.put("lugar_nacimientoh", "211");
        data.put("lugar_nacimientom", "212");
        
        data.put("lugar_nacimiento_aquit", "213"); 
        data.put("lugar_nacimiento_aquih", "214");
        data.put("lugar_nacimiento_aquim", "215");
        
        data.put("lugar_nacimiento_otrolugart", "216"); 
        data.put("lugar_nacimiento_otrolugarh", "217");
        data.put("lugar_nacimiento_otrolugarm", "218");
        
        data.put("lugar_nacimiento_exteriort", "219"); 
        data.put("lugar_nacimiento_exteriorh", "220");
        data.put("lugar_nacimiento_exteriorm", "221");
        
        data.put("lugar_recidenciat", "222"); 
        data.put("lugar_recidenciah", "223");
        data.put("lugar_recidenciam", "224");
        
        data.put("lugar_recidencia_aquit", "225"); 
        data.put("lugar_recidencia_aquih", "226");
        data.put("lugar_recidencia_aquim", "227");
        
        data.put("lugar_recidencia_otrolugart", "228"); 
        data.put("lugar_recidencia_otrolugarh", "229");
        data.put("lugar_recidencia_otrolugarm", "300");
        
        data.put("lugar_recidencia_exteriort", "301"); 
        data.put("lugar_recidencia_exteriorh", "302");
        data.put("lugar_recidencia_exteriorm", "303");
        
        // POBLACIÓN EMPADRONADA DE 10 AÑOS O MÁS DE EDAD, SEGÚN ACTIVIDAD ECONÓMICA Y CATEGORIA OCUPACIONAL
        
        data.put("actividad_total", "304"); 
        data.put("actividad_totalh", "305");
        data.put("actividad_totalm", "306");
        
        data.put("actividad_agricultura", "307"); 
        data.put("actividad_agriculturah", "308");
        data.put("actividad_agriculturam", "309");
        
        data.put("actividad_mineria", "310"); 
        data.put("actividad_mineriah", "311");
        data.put("actividad_mineriam", "312");
        
        data.put("actividad_industria", "313"); 
        data.put("actividad_industriah", "314");
        data.put("actividad_industriam", "315");
        
        data.put("actividad_electricidad", "316"); 
        data.put("actividad_electricidadh", "317");
        data.put("actividad_electricidadm", "318");
        
        data.put("actividad_construccion", "319"); 
        data.put("actividad_construccionh", "320");
        data.put("actividad_construccionm", "321");
        
        data.put("actividad_comercio", "322"); 
        data.put("actividad_comercioh", "323");
        data.put("actividad_comerciom", "324");
        
        data.put("actividad_otrosservicios", "325"); 
        data.put("actividad_otrosserviciosh", "326");
        data.put("actividad_otrosserviciosm", "327");
        
        data.put("actividad_sinespecificar", "328"); 
        data.put("actividad_sinespecificarh", "329");
        data.put("actividad_sinespecificarm", "330");
        
        data.put("actividad_descripsionincompleta", "331"); 
        data.put("actividad_descripsionincompletah", "332");
        data.put("actividad_descripsionincompletam", "333");
        
        // Categoria Ocupacional
        
        data.put("ocupacional_totalt", "334"); 
        data.put("ocupacional_totalh", "335");
        data.put("ocupacional_totalm", "336");
        
        data.put("ocupacional_obrerot", "336"); 
        data.put("ocupacional_obreroh", "337");
        data.put("ocupacional_obrerom", "338");
        
        data.put("ocupacional_hogart", "339"); 
        data.put("ocupacional_hogarh", "340");
        data.put("ocupacional_hogarm", "341");
        
        data.put("ocupacional_cuentapropiat", "342"); 
        data.put("ocupacional_cuentapropiah", "343");
        data.put("ocupacional_cuentapropiam", "344");
        
        data.put("ocupacional_sociot", "345"); 
        data.put("ocupacional_socioh", "346");
        data.put("ocupacional_sociom", "347");
        
        data.put("ocupacional_familiart", "348"); 
        data.put("ocupacional_familiarh", "349");
        data.put("ocupacional_familiarm", "350");
        
        data.put("ocupacional_cooperativistat", "351"); 
        data.put("ocupacional_cooperativistah", "352");
        data.put("ocupacional_cooperativistam", "353");
        
        data.put("ocupacional_sinespecificart", "354"); 
        data.put("ocupacional_sinespecificarh", "355");
        data.put("ocupacional_sinespecificarm", "356");
        
        // VIVIENDA
        
        // Viviendas
        data.put("total_viv", "357");   // Sumatoria
        data.put("viv_vivpart", "358");
        data.put("viv_vivcolec", "359");
        
        // Disponibilidad de energía eléctrica
        data.put("total_energia", "360");   // Sumatoria
        data.put("viv_sb_enrg_red", "361");
        data.put("viv_sb_enrg_otrfuente", "362");
        data.put("viv_sb_enrg_notiene", "363");
        
        // Combustible o energía más utilizado para cocinar
        data.put("total_combustible", "364");   // Sumatoria
        data.put("viv_sb_comb_gasgarraf", "365");
        data.put("viv_sb_comb_caneria", "366");
        data.put("viv_sb_comb_lenia", "367");
        data.put("viv_sb_comb_otros", "368");
        
        // Procedencia del agua que utilizan en la vivienda
        data.put("total_sb_agua", "369");   // Sumatoria
        data.put("viv_sb_agua_red", "370"); 
        data.put("viv_sb_agua_ppublica", "371");
        data.put("viv_sb_agua_carro", "372");
        data.put("viv_sb_agua_pozo", "373");
        data.put("viv_sb_agua_lluvia", "374");
        data.put("viv_sb_agua_otros", "375");
        
        // Desague del servicio sanitario
        data.put("total_desague", "376");   // Sumatoria
        data.put("viv_sb_desgu_alcant", "377"); 
        data.put("viv_sb_desgu_camsept", "378");
        data.put("viv_sb_desgu_pozociego", "379");
        data.put("viv_sb_desgu_calle", "380");
        data.put("viv_sb_desgu_quebrada", "381");
        data.put("viv_sb_desgu_lago", "382");
        
        // Eliminación de la basura
        data.put("total_basura", "383");   // Sumatoria
        data.put("viv_basura_contened", "384"); 
        data.put("viv_basura_carro", "385");
        data.put("viv_basura_baldio", "386");
        data.put("viv_basura_rio", "387");
        data.put("viv_basura_queman", "388");
        data.put("viv_basura_entierran", "389");
        data.put("viv_basura_otros", "390");
        
        
        return data;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String inputFilePath = "C:/opt/Ficha_INE_CNA.pdf"; // Existing file
        String outputFilePath = "C:/opt/output.pdf"; // New file

        Map<String, String> data = Ficha.data();

        OutputStream fos = new FileOutputStream(new File(outputFilePath));

        PdfReader pdfReader = new PdfReader(inputFilePath);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);

        // number of pages
        System.out.println("N. pages: " + pdfReader.getNumberOfPages());

        // Ccontenido en pagina 1
        PdfContentByte pdfPage1 = pdfStamper.getOverContent(1);
        
        // DATOS INICIALES
        Ficha.setText(pdfPage1, 160, 747, data.get("departamento"));
        Ficha.setText(pdfPage1, 160, 738, data.get("provincia"));
        Ficha.setText(pdfPage1, 160, 729, data.get("municipio"));
        Ficha.setText(pdfPage1, 160, 720, data.get("municipio"));
        Ficha.setText(pdfPage1, 160, 712, data.get("num_manzano"));
        
        // POBLACION EMPADRONADA POR SEXO, SEGUN GRUPO DE EDAD
        Ficha.setText(pdfPage1, 160, 652, data.get("pob_edad_tot"));
        Ficha.setText(pdfPage1, 208, 652, data.get("pob_edad_toth"));
        Ficha.setText(pdfPage1, 257, 652, data.get("pob_edad_totm"));

        Ficha.setText(pdfPage1, 160, 640, data.get("pob_edad_0003"));
        Ficha.setText(pdfPage1, 208, 640, data.get("pob_edad_0003h"));
        Ficha.setText(pdfPage1, 257, 640, data.get("pob_edad_0003m"));
        
        Ficha.setText(pdfPage1, 160, 629, data.get("pob_edad_0405"));
        Ficha.setText(pdfPage1, 208, 629, data.get("pob_edad_0405h"));
        Ficha.setText(pdfPage1, 257, 629, data.get("pob_edad_0405m"));
        
        Ficha.setText(pdfPage1, 160, 617, data.get("pob_edad_0619"));
        Ficha.setText(pdfPage1, 208, 617, data.get("pob_edad_0619h"));
        Ficha.setText(pdfPage1, 257, 617, data.get("pob_edad_0619m"));
        
        Ficha.setText(pdfPage1, 160, 605, data.get("pob_edad_2039"));
        Ficha.setText(pdfPage1, 208, 605, data.get("pob_edad_2039h"));
        Ficha.setText(pdfPage1, 257, 605, data.get("pob_edad_2039m"));
        
        Ficha.setText(pdfPage1, 160, 593, data.get("pob_edad_4059"));
        Ficha.setText(pdfPage1, 208, 593, data.get("pob_edad_4059h"));
        Ficha.setText(pdfPage1, 257, 593, data.get("pob_edad_4059m"));
        
        Ficha.setText(pdfPage1, 160, 581, data.get("pob_edad_60mas"));
        Ficha.setText(pdfPage1, 208, 581, data.get("pob_edad_60mash"));
        Ficha.setText(pdfPage1, 257, 581, data.get("pob_edad_60masm"));
        
        Ficha.setText(pdfPage1, 160, 552, data.get("pob_total18amast"));
        Ficha.setText(pdfPage1, 208, 552, data.get("pob_total18amash"));
        Ficha.setText(pdfPage1, 257, 552, data.get("pob_total18amasm"));
        
        Ficha.setText(pdfPage1, 160, 532, data.get("pob_m1549t"));
        
        Ficha.setText(pdfPage1, 160, 500, data.get("pob_vivpartt"));
        Ficha.setText(pdfPage1, 208, 500, data.get("pob_vivparth"));
        Ficha.setText(pdfPage1, 257, 500, data.get("pob_vivpartm"));
        
        Ficha.setText(pdfPage1, 160, 480, data.get("pob_vivcolectt"));
        Ficha.setText(pdfPage1, 208, 480, data.get("pob_vivcolecth"));
        Ficha.setText(pdfPage1, 257, 480, data.get("pob_vivcolectm"));
        
        Ficha.setText(pdfPage1, 160, 460, data.get("pob_vivctranst"));
        Ficha.setText(pdfPage1, 208, 460, data.get("pob_vivctransth"));
        Ficha.setText(pdfPage1, 257, 460, data.get("pob_vivctranstm"));
        
        Ficha.setText(pdfPage1, 160, 440, data.get("pob_vivcallet"));
        Ficha.setText(pdfPage1, 208, 440, data.get("pob_vivcalleh"));
        Ficha.setText(pdfPage1, 257, 440, data.get("pob_vivcallem"));
        
        Ficha.setText(pdfPage1, 160, 406, data.get("pob_inscregcivt"));
        Ficha.setText(pdfPage1, 208, 406, data.get("pob_inscregcivh"));
        Ficha.setText(pdfPage1, 257, 406, data.get("pob_inscregcivm"));
        
        Ficha.setText(pdfPage1, 160, 386, data.get("pob_tienecarnett"));
        Ficha.setText(pdfPage1, 208, 386, data.get("pob_tienecarneth"));
        Ficha.setText(pdfPage1, 257, 386, data.get("pob_tienecarnetm"));
        
        // POBLACIÓN EMPADRONADA POR SEXO, SEGÚN IDIOMA EN EL QUE APRENDIÓ A HABLAR
        
        Ficha.setText(pdfPage1, 160, 321, data.get("idioma_ninez_ttotal"));
        Ficha.setText(pdfPage1, 208, 321, data.get("idioma_ninez_htotalh"));
        Ficha.setText(pdfPage1, 257, 321, data.get("idioma_ninez_mtotalm"));
        
        Ficha.setText(pdfPage1, 160, 309, data.get("idioma_ninez_tcastellano"));
        Ficha.setText(pdfPage1, 208, 309, data.get("idioma_ninez_hcastellanoh"));
        Ficha.setText(pdfPage1, 257, 309, data.get("idioma_ninez_mcastellanom"));
        
        Ficha.setText(pdfPage1, 160, 297, data.get("idioma_ninez_tquechua"));
        Ficha.setText(pdfPage1, 208, 297, data.get("idioma_ninez_hquechua"));
        Ficha.setText(pdfPage1, 257, 297, data.get("idioma_ninez_mquechua"));
        
        Ficha.setText(pdfPage1, 160, 285, data.get("idioma_ninez_taymara"));
        Ficha.setText(pdfPage1, 208, 285, data.get("idioma_ninez_haymarah"));
        Ficha.setText(pdfPage1, 257, 285, data.get("idioma_ninez_maymaram"));
        
        Ficha.setText(pdfPage1, 160, 273, data.get("idioma_ninez_tguarani"));
        Ficha.setText(pdfPage1, 208, 273, data.get("idioma_ninez_hguarani"));
        Ficha.setText(pdfPage1, 257, 273, data.get("idioma_ninez_mguarani"));
        
        Ficha.setText(pdfPage1, 160, 262, data.get("idioma_ninez_toficiales"));
        Ficha.setText(pdfPage1, 208, 262, data.get("idioma_ninez_hoficiales"));
        Ficha.setText(pdfPage1, 257, 262, data.get("idioma_ninez_moficiales"));
        
        Ficha.setText(pdfPage1, 160, 250, data.get("idioma_ninez_totros"));
        Ficha.setText(pdfPage1, 208, 250, data.get("idioma_ninez_hotrosh"));
        Ficha.setText(pdfPage1, 257, 250, data.get("idioma_ninez_motrosm"));
        
        Ficha.setText(pdfPage1, 160, 238, data.get("idioma_ninez_textranjero"));
        Ficha.setText(pdfPage1, 208, 238, data.get("idioma_ninez_hextranjero"));
        Ficha.setText(pdfPage1, 257, 238, data.get("idioma_ninez_mextranjero"));
        
        Ficha.setText(pdfPage1, 160, 226, data.get("idioma_ninez_tnohabla"));
        Ficha.setText(pdfPage1, 208, 226, data.get("idioma_ninez_hnohablah"));
        Ficha.setText(pdfPage1, 257, 226, data.get("idioma_ninez_mnohablam"));
        
        Ficha.setText(pdfPage1, 160, 214, data.get("idioma_ninez_tsinespecificar"));
        Ficha.setText(pdfPage1, 208, 214, data.get("idioma_ninez_hsinespecificarh"));
        Ficha.setText(pdfPage1, 257, 214, data.get("idioma_ninez_msinespecificarm"));
        
        // POBLACIÓN EMPADRONADA DE 6 A 19 AÑOS POR SEXO, SEGÚN ASISTENCIA ESCOLAR
        
        Ficha.setText(pdfPage1, 160, 170, data.get("asist_escolart"));
        Ficha.setText(pdfPage1, 208, 170, data.get("asist_escolarh"));
        Ficha.setText(pdfPage1, 257, 170, data.get("asist_escolarm"));
        
        Ficha.setText(pdfPage1, 160, 158, data.get("asist_asistet"));
        Ficha.setText(pdfPage1, 208, 158, data.get("asist_asisteh"));
        Ficha.setText(pdfPage1, 257, 158, data.get("asist_asistem"));
        
        Ficha.setText(pdfPage1, 160, 147, data.get("asist_noasistet"));
        Ficha.setText(pdfPage1, 208, 147, data.get("asist_noasisteh"));
        Ficha.setText(pdfPage1, 257, 147, data.get("asist_noasistem"));
        
        Ficha.setText(pdfPage1, 160, 135, data.get("asist_sinespecificart"));
        Ficha.setText(pdfPage1, 208, 135, data.get("asist_sinespecificarh"));
        Ficha.setText(pdfPage1, 257, 135, data.get("asist_sinespecificarm"));
        
        // LUGAR DONDE ACUDE LA POBLACIÓN CUANDO TIENEN PROBLEMAS DE SALUD
        
        Ficha.setText(pdfPage1, 415, 648, data.get("salud_cajat"));
        Ficha.setText(pdfPage1, 463, 648, data.get("salud_cajah"));
        Ficha.setText(pdfPage1, 512, 648, data.get("salud_cajam"));
        
        Ficha.setText(pdfPage1, 415, 632, data.get("salud_seguro_privadot"));
        Ficha.setText(pdfPage1, 463, 632, data.get("salud_seguro_privadoh"));
        Ficha.setText(pdfPage1, 512, 632, data.get("salud_seguro_privadom"));
        
        Ficha.setText(pdfPage1, 415, 616, data.get("salud_seguro_publicot"));
        Ficha.setText(pdfPage1, 463, 616, data.get("salud_seguro_publicoh"));
        Ficha.setText(pdfPage1, 512, 616, data.get("salud_seguro_publicom"));
        
        Ficha.setText(pdfPage1, 415, 595, data.get("salud_privadot"));
        Ficha.setText(pdfPage1, 463, 595, data.get("salud_privadoh"));
        Ficha.setText(pdfPage1, 512, 595, data.get("salud_privadom"));
        
        Ficha.setText(pdfPage1, 415, 579, data.get("salud_medio_tradicionalt"));
        Ficha.setText(pdfPage1, 463, 579, data.get("salud_medio_tradicionalh"));
        Ficha.setText(pdfPage1, 512, 579, data.get("salud_medio_tradicionalm"));
        
        Ficha.setText(pdfPage1, 415, 567, data.get("salud_solcaserast"));
        Ficha.setText(pdfPage1, 463, 567, data.get("salud_solcaserash"));
        Ficha.setText(pdfPage1, 512, 567, data.get("salud_solcaserasm"));
        
        Ficha.setText(pdfPage1, 415, 555, data.get("salud_farmaciat"));
        Ficha.setText(pdfPage1, 463, 555, data.get("salud_farmaciah"));
        Ficha.setText(pdfPage1, 512, 555, data.get("salud_farmaciam"));
        
        // POBLACIÓN EMPADRONADA, POR SEXO, SEGÚN LUGAR DE NACIMIENTO Y RESIDENCIA HABITUAL
        
        Ficha.setText(pdfPage1, 415, 511, data.get("lugar_nacimientot"));
        Ficha.setText(pdfPage1, 463, 511, data.get("lugar_nacimientoh"));
        Ficha.setText(pdfPage1, 512, 511, data.get("lugar_nacimientom"));
        
        Ficha.setText(pdfPage1, 415, 499, data.get("lugar_nacimiento_aquit"));
        Ficha.setText(pdfPage1, 463, 499, data.get("lugar_nacimiento_aquih"));
        Ficha.setText(pdfPage1, 512, 499, data.get("lugar_nacimiento_aquim"));
        
        Ficha.setText(pdfPage1, 415, 488, data.get("lugar_nacimiento_otrolugart"));
        Ficha.setText(pdfPage1, 463, 488, data.get("lugar_nacimiento_otrolugarh"));
        Ficha.setText(pdfPage1, 512, 488, data.get("lugar_nacimiento_otrolugarm"));
        
        Ficha.setText(pdfPage1, 415, 476, data.get("lugar_nacimiento_exteriort"));
        Ficha.setText(pdfPage1, 463, 476, data.get("lugar_nacimiento_exteriorh"));
        Ficha.setText(pdfPage1, 512, 476, data.get("lugar_nacimiento_exteriorm"));
        
        Ficha.setText(pdfPage1, 415, 441, data.get("lugar_recidenciat"));
        Ficha.setText(pdfPage1, 463, 441, data.get("lugar_recidenciah"));
        Ficha.setText(pdfPage1, 512, 441, data.get("lugar_recidenciam"));
        
        Ficha.setText(pdfPage1, 415, 429, data.get("lugar_recidencia_aquit"));
        Ficha.setText(pdfPage1, 463, 429, data.get("lugar_recidencia_aquih"));
        Ficha.setText(pdfPage1, 512, 429, data.get("lugar_recidencia_aquim"));
        
        Ficha.setText(pdfPage1, 415, 417, data.get("lugar_recidencia_otrolugart"));
        Ficha.setText(pdfPage1, 463, 417, data.get("lugar_recidencia_otrolugarh"));
        Ficha.setText(pdfPage1, 512, 417, data.get("lugar_recidencia_otrolugarm"));
        
        Ficha.setText(pdfPage1, 415, 405, data.get("lugar_recidencia_exteriort"));
        Ficha.setText(pdfPage1, 463, 405, data.get("lugar_recidencia_exteriorh"));
        Ficha.setText(pdfPage1, 512, 405, data.get("lugar_recidencia_exteriorm"));
        
        // POBLACIÓN EMPADRONADA DE 10 AÑOS O MÁS DE EDAD, SEGÚN ACTIVIDAD ECONÓMICA Y CATEGORIA OCUPACIONAL
        
        Ficha.setText(pdfPage1, 415, 361, data.get("actividad_total"));
        Ficha.setText(pdfPage1, 463, 361, data.get("actividad_totalh"));
        Ficha.setText(pdfPage1, 512, 361, data.get("actividad_totalm"));
        
        Ficha.setText(pdfPage1, 415, 345, data.get("actividad_agricultura"));
        Ficha.setText(pdfPage1, 463, 345, data.get("actividad_agriculturah"));
        Ficha.setText(pdfPage1, 512, 345, data.get("actividad_agriculturam"));
        
        Ficha.setText(pdfPage1, 415, 329, data.get("actividad_mineria"));
        Ficha.setText(pdfPage1, 463, 329, data.get("actividad_mineriah"));
        Ficha.setText(pdfPage1, 512, 329, data.get("actividad_mineriam"));
        
        Ficha.setText(pdfPage1, 415, 317, data.get("actividad_industria"));
        Ficha.setText(pdfPage1, 463, 317, data.get("actividad_industriah"));
        Ficha.setText(pdfPage1, 512, 317, data.get("actividad_industriam"));
        
        Ficha.setText(pdfPage1, 415, 301, data.get("actividad_electricidad"));
        Ficha.setText(pdfPage1, 463, 301, data.get("actividad_electricidadh"));
        Ficha.setText(pdfPage1, 512, 301, data.get("actividad_electricidadm"));
        
        Ficha.setText(pdfPage1, 415, 284, data.get("actividad_construccion"));
        Ficha.setText(pdfPage1, 463, 284, data.get("actividad_construccionh"));
        Ficha.setText(pdfPage1, 512, 284, data.get("actividad_construccionm"));
        
        Ficha.setText(pdfPage1, 415, 268, data.get("actividad_comercio"));
        Ficha.setText(pdfPage1, 463, 268, data.get("actividad_comercioh"));
        Ficha.setText(pdfPage1, 512, 268, data.get("actividad_comerciom"));
        
        Ficha.setText(pdfPage1, 415, 252, data.get("actividad_otrosservicios"));
        Ficha.setText(pdfPage1, 463, 252, data.get("actividad_otrosserviciosh"));
        Ficha.setText(pdfPage1, 512, 252, data.get("actividad_otrosserviciosm"));
        
        Ficha.setText(pdfPage1, 415, 240, data.get("actividad_sinespecificar"));
        Ficha.setText(pdfPage1, 463, 240, data.get("actividad_sinespecificarh"));
        Ficha.setText(pdfPage1, 512, 240, data.get("actividad_sinespecificarm"));
        
        Ficha.setText(pdfPage1, 415, 228, data.get("actividad_descripsionincompleta"));
        Ficha.setText(pdfPage1, 463, 228, data.get("actividad_descripsionincompletah"));
        Ficha.setText(pdfPage1, 512, 228, data.get("actividad_descripsionincompletam"));
        
        // Categoría ocupacional
        
        Ficha.setText(pdfPage1, 415, 205, data.get("ocupacional_totalt"));
        Ficha.setText(pdfPage1, 463, 205, data.get("ocupacional_totalh"));
        Ficha.setText(pdfPage1, 512, 205, data.get("ocupacional_totalm"));
        
        Ficha.setText(pdfPage1, 415, 193, data.get("ocupacional_obrerot"));
        Ficha.setText(pdfPage1, 463, 193, data.get("ocupacional_obreroh"));
        Ficha.setText(pdfPage1, 512, 193, data.get("ocupacional_obrerom"));
        
        Ficha.setText(pdfPage1, 415, 181, data.get("ocupacional_hogart"));
        Ficha.setText(pdfPage1, 463, 181, data.get("ocupacional_hogarh"));
        Ficha.setText(pdfPage1, 512, 181, data.get("ocupacional_hogarm"));
        
        Ficha.setText(pdfPage1, 415, 165, data.get("ocupacional_cuentapropiat"));
        Ficha.setText(pdfPage1, 463, 165, data.get("ocupacional_cuentapropiah"));
        Ficha.setText(pdfPage1, 512, 165, data.get("ocupacional_cuentapropiam"));
        
        Ficha.setText(pdfPage1, 415, 149, data.get("ocupacional_sociot"));
        Ficha.setText(pdfPage1, 463, 149, data.get("ocupacional_socioh"));
        Ficha.setText(pdfPage1, 512, 149, data.get("ocupacional_sociom"));
        
        Ficha.setText(pdfPage1, 415, 133, data.get("ocupacional_familiart"));
        Ficha.setText(pdfPage1, 463, 133, data.get("ocupacional_familiarh"));
        Ficha.setText(pdfPage1, 512, 133, data.get("ocupacional_familiarm"));
        
        Ficha.setText(pdfPage1, 415, 112, data.get("ocupacional_cooperativistat"));
        Ficha.setText(pdfPage1, 463, 112, data.get("ocupacional_cooperativistah"));
        Ficha.setText(pdfPage1, 512, 112, data.get("ocupacional_cooperativistam"));
        
        Ficha.setText(pdfPage1, 415, 96, data.get("ocupacional_sinespecificart"));
        Ficha.setText(pdfPage1, 463, 96, data.get("ocupacional_sinespecificarh"));
        Ficha.setText(pdfPage1, 512, 96, data.get("ocupacional_sinespecificarm"));
        
        
        // Ccontenido en pagina 2
        PdfContentByte pdfPage2 = pdfStamper.getOverContent(2);
        
        // Viviendas
        Ficha.setText(pdfPage2, 250, 699, data.get("total_viv"));
        Ficha.setText(pdfPage2, 250, 687, data.get("viv_vivpart"));
        Ficha.setText(pdfPage2, 250, 675, data.get("viv_vivcolec"));
        
        // Disponibilidad de energía eléctrica
        Ficha.setText(pdfPage2, 250, 652, data.get("total_energia"));
        Ficha.setText(pdfPage2, 250, 640, data.get("viv_sb_enrg_red"));
        Ficha.setText(pdfPage2, 250, 628, data.get("viv_sb_enrg_otrfuente"));
        Ficha.setText(pdfPage2, 250, 614, data.get("viv_sb_enrg_notiene"));
        
        // Combustible o energía más utilizado para cocinar
        Ficha.setText(pdfPage2, 250, 592, data.get("total_combustible"));
        Ficha.setText(pdfPage2, 250, 580, data.get("viv_sb_comb_gasgarraf"));
        Ficha.setText(pdfPage2, 250, 569, data.get("viv_sb_comb_caneria"));
        Ficha.setText(pdfPage2, 250, 557, data.get("viv_sb_comb_lenia"));
        Ficha.setText(pdfPage2, 250, 545, data.get("viv_sb_comb_otros"));
        
        // Procedencia del agua que utilizan en la vivienda
        Ficha.setText(pdfPage2, 250, 521, data.get("total_sb_agua"));
        Ficha.setText(pdfPage2, 250, 509, data.get("viv_sb_agua_red"));
        Ficha.setText(pdfPage2, 250, 498, data.get("viv_sb_agua_ppublica"));
        Ficha.setText(pdfPage2, 250, 486, data.get("viv_sb_agua_carro"));
        Ficha.setText(pdfPage2, 250, 474, data.get("viv_sb_agua_pozo"));
        Ficha.setText(pdfPage2, 250, 462, data.get("viv_sb_agua_lluvia"));
        Ficha.setText(pdfPage2, 250, 450, data.get("viv_sb_agua_otros"));
        
        // Desague del servicio sanitario
        Ficha.setText(pdfPage2, 475, 702, data.get("total_desague"));
        Ficha.setText(pdfPage2, 475, 690, data.get("viv_sb_desgu_alcant"));
        Ficha.setText(pdfPage2, 475, 678, data.get("viv_sb_desgu_camsept"));
        Ficha.setText(pdfPage2, 475, 667, data.get("viv_sb_desgu_pozociego"));
        Ficha.setText(pdfPage2, 475, 655, data.get("viv_sb_desgu_calle"));
        Ficha.setText(pdfPage2, 475, 643, data.get("viv_sb_desgu_quebrada"));
        Ficha.setText(pdfPage2, 475, 631, data.get("viv_sb_desgu_lago"));
        
        // Eliminación de la basura
        Ficha.setText(pdfPage2, 475, 595, data.get("total_basura"));
        Ficha.setText(pdfPage2, 475, 580, data.get("viv_basura_contened"));
        Ficha.setText(pdfPage2, 475, 559, data.get("viv_basura_carro"));
        Ficha.setText(pdfPage2, 475, 539, data.get("viv_basura_baldio"));
        Ficha.setText(pdfPage2, 475, 523, data.get("viv_basura_rio"));
        Ficha.setText(pdfPage2, 475, 511, data.get("viv_basura_queman"));
        Ficha.setText(pdfPage2, 475, 499, data.get("viv_basura_entierran"));
        Ficha.setText(pdfPage2, 475, 487, data.get("viv_basura_otros"));
        
        
        pdfStamper.close(); // close pdfStamper
        System.out.println("Modified PDF created in >> " + outputFilePath);
    }

}
