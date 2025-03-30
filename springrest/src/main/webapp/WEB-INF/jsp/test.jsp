<!DOCTYPE html>
<html lang="en">
<head>
    <title>JSP page</title>
	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>    
        <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    
</head>
<body>

<jsp:include page="menu.jsp" />.

<p></p>
<div  class="container">
 JSP PAGE: Hello submit ${name}  and  session ${username} <br>
 
 
 
 <div>
           <form action="/jspSearch"  method="post">
	                      
	                                <label for="name" >Post by Name</label> 
	                                <input type="text" value="${name}" name="name"
	                                            id="name" placeholder="Search by name" />
	                            <button type="submit" class="btn btn-primary">Search</button><br>
	                            <input type="text" value="${sessionScope.username}"  name="username"/><br>
	                            <input size=120 name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                            
	                            </form>
</div>	                            
</div>
   
</body>
</html>