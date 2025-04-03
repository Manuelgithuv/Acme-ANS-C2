<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.administrator.list.label.locatorCode" path="locatorCode"/>
	<acme:list-column code="authenticated.administrator.list.label.travelClass" path="travelClass"/>
	<acme:list-column code="authenticated.administrator.list.label.price" path="price"/>
	<acme:list-column code="authenticated.administrator.list.label.published" path="published"/>
	<acme:list-column code="authenticated.administrator.list.label.lastCardNibble" path="lastCardNibble"/>
</acme:list>
