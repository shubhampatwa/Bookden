
<?php 
// $title="";
// $author="";
// $str= utf8_decode(urldecode("http://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]"));
// //header("location:$str");
// // echo "$str\n<br>";
// //echo $str[];
// $str=explode("?",$str);
// // echo $str[1]."<br>";
// $st=explode("&&", $str[1]);
//  //echo $st[0],$st[1];
// $first=explode("=", $st[0]);
// $second=explode("=", $st[1]);
// //echo $first[0],$second[0];
// if(strcmp("title",$first[0])==0){
// //	echo "yooo";
// 	$title=$first[1];
// }
// if (strcmp("author", $second[0])==0) {
// 	# code...
// 	$author=$second[1];
// }
//echo $title,$author;
//$con=mysqli_connect("localhost","root","","bookden");
require 'config.php';
if (isset($_GET['type']) && isset($_GET['category'])) {
	# code...

$sql="SELECT * FROM books where type='".$_GET['type']."'&&category='".$_GET['category']."'";
$query=mysqli_query($con,$sql);
$exists=mysqli_num_rows($query);
$response["data"]=array();
if($exists)
{
while ($row=mysqli_fetch_array($query)) {
	# code...
	$data=array();
	$data['title']=$row['title'];
	$data['author']=$row['author'];
	$data['publication']=$row['publication'];
	$data['link']=$row['book'];
	array_push($response["data"], $data);

}
echo json_encode($response);
}
else{
	echo "no books found";
}
}
$response["detail"]=array();
if(isset($_POST['title']) && isset($_POST['author']))
{
	//echo "yes entered";
	//echo $_GET['title'];
	//echo $_GET['author'];
	//$title=substr($_GET['title'], 1,-1);
	//$author=substr($_GET['author'],1,-1);
	//echo $title;
	//echo $author;
$sql="SELECT * FROM books where title='".$_POST['title']."' and author='".$_POST['author']."'";
//echo $sql;
$query=mysqli_query($con,$sql);
while ($row=mysqli_fetch_array($query)) {
	# code...
	$detail=array();
	$detail['id']=$row['id'];
	$detail['title']=$row['title'];
	$detail['author']=$row['author'];
	$detail['publication']=$row['publication'];
	$detail['pages']=$row['pages'];
	$detail['type']=$row['type'];
	$detail['category']=$row['category'];
	$detail['e_commerce_link']=$row['e_commerce_link'];
	$detail['goodread']=$row['goodread'];
	$detail['sample']=$row['sample'];
	$detail['cover']=$row['cover'];
	$detail['language']=$row['language'];
	array_push($response["detail"], $detail);	
}
echo json_encode($response);
 }
?>