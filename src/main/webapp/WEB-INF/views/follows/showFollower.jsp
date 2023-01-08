<%@page import="java.util.List"%>
<%@page import="actions.views.FollowView"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}"></c:set>
<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}"></c:set>
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}"></c:set>
<c:set var="commFol" value="${ForwardConst.CMD_FOLLOW.getValue()}"></c:set>
<c:set var="commDes" value="${ForwardConst.CMD_DESTROY.getValue()}"></c:set>



<c:import url="../layout/app.jsp">
    <c:param name="content">
    	<h2>フォロワー　日報一覧</h2>
    </c:param>
</c:import>