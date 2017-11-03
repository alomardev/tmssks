<?php
require_once "../php/app.php";
openSession();

echo deleteUser($_POST['id']);
?>
