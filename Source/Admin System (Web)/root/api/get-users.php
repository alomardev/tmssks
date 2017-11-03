<?php
require_once "../php/app.php";
$list = getUsers($_GET['role']);
echo json_encode($list);
?>
