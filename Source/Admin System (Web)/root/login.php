<?php
require_once "php/app.php";

if (openSession()) {
	header("location: ./");
	exit;
}

$error = false;
if (isset($_POST["username"], $_POST["password"])) {
	$username = $_POST["username"];
	$password = $_POST["password"];
	$query = "SELECT `id` FROM `users` WHERE (`username`='$username' OR `email`='$username') AND BINARY `password`='$password';";
	$result = mysqli_query($link, $query);
	if ($result && mysqli_num_rows($result) > 0) {
		$row = mysqli_fetch_assoc($result);
		setcookie(COOKIE_ID, $row['id']."|".md5("$row[id]".KEY), time() + 43200);
		header("location: ./");
		exit;
	} else {
		$error = true;
	}
}

$css = "login.css";
include "php/ui.top.php";
?>
<div class="row">
	<div class="col">
		<h2>About the System</h2>
		Welcome to Transport Monitor System for School Kids Safety web site which
		helps school admins to manage parents and kids relation as well as to register transportations.
		Parents also can access the system to update their profile and to list
		the kids to check the validity of the information.
	</div>
	<div class="col">
		<h2>Login with your account</h2>
		<form id="login-form" action="./login.php" method="POST">
			<table>
				<tr><td colspan="2"><input<?php if ($error) echo " class='error'"; ?> type="text" name="username" placeholder="Username or Email" requried></td></tr>
				<tr><td colspan="2"><input<?php if ($error) echo " class='error'"; ?> type="password" name="password" placeholder="Password" requried></td></tr>
				<tr><td id="remember-cell"><label><input type="checkbox" value="remember" checked> Remember me</label></td><td><input type="submit" value="Sign in"></td></tr>
			</table>
		</form>
	</div>
</div>
<?php include "php/ui.bottom.php"; ?>
