<?php 
	/**
	* created by SHA group ("amiSHA","diSHA","guruSHA");
	*/
	class DB_Connect 
	{
		private $conn;
		public function connect()
		{
			# code...
			require_once 'include/config.php';
			$this->conn=new mysqli(DB_HOST, DB_USER,DB_PASSWORD,DB_DATABASE);
			return $this->conn;
		}
	}
 ?>