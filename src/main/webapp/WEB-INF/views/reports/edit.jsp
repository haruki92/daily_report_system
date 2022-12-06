<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}"></c:set>
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}"></c:set>

<c:import url="/WEB-INF/views/layout/app.jsp">
	<c:param name="content">
		<h2>日報　編集ページ</h2>
		<form method="POST" action="<c:url value='?action=${actRep}&command=${commUpd}'></c:url>">
			<c:import url="_form.jsp"></c:import>
		</form>
		
		<p>
			<a href="<c:url value='?action=Report&command=index'></c:url>">一覧に戻る</a>
		</p>
	</c:param>
</c:import>