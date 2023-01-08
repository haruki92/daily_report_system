<%@page import="java.util.List"%>
<%@page import="actions.views.FollowView"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}"></c:set>
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}"></c:set>
<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}"></c:set>
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}"></c:set>
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}"></c:set>
<c:set var="commFol" value="${ForwardConst.CMD_FOLLOW.getValue()}"></c:set>
<c:set var="commDes" value="${ForwardConst.CMD_DESTROY.getValue()}"></c:set>


<c:import url="../layout/app.jsp">
    <c:param name="content">
    <h2>フォロー中の従業員　一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                </tr>
                <c:forEach var="employee" items="${follows}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.follow_id.code}" /></td>
                        <td><c:out value="${employee.follow_id.name}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${sessionScope.follows_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((sessionScope.follows_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actEmp}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <br><br>
    
    <h2>フォロー中の従業員の日報一覧</h2>
    <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${follow_reports}" varStatus="status">
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='?action=${actFol}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${follow_reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((follow_reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actRep}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p>
            <a href="<c:url value='?action=${actTop}&command=${commIdx}' />">トップに戻る</a>
        </p>
    </c:param>
</c:import>