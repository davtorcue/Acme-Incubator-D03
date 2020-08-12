<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">
	
	<acme:form-url code="administrator.notice.form.label.header" path="header"/>
	<acme:form-textbox code="administrator.notice.form.label.title" path="title"/>
	<acme:form-textarea code="administrator.notice.form.label.body" path="body"/>
	<acme:form-moment code="administrator.notice.form.label.creationDateTime" path="creationDateTime"/>
	<acme:form-moment code="administrator.notice.form.label.deadline" path="deadline"/>
	<acme:form-textarea code="administrator.notice.form.label.optionalLinks" path="optionalLinks"/>
	
	<acme:form-return code="administrator.notice.button.return"/>
</acme:form>