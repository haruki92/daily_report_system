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
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>従業員　一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                   <tr class="row${status.count % 2}">
                       <td><c:out value="${employee.code}" /><br>
                       	<c:out value="${employee.isFollow}"></c:out>
                       </td>
                       <td><c:out value="${employee.name}" /></td>
                        <td>
                       		<!-- ログイン中の従業員ではない時にのみ操作表示 -->
                       		<c:if test="${sessionScope.login_employee.id != employee.id}">
                        		<c:choose>
                        			<c:when test="${employee.deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}">
                        				(削除済)
                        			</c:when>
                        			<c:otherwise>
	                        			<c:choose>
	                        				<c:when test="${employee.isFollow == true}">
	                        					<form method="POST" action="?action=${actFol}&command=${commDes}&id=${employee.id}">
													<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
	                        						<button id="unfollow">フォロー解除する</button>
	                        					</form>
	                        				</c:when>
	                        				<c:otherwise>
				                        		<form method="POST" action="?action=${actFol}&command=${commFol}&id=${employee.id}">
													<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
	                        						<button id="follow">フォローする</button>
				                            	</form>
	                        				</c:otherwise>
	                        			</c:choose>
                        			</c:otherwise>
                        		</c:choose>
                       		</c:if>
                        </td>
                 	 </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${employees_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((employees_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actFol}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actTop}&command=${commIdx}' />">トップへ戻る</a></p>

    </c:param>
</c:import>