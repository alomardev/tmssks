<?php
require_once "php/app.php";
openSession("./login.php");
if ($session['role'] == 2) {
	header("Location: ./mykids.php");
	exit;
}
include "php/ui.top.php"; ?>
<h2>Manipulations</h2>
<div class="row">
	<?php if ($session['role'] == ROLE_PARENT) { ?>
		<div class="col" style="width: 100%;"><a class="button big full-width" href="./mykids.php">Kids List</a></div>
		<!-- <div class="col"><a class="button big full-width" href="./messaging.php">Messaging</a></div> -->
	<?php } elseif ($session['role'] == ROLE_SCHOOL_ADMIN) { ?>
		<div class="col" style="box-sizing: border-box; width: 30%;"><a class="button big full-width" href="./kids.php">Kids Info</a></div>
		<div class="col" style="box-sizing: border-box; width: 30%;"><a class="button big full-width" href="./parents.php">Parents Info</a></div>
		<div class="col" style="box-sizing: border-box; width: 30%;"><a class="button big full-width" href="./trans.php">Transprotations</a></div>
		<!-- <div class="col"><a class="button big full-width" href="./messaging.php">Messaging</a></div> -->
	<?php } ?>
</div>
<?php include "php/ui.bottom.php"; ?>
