<?php
require_once "../php/app.php";
openSession();

echo deleteTrans($_POST['id']);
?>
