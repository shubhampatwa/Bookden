<?php
 require 'config.php';
 //$con=mysqli_connect("localhost","root","","bookden") or die(mysqli_error());

$response['detail']=array();

if(isset($_GET['email']) && isset($_GET['id'])){
	$sql="select * from books where id='".$_GET['id']."'";
	$query=mysqli_query($con,$sql);
	$title="";
	$author="";
	$publication="";
	$link="";
	//echo "shubham";
	while ($row=mysqli_fetch_array($query)) {
		# code...
		$title=$row['title'];
		$author=$row['author'];
		$link=$row['book'];
		$publication=$row['publication'];
	}
	$sql="select * from userbook where email='".$_GET['email']."'&&title='".$title."'&&author='".$author."'&&publication='".$publication."'";
	$query=mysqli_query($con,$sql);
	$exists=mysqli_num_rows($query);
	//echo "<p>$exists</p>";
	if(!$exists){
	$sql="INSERT into userbook(id,email,title,author,publication,link) values(NULL,'".$_GET['email']."','".$title."','".$author."','".$publication."','".$link."')";
	//echo "$sql";
	mysqli_query($con,$sql);
	}
}
else if (isset($_GET['email'])) {
	# code...
	$sql="SELECT * FROM userbook where email='".$_GET['email']."'";
//echo $sql;
$query=mysqli_query($con,$sql);
while ($row=mysqli_fetch_array($query)) {
	# code...
	$detail=array();
	$detail['id']=$row['id'];
	$detail['title']=$row['title'];
	$detail['author']=$row['author'];
	$detail['publication']=$row['publication'];
	$detail['link']=$row['link'];
	array_push($response["detail"], $detail);	
}
echo json_encode($response);
 }

?>