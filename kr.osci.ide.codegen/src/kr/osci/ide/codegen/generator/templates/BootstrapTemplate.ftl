<html>
<body>
	<form class="form-horizontal">
		<#list model.fields as field>
		<div class="form-group">
			<label class="control-label col-sm-2">${field.column.comments}</label>
			<div class="col-sm-10">
				<input type="text" id="${field.name}" name="${field.name}" class="form-control input-sm" required />
			</div>
		</div>
		</#list>
	</form>
	<form class="form-horizontal">
		<#list model.fields as field>
    	<div class="form-group">
			<label class="control-label col-sm-2">${field.column.comments} :</label>
			<div class="col-sm-9">
				<p class="form-control-static ${field.name}"></p>
			</div>
		</div>
		</#list>
	</form>
</body>
</html>