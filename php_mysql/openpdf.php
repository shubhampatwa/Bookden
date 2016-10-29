<!DOCTYPE html>
<html>
<head>
	<title></title>
</head>
<body>
<?php 
	if(isset($_GET['link']))
	{
		//echo "hello";
		echo "<iframe src=http://docs.google.com/gview?url=http://10.0.0.5/bookden/".$_GET['link']."&embedded=true style=width:718px; height:700px; frameborder=0></iframe>";
	}
?>
</body>
</html>
