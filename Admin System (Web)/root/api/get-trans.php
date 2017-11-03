<?php
require_once "../php/app.php";
$list = getTrans();
echo json_encode($list);
?>
