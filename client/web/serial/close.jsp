<%-- Interbot
     Copyright (C) 2013 Tuna Oezer, General AI.
     All rights reserved.
--%>

<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ai.general.interbot.web.InterbotWeb" %>
<%@ page import="ai.general.plugin.PluginManager" %>
<%@ page import="ai.general.scriptduino.Scriptduino" %>
<%@ page import="ai.general.scriptduino.ScriptduinoPlugin" %>

<%
  boolean success = false;
  boolean local_access = InterbotWeb.isLocalAddress(request.getRemoteAddr());
  if (local_access) {
    response.setContentType("application/json");
    response.setHeader("Content-Disposition", "inline");
    ScriptduinoPlugin scriptduino_plugin =
      (ScriptduinoPlugin) PluginManager.Instance.getPlugin("Scriptduino");
    if (scriptduino_plugin != null) {
      Scriptduino scriptduino = scriptduino_plugin.getScriptduino();
      scriptduino.close();
      success = true;
    }
  } else {
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }
%>

{ "success": <%= Boolean.toString(success) %> }
