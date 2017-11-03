<?php
require_once "php/app.php";
openSession("./login.php");

if (isset($_POST['apply_submit'])) {
	$user = array('id' => $session['id'],
	 'name' => $_POST['name'],
	 'email' => $_POST['email'],
	 'phone1' => $_POST['phone1'],
	 'phone2' => $_POST['phone2'],
	 'password' => $_POST['new_password'],
	 'old_password' => $_POST['old_password']);
	saveUser($user);
	header('location: ./profile.php');
	exit();
}

$phones = array();
$query = "SELECT * FROM `phones` WHERE `user_id`=$session[id];";
$result = mysqli_query($link, $query);
if ($result) {
	$p = 0;
	while ($row = mysqli_fetch_assoc($result)) {
		$phones[$p++] = $row['phone'];
	}
}

include "php/ui.top.php"; ?>

<h2>Profile</h2>
<form action="" method="POST">

<div class="row">
	<div class="col input-group">
		<label>Name</label>
		<input name='name' type="text" value="<?php echo $session['name']; ?>">
	</div>
	<div class="col input-group">
		<label>Email</label>
		<input type="text" name='email', value="<?php echo $session['email']; ?>">
	</div>
	<div class="col input-group">
		<label>Change Password</label>
		<input type="password" name='old_password' placeholder="Current password">
		<input type="password" name='new_password' placeholder="New Password">
	</div>
	<div class="col input-group">
		<label>Phone Numbers</label>
		<input name='phone1' type="tel" placeholder="Phone 1" value="<?php echo (isset($phones[0]) && $phones[0] != null) ? $phones[0] : ""; ?>">
		<input name='phone2' type="tel" placeholder="Phone 2" value="<?php echo (isset($phones[1]) && $phones[1] != null) ? $phones[1] : ""; ?>">
	</div>
</div>

<div class="seperator"></div>

<div class="clear-float">
	<input class="float-right" type="submit" name="apply_submit" value="Apply Changes">
</div>
</form>

</div>
<?php include "php/ui.bottom.php"; ?>
