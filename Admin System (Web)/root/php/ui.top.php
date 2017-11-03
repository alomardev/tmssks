<?php $pagename = basename($_SERVER["PHP_SELF"]); ?>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/global.css">
	<?php if (isset($css)) {
		echo "<link rel=\"stylesheet\" href=\"css/$css\">";
	 } ?>
	<link rel="shortcut icon" href="res/drawable/favicon.ico" type="image/x-icon">
	<link rel="icon" href="res/drawable/favicon.ico" type="image/x-icon">
	<title>TMSSKS
	<?php
	if (isset($title)) {
		echo " - $title";
	}
	?>
	</title>
</head>
<body>
<div id="page">
<header>
	<nav>
		<div class="wrapper">
			<ul class="clear-float">
			<?php if (!is_null($session)) { ?>
				<li><a href="./logout.php">Logout (<?php echo $session['username']; ?>)</a></li>
				<li><a <?php if ($pagename == "profile.php") { ?> class="active" <?php } ?> href="./profile.php">Profile</a></li>
				<!-- <li><a <?php if ($pagename == "messaging.php") { ?> class="active" <?php } ?> href="./messaging.php">Messaging</a></li> -->
				<?php if ($session['role'] == ROLE_PARENT) { ?>
					<li><a <?php if ($pagename == "mykids.php") { ?> class="active" <?php } ?> href="./mykids.php">My Kids</a></li>
				<?php } elseif ($session['role'] == ROLE_SCHOOL_ADMIN) { ?>
					<li><a <?php if ($pagename == "kids.php") { ?> class="active" <?php } ?> href="./kids.php">Kids</a></li>
					<li><a <?php if ($pagename == "parents.php") { ?> class="active" <?php } ?> href="./parents.php">Parents</a></li>
					<li><a <?php if ($pagename == "trans.php") { ?> class="active" <?php } ?> href="./trans.php">Transportations</a></li>
				<?php } ?>
			<?php } else { ?>
				<li><a <?php if ($pagename == "login.php") { ?> class="active" <?php } ?> href="./login.php">Login</a></li>
			<?php } ?>
			</ul>
		</div>
	</nav>
	<div class="wrapper">
		<div id="brand">
		<table>
			<tr>
				<td>
					<img src="res/drawable/logo.svg">
				</td>
				<td>
					<h1>Transport Monitoring System</h1>
					<h2>for School Kids Safety</h2>
				</td>
			</tr>
		</table>
		</div>
	</div>
</header>
<section>
	<div class="wrapper">
		<div class="card">
