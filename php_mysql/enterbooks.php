<!DOCTYPE html>
<html>
<head>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>

	<title>Author Section</title>
<meta name="theme-color" content="#263238">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/css/materialize.min.css">
<link rel="stylesheet" href="path/to/font-awesome/css/font-awesome.min.css">
  <!-- Compiled and minified JavaScript -->
 <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/js/materialize.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        // body...
    $(".button-collapse").sideNav();
    });
</script>
  <style type="text/css">
  .nav-wrapper{
    background-color: #37474F;
  }
  button{
    background-color: #37474F!important;
  }
  label{
    color:#263238; 
  }
  .fa{
    color:white;
    /*background-color: white;*/
  }
  [type="checkbox"]:not(:checked), [type="checkbox"]:checked {
    position: relative!important;
    opacity: 1;
    left: 20px;
  }
  </style>
</head>
<body>
<nav>
    <div class="nav-wrapper">
      <a href="#!" class="brand-logo">Author Section</a>
      <a href="#" data-activates="mobile-demo" class="button-collapse" ><i class="fa fa-arrow-left" ></i></a>
      </div>
      </nav>
<div class="container">
<form enctype='multipart/form-data' method="POST" action="enterbooks.php">
	<label>Title:</label><input type="text" name="title" placeholder="2 states" required></input></br>
	<label>Author:</label><input type="text" name="author" placeholder="chetan bhagat" required></input></br>
	<label>Publication:</label><input type="text" name="publication" placeholder="abc publication Ltd" required></input></br>
	<label>Pages</label><input type="number" name="pages" placeholder="124" required></input></br>
	<label>Type:</label><input type="text" name="type" placeholder="fiction or non-fiction" required></input></br>
	<label>Category:</label><input type="text" name="category" placeholder="romance" required></input></br>
	<label>Link:</label><input type="text" name="e_commerce_link" placeholder="https://www.xyz.com?......" required></input></br>
  <label>Link:</label><input type="text" name="goodread" placeholder="https://www.roodread.com?......" required></input></br>
	<label>Language:</label><input type="text" name="language" placeholder="english" required></input></br>
	<label>Poster:</label><input type="file" size="1000" name="image" placeholder="poster"></input></br>
	<label>Sample Book:</label><input type="file" name="sample" placeholder="sample book"></input></br>
	<label>Book:</label><input type="file" name="book" placeholder="book"></input></br>
	<button type="submit" name="submit" class=btn-large waves-effect waves-light modal-trigger>Add Book</button>
</form>
</div>
<!-- <div class="input-field col s12">
    <select>
      <option value="" name="type" selected>Choose your option</option>
      <option value="1">Option 1</option>
      <option value="2">Option 2</option>
    </select>
    <label>type</label>
  </div> -->

</body>
</html>

 <?php
require 'config.php';
if (isset($_POST['submit'])) {
    # code...

 //$con=mysqli_connect("localhost","root","","bookden") or die(mysqli_error());

$target_dir = "images/";
$poster_file = $target_dir . basename($_FILES["image"]["name"]);
$uploadOk = 1;

$target_dir="samples/";
$sample_file=$target_dir . basename($_FILES["sample"]["name"]);

$target_dir="books/";
$book_file=$target_dir.basename($_FILES["book"]["name"]);

// Check if file already exists
if (file_exists($poster_file)) {
    echo "Sorry, same name Poster already exists please change the name and again upload.";
    $uploadOk = 0;
}
if (file_exists($sample_file)) {
    echo "Sorry, same name Poster already exists please change the name and again upload.";
    $uploadOk = 0;
}
if (file_exists($book_file)) {
    echo "Sorry, same name Poster already exists please change the name and again upload.";
    $uploadOk = 0;
}

// Check file size
if ($_FILES["image"]["size"] > 5000000) {
    echo "Sorry, your poster file is too large.";
    $uploadOk = 0;
}
if ($_FILES["sample"]["size"] > 5000000) {
    echo "Sorry, your sample file is too large.";
    $uploadOk = 0;
}if ($_FILES["book"]["size"] > 5000000) {
    echo "Sorry, your book file is too large.";
    $uploadOk = 0;
}
if ($uploadOk == 0) {
    echo "Sorry, your file was not uploaded.";
// if everything is ok, try to upload file
} else {
    if (move_uploaded_file($_FILES["image"]["tmp_name"], $poster_file) && move_uploaded_file($_FILES["sample"]["tmp_name"], $sample_file) && move_uploaded_file($_FILES["book"]["tmp_name"],$book_file )) {
        echo "The file ". basename( $_FILES["image"]["name"])." ".basename($_FILES["sample"]["name"])." ".basename($_FILES["book"]["name"])." has been uploaded.";

 // VALUES (NULL, '2 states', 'chetan bhaghat', 'xyz', '11', 'fiction', 'romance', 'dffd', 'dsada', 'dsjkhsk', 'dsewe', 'fdfdgd');


        $sql="INSERT INTO `books` (`id`, `title`, `author`, `publication`, `pages`, `type`, `category`, `e_commerce_link`, `goodread`, `language`, `cover`, `sample`, `book`) VALUES(NULL,'".$_POST['title']."','".$_POST['author']."','".$_POST['publication']."','".$_POST['pages']."','".$_POST['type']."','".$_POST['category']."','".$_POST['e_commerce_link']."','".$_POST['goodread']."','".$_POST['language']."','".$poster_file."','".$sample_file."','".$book_file."')";
        echo $sql;
        mysqli_query($con,$sql);

    } else {
        echo "Sorry, there was an error uploading your file.";
    }
}
}
?>


