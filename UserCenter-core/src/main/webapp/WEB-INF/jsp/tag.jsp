<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%@ page session="false" %>
<!-- project root directory -->
<c:set var="baseurl" value="${pageContext.request.contextPath}/" />

<!-- system root url -->
<script type="text/javascript">
	var baseurl = "${baseurl}";
</script>