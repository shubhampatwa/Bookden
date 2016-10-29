<?php 
	/**
	* created by SHA group ("amiSHA","diSHA","guruSHA");
	*/
	class DB_Functions 
	{
		private $conn;
		
		//constructor 
		function __construct(){
			require_once 'include/db_connect.php';
			$db=new DB_Connect();
			$this->conn=$db->connect();

		}

		//destructor
		function __destruct(){

		}
		
		/*
		*storing new user
		*return user details
		*/
		public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT unique_id, name, email, created_at, updated_at FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->store_result();
            $stmt->bind_result($unique_id, $name, $email, $created_at, $updated_at);
            $user=array();
            while($stmt->fetch()){
                $user["unique_id"]=$unique_id;
                $user["name"]=$name;
                $user["email"]=$email;
                $user["created_at"]=$created_at;
                $user["updated_at"]=$updated_at;
            }
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
/**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT unique_id, name, email, encrypted_password, salt, created_at, updated_at  FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $stmt->store_result();
            $stmt->bind_result($unique_id, $name, $email, $encrypted_password, $salt, $created_at, $updated_at);
            $user=array();
            while($stmt->fetch()){
                $user["unique_id"]=$unique_id;
                $user["name"]=$name;
                $user["email"]=$email;
                $user["encrypted_password"]=$encrypted_password;
                $user["salt"]=$salt;
                $user["created_at"]=$created_at;
                $user["updated_at"]=$updated_at;
            }
            $stmt->close();
 
            // verifying user password
            $salt = $user["salt"];
            $encrypted_password = $user["encrypted_password"];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
	}
 ?>