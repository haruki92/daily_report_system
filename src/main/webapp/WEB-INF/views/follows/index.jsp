<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}"></c:set>
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}"></c:set>
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}"></c:set>

<c:import url="/WEB-INF/views/employees/index.jsp">
</c:import>