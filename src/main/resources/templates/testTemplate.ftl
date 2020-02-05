<html>
<body>
<table>
<tr>
<th>ID</th>
<th>NAME</th>
<th>DEPARTMENT</th>
<th>EMAIL</th>
<th>MOBILE</th>
</tr>
<#if data??>
	<#list data as item>
		<tr>
    	<td>${item.id}</td>
    	<td>${item.name}</td>
    	<td>${item.department}</td>
    	<td>${item.email}</td>
    	<td>${item.mobile}</td>
    	</tr>
	</#list>
</#if>
<tr>
</tr>
</table>

</body>
</html>