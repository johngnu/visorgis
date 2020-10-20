<%-- 
    Document   : ExtJS Scripts View
    Created on : 01-03-2012, 02:30:51 PM
    Author     : Johns Castillo Valencia
--%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!-- ICG - S.R.L. - Internationa Consulting Group  2011 - 2012 --> 
        <!-- Control de escenario v1.0 --> 
        <script type="text/javascript">
            //var __sitmax_page = '${__sitmax_page}';
            //if(__sitmax_page == 'main'){if (top != self) {window.location='<c:url value="/"/>main?error=iframe';}  
            //}else{if(top == self){window.location='<c:url value="/"/>main?error=noiframe';}}               
        </script> 
        <!-- ALL ExtJS Framework And App resources -->
        <link rel="stylesheet" type="text/css" href="/ext-3.3.1/resources/css/ext-all.css" />
        <link rel="stylesheet" type="text/css" href="/ext-3.3.1/resources/css/xtheme-gray.css" />                    
        <script type="text/javascript" src="/ext-3.3.1/adapter/ext/ext-base.js"></script>
        <script type="text/javascript" src="/ext-3.3.1/ext-all.js"></script>        
        <script type="text/javascript" src="/ext-3.3.1/src/locale/ext-lang-es.js"></script> 
        <script type="text/javascript" src="<c:url value="/js/ui/sitmax-errors-msgs.js"/>"></script> 
        <script type="text/javascript" src="<c:url value="/js/ui/sitmax-vtypes.js"/>"></script> 
        <script type="text/javascript">    
            /**
            * Variables Globales estaticas
            * @author Johns Castillo Valencia
            */
            Ext.BLANK_IMAGE_URL = '/ext-3.3.1/resources/images/default/s.gif';    
            Ext.IMAGES_SILK = '/ext-3.3.1/resources/images/silk/icons/';
            Ext.SROOT = '<c:url value="/"/>';            
            Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
            Ext.util.CSS.swapStyleSheet('theme', '/ext-3.3.1/resources/css/' + Ext.state.Manager.get('theme'));
            Ext.QuickTips.init();
            /**
            * Principal ROOT_APP namespace
            * @author Johns Castillo Valencia
            */
            Ext.ns('com.icg');
            Ext.ns('com.icg.sitmax.struct'); 
        </script>
        <!-- ALL Application ICONS -->        
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/icons.css"/>"/>