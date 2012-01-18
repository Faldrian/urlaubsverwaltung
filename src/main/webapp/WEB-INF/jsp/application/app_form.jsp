<%-- 
    Document   : app_form
    Created on : 26.10.2011, 15:05:51
    Author     : Aljona Murygina
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<spring:url value='/jquery/css/ui-lightness/jquery-ui-1.8.16.custom.css' />" />
        <link rel="stylesheet" type="text/css" href="<spring:url value='/css/main.css' />" /> 
        <title><spring:message code="title" /></title>
        <script src="<spring:url value='/jquery/js/jquery-1.6.2.min.js' />" type="text/javascript" ></script>
        <script src="<spring:url value='/jquery/js/jquery-ui-1.8.16.custom.min.js' />" type="text/javascript" ></script>
        <script type="text/javascript">
	$(function() {
		var dates = $( "#from, #to, #at" ).datepicker({
                        dateFormat: "dd.mm.yy",
			defaultDate: "+1w",
			numberOfMonths: 1,
			onSelect: function( selectedDate ) {
				var option = this.id == "from" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
			}
		});
	});
	</script>
        <%-- Is there a way that this works?!
        <c:if test="${appForm.howLong != null}">
        <script type="text/javascript">
            $(document).ready(function() {
                
                var dayLength = <c:out value="${appForm.howLong}" />;
            
                if(dayLength.indexOf("FULL") != -1) {
                    $('#full-day').show(); $('#half-day').hide();
                } 
                
                if(dayLength.indexOf("MORNING") != -1) {
                    $('#half-day').show(); $('#full-day').hide();
                }
                
                if(dayLength.indexOf("NOON") != -1) {
                    $('#half-day').show(); $('#full-day').hide();
                }
            });
        </script>
        </c:if>
        --%>
    </head>
    
    <body>
        
        <spring:url var="formUrlPrefix" value="/web" />
        
        <%@include file="../include/header.jsp" %>
        
        <div id="content">
        
        <h2><spring:message code="app.title" /></h2>
        
        <br />
        
        <form:form method="post" action="${formUrlPrefix}/application/new" modelAttribute="appForm"> 
            
        <table>
            <tr>
                <td>
                    <spring:message code="name" />:&nbsp;
                </td>
                <td>
                    <c:out value="${person.lastName}" />,&nbsp;<c:out value="${person.firstName}" />
                </td> 
            </tr>
            <tr>
                <td>
                    <spring:message code="entitlement" />:
                </td>    
                <td>    
                    <c:out value="${account.vacationDays}" />&nbsp;<spring:message code="days" />
                </td>
            </tr>
            <tr>
                <td>
                  <spring:message code="remaining" />:  
                </td>
                <td>
                    <c:out value="${account.remainingVacationDays}" />&nbsp;<spring:message code="days" />
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <spring:message code="app.apply" />
                </td>
                <td>
                   <form:select path="vacationType" size="1">
                        <c:forEach items="${vacTypes}" var="vacType">
                                <option value="${vacType}">
                                    <spring:message code='${vacType.vacationTypeName}' />
                                </option>
                        </c:forEach>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">
                    <form:radiobutton path="howLong" checked="checked" value="${full}" onclick="$('#full-day').show(); $('#half-day').hide();" /><spring:message code='${full.dayLength}' /> 
                    <form:radiobutton path="howLong" value="${morning}" onclick="$('#full-day').hide(); $('#half-day').show();" /><spring:message code='${morning.dayLength}' />
                    <form:radiobutton path="howLong" value="${noon}" onclick="$('#full-day').hide(); $('#half-day').show();" /><spring:message code='${noon.dayLength}' />
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr id="full-day">  
                <td>
                    Von: <form:input id="from" path="startDate" cssErrorClass="error" />
                    <br />
                    <form:errors path="startDate" cssClass="error" />
                </td>
                <td>
                    Bis: <form:input id="to" path="endDate" cssErrorClass="error" />
                    <br />
                    <form:errors path="endDate" cssClass="error" />
                </td>
            </tr>
            <tr id="half-day" style="display: none">  
                <td>
                    Am: <form:input id="at" path="startDateHalf" cssErrorClass="error" />
                    <br />
                    <form:errors path="startDateHalf" cssClass="error" />
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <label for="reason"><spring:message code='reason' />:</label>
                </td>
                <td>
                    <form:input id="reason" path="reason" cssErrorClass="error" />
                    <br />
                    <form:errors path="reason" cssClass="error" />
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td>
                   <label for="vertreter"><spring:message code='app.rep' />:</label> 
                </td>
                <td colspan="2">
                    <form:select path="rep" id="vertreter" size="1">
                        <c:forEach items="${persons}" var="einmitarbeiter">
                                <option value="${einmitarbeiter.lastName} ${einmitarbeiter.firstName}">
                                    <c:out value='${einmitarbeiter.lastName}' />&nbsp;<c:out value="${einmitarbeiter.firstName}" />
                                </option>
                        </c:forEach>
                    </form:select>                             
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <label for="anschrift"><spring:message code='app.address' />:</label>
                </td>
                <td colspan="4">
                    <form:input id="anschrift" path="address" />
                </td>
            </tr>
            <tr>
                <td>
                    <label for="telefon"><spring:message code='app.phone' />:</label>
                </td>
                <td colspan="4">
                    <form:input id="telefon" path="phone" />
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">
                    <spring:message code='app.footer' />&nbsp;<joda:format style="M-" value="${date}"/>
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" name="<spring:message code='apply' />" value="<spring:message code='apply' />" />
                </td>
            </tr>
<%--            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2"><form:errors path="*" cssClass="error" /></td>
            </tr>--%>
            
        </table>
                
        </form:form>    
        
        </div>
        
    </body>
    
</html>