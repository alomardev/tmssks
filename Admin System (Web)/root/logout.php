<?php
require_once "php/app.php";
setcookie(COOKIE_ID, "", 1);
header("location: ./login.php");
?>
