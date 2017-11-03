<?php
require_once "../php/app.php";
$user_id = isset($_POST["user_id"]) ? $_POST["user_id"] : -1;
$id_hash = isset($_POST["id_hash"]) ? $_POST["id_hash"] : -1;
$username = isset($_POST["username"]) ? $_POST["username"] : "";
$password = isset($_POST["password"]) ? $_POST["password"] : "";

$r = array("username" => $username, "password" => $password);
$authorized = false;
if ($user_id > -1 && md5($user_id.KEY) === $id_hash) {
	$authorized = true;
} else {
	if (!empty($username) && !empty($password)) {
		$query = "SELECT `id`, `name`, `username`, `email` FROM `users` WHERE (`username`='$username' OR `email`='$username') AND BINARY `password`='$password';";
		$result = mysqli_query($link, $query);
		if ($result && mysqli_num_rows($result) > 0) {
			$row = mysqli_fetch_assoc($result);
			$authorized = true;
			$r["data"] = $row;
			$r["hash"] = md5($user_id.KEY);
		}
	}
}
$r["authorized"] = $authorized;
header("Content-Type: application/json");
echo json_encode($r);
?>
