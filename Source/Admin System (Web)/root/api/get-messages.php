<?php
require_once "../php/app.php";
openSession("../login.php");
$list = getMessages();
echo json_encode($list);
?>
